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

import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.sharetask.api.NotificationQueueService;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.NotificationQueue;
import org.sharetask.repository.NotificationQueueRepository;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class NotificationQueueServiceTest extends DbUnitTest{

	@Inject
	private NotificationQueueService notificationQueueService;
	
	@Inject
	private NotificationQueueRepository notificationQueueRepository;

	/**
	 * Test method for {@link org.sharetask.api.NotificationQueueService#storeInvitation(java.lang.String, java.lang.String[], java.lang.String)}.
	 */
	@Test
	public void testStoreInvitation() {
		notificationQueueService.storeInvitation("sender@test.com", new String[] {"recipient@test.com"}, "Test subject", "Test message");
		final List<NotificationQueue> all = notificationQueueRepository.findAll();
		assertThat(all.size() > 0, CoreMatchers.equalTo(true));
	}
}
