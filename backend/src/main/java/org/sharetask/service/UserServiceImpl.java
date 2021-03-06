/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.sharetask.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.InvitationService;
import org.sharetask.api.UserService;
import org.sharetask.api.dto.InvitationDTO;
import org.sharetask.api.dto.UserDTO;
import org.sharetask.api.dto.UserInfoDTO;
import org.sharetask.entity.Role;
import org.sharetask.entity.User;
import org.sharetask.repository.UserRepository;
import org.sharetask.security.UserDetailsImpl;
import org.sharetask.utility.DTOConverter;
import org.sharetask.utility.HashCodeUtil;
import org.sharetask.utility.SecurityUtil;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	@Inject
	private UserRepository userRepository;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private SaltSource saltSource;

	@Inject 
	private InvitationService invitationService; 

	private static class UserDetailBuilder {

		private final User user;

		public UserDetailBuilder(final User user) {
			this.user = user;
		}

		public UserDetails build() {
			final String username = user.getUsername();
			final String password = user.getPassword();
			final String salt = user.getSalt();
			final boolean enabled = user.isEnabled();
			final boolean accountNonExpired = user.isEnabled();
			final boolean credentialsNonExpired = user.isEnabled();
			final boolean accountNonLocked = user.isEnabled();

			final Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (final Role role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.name()));
			}

			final UserDetails userDetails = new UserDetailsImpl(username, password, salt,
					enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
			return userDetails;
		}
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userRepository.findByUsername(username);
		UserDetails userDetails = null;
		if (user != null) {
			userDetails = new UserDetailBuilder(user).build();
		} else {
			throw new UsernameNotFoundException("User with name: " + username + " not found");
		}
		return userDetails;
	}

	@Override
	@Transactional
	public UserDTO create(final UserDTO userDTO) {
		log.info("Registering user: {}", userDTO);
		final User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setName(userDTO.getName());
		user.setSurName(userDTO.getSurName());

		if (userRepository.findByUsername(userDTO.getUsername()) != null) {
			throw new UserAlreadyExistsException();
		}

		user.setEmail(user.getUsername());
		final Collection<Role> roles = new ArrayList<Role>();
		user.setEnabled(false);

		roles.add(Role.ROLE_USER);
		user.setRoles(roles);

		// salt create
		final String salt = getSalt(userDTO.getUsername());
		user.setSalt(salt);

		final UserDetails userDetails = new UserDetailsImpl(user.getUsername(), "password", salt,
				new ArrayList<GrantedAuthority>());
		user.setPassword(passwordEncoder.encodePassword(userDTO.getPassword(),
				saltSource.getSalt(userDetails)));
		final User storedUser = userRepository.save(user);
		invitationService.inviteRegisteredUser(userDTO.getUsername());
		return DTOConverter.convert(storedUser,  UserDTO.class);
	}

	private String getSalt(final String username) {
		return HashCodeUtil.getHashCode(System.currentTimeMillis() + username);
	}

	@Override
	@Transactional
	public UserInfoDTO update(final UserInfoDTO userInfoDTO) {
		final User user = userRepository.read(userInfoDTO.getUsername());
		user.setName(userInfoDTO.getName());
		user.setSurName(userInfoDTO.getSurName());
		final User storedUser = userRepository.save(user);
		return DTOConverter.convert(storedUser, UserInfoDTO.class);
	}

	@Override
	public UserInfoDTO read(final String username) {
		final User user = userRepository.read(username);
		return DTOConverter.convert(user, UserInfoDTO.class);
	}
	
	@Override
	@Transactional
	public void changePassword(final String password) {
		final String username = SecurityUtil.getCurrentSignedInUsername();
		final User user = userRepository.read(username);
		final UserDetails userDetails = new UserDetailsImpl(user.getUsername(), "password", user.getSalt(),
				new ArrayList<GrantedAuthority>());
		user.setPassword(passwordEncoder.encodePassword(password, saltSource.getSalt(userDetails)));
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void confirmInvitation(@NotNull final String code) {
		final InvitationDTO invitation = invitationService.confirmInvitation(code);
		final User user = userRepository.read(invitation.getEmail());
		user.setEnabled(true);
		userRepository.save(user);
	}
	
}
