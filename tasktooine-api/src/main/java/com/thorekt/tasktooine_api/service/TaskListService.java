package com.thorekt.tasktooine_api.service;

import org.springframework.stereotype.Service;

import com.thorekt.tasktooine_api.dto.TaskListDto;
import com.thorekt.tasktooine_api.repository.TaskListRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskListService {
    private final TaskListRepository taskListRepository;

    @Transactional
    public void createTaskList(TaskListDto taskListDto) {
        taskListRepository.save(taskListDto.toModel());
    }

    @Transactional
    public void updateTaskList(TaskListDto taskListDto) {
        if (!taskListRepository.existsById(taskListDto.getId())) {
            throw new IllegalArgumentException("Task list with ID " + taskListDto.getId() + " does not exist.");
        }
        taskListRepository.save(taskListDto.toModel());
    }

    @Transactional
    public void deleteTaskList(TaskListDto taskListDto) {
        if (!taskListRepository.existsById(taskListDto.getId())) {
            throw new IllegalArgumentException("Task list with ID " + taskListDto.getId() + " does not exist.");
        }
        taskListRepository.delete(taskListDto.toModel());
    }

    public TaskListDto getTaskListById(UUID taskListId) {
        return taskListRepository.findById(taskListId)
                .map(TaskListDto::fromModel)
                .orElseThrow(() -> new IllegalArgumentException("Task list with ID " + taskListId + " does not exist."));
    }
}
