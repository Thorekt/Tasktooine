package com.thorekt.tasktooine_api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.thorekt.tasktooine_api.dto.TaskDto;
import com.thorekt.tasktooine_api.repository.TaskRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    @Transactional
    public void createTask(TaskDto taskDto) {
        taskRepository.save(taskDto.toModel());
    }

    @Transactional
    public void updateTask(TaskDto taskDto) {
        if (!taskRepository.existsById(taskDto.getId())) {
            throw new IllegalArgumentException("Task with ID " + taskDto.getId() + " does not exist.");
        }
        taskRepository.save(taskDto.toModel());
    }

    @Transactional
    public void deleteTask(TaskDto taskDto) {
        if (!taskRepository.existsById(taskDto.getId())) {
            throw new IllegalArgumentException("Task with ID " + taskDto.getId() + " does not exist.");
        }
        taskRepository.delete(taskDto.toModel());
    }

    public TaskDto getTaskById(UUID taskId) {
        return taskRepository.findById(taskId)
                .map(TaskDto::fromModel)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + taskId + " does not exist."));
    }

    
}
