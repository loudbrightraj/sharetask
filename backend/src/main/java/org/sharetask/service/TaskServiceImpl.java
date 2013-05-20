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
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.TaskQueue;
import org.sharetask.api.TaskService;
import org.sharetask.api.dto.CommentDTO;
import org.sharetask.api.dto.EventDTO;
import org.sharetask.api.dto.TaskDTO;
import org.sharetask.entity.Comment;
import org.sharetask.entity.Event;
import org.sharetask.entity.Event.EventType;
import org.sharetask.entity.Task;
import org.sharetask.entity.Task.PriorityType;
import org.sharetask.entity.Task.StateType;
import org.sharetask.entity.User;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.TaskRepository;
import org.sharetask.repository.UserRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.sharetask.utility.DTOConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Task service implementation.
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

	@Inject
	private TaskRepository taskRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private WorkspaceRepository workspaceRepository;

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#create(org.sharetask.api.dto.TaskDTO)
	 */
	@Override
	@Transactional
	public TaskDTO create(final Long workspaceId, final TaskDTO task) {
		final Workspace workspace = workspaceRepository.read(workspaceId);
		Task taskEntity = DTOConverter.convert(task, Task.class);
		taskEntity.setWorkspace(workspace);
		taskEntity = taskRepository.save(taskEntity);
		return DTOConverter.convert(taskEntity, TaskDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#addComment(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	public TaskDTO addComment(final Long taskId, final String message) {
		final Task task = taskRepository.read(taskId);
		task.addComment(new Comment(message));
		taskRepository.save(task);
		return DTOConverter.convert(task, TaskDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#findTaskByQueue(java.lang.Long, org.sharetask.api.TaskQueue)
	 */
	@Override
	public List<TaskDTO> findTaskByQueue(final Long workspaceId, final TaskQueue taskQueue) {
		List<Task> result;
		switch (taskQueue) {
			case ALL:
				result = taskRepository.findByWorkspaceId(workspaceId);
				break;
			case EXPIRED:
				result = taskRepository.findByDueDateLessThan(new Date());
				break;
			case HIGH_PRIORITY:
				result = taskRepository.findByPriority(PriorityType.HIGH);
				break;
			case FINISHED:
				result = taskRepository.findByState(StateType.FINISHED);
				break;
			case TODAY:
				//TODO 
				result = taskRepository.findByDueDate(new Date());
				break;
			default:
				result = new ArrayList<Task>();
		}
		return DTOConverter.convertList(result, TaskDTO.class);
	}
	
	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#update(org.sharetask.api.dto.TaskDTO)
	 */
	@Override
	@Transactional
	public TaskDTO update(TaskDTO task) {
		Task entity = taskRepository.findOne(task.getId());
		DTOConverter.convert(task, entity);
		entity = taskRepository.save(entity);
		return DTOConverter.convert(entity, TaskDTO.class);
	}
	
	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#complete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void complete(Long taskId) {
		final Task entity = taskRepository.findOne(taskId);
		entity.finish();
		taskRepository.save(entity);
	}
	
	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#forward(java.lang.Long, java.lang.String)
	 */
	@Override
	public void forward(Long taskId, String assignee) {
		final Task task = taskRepository.read(taskId);

		final User assigneeUser = userRepository.read(assignee);
		if (task.getWorkspace().getMembers().contains(assigneeUser)) {
			log.debug("Added user {} is member of workspace.", assigneeUser.getUsername());
			task.setAssignee(assigneeUser);
		}
		
		task.addEvent(new Event(EventType.TASK_FORWARDED));
		taskRepository.save(task);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#getComments(java.lang.Long)
	 */
	@Override
	public List<CommentDTO> getComments(final Long taskId) {
		final Task task = taskRepository.read(taskId);
		return DTOConverter.convertList(task.getComments(), CommentDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#getEvents(java.lang.Long)
	 */
	@Override
	public List<EventDTO> getEvents(final Long taskId) {
		final Task task = taskRepository.read(taskId);
		return DTOConverter.convertList(task.getEvents(), EventDTO.class);
	}

	@Override
	public void delete(final Long taskId) {
		final Task task = taskRepository.read(taskId);
		task.setState(StateType.DELETED);
		taskRepository.save(task);
	}
}
