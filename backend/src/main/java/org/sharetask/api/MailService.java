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
package org.sharetask.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.sharetask.api.dto.InvitationDTO;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface MailService {
	
	/**
	 * Send invitation email.
	 * @param invitation
	 */
	void sendInvitation(@NotNull final InvitationDTO invitation);

	void sendEmail(@NotNull final String from, @NotNull final List<String> to, @NotNull final String subject, 
			@NotNull final String msq, final int retry);
}
