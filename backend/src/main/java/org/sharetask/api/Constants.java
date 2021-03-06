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

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface Constants {

	long VERSION = 1000000L;

	String PERIMISSION_WORKSPACE_MEMBER_OR_OWNER = "isAuthenticated() and hasRole('ROLE_USER') and " +
			"hasPermission(#workspaceId, 'isWorkspaceMemberOrOwner')";

	String PERIMISSION_WORKSPACE_OWNER = "isAuthenticated() and hasRole('ROLE_USER') and " +
			"hasPermission(#workspaceId, 'isWorkspaceOwner')";

	String PERMISSION_TASK_ASSIGNEE = "isAuthenticated() and hasRole('ROLE_USER') and " +
			"hasPermission(#taskId, 'isTaskAssignee')";

	String PERMISSION_TASK_ASSIGNEE_OR_CREATOR = "isAuthenticated() and hasRole('ROLE_USER') and " +
			"hasPermission(#taskId, 'isTaskAssigneeOrCreator')";
}
