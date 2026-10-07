package com.thorekt.tasktooine_api.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.thorekt.tasktooine_api.model.Task;

public interface TaskRepository extends CrudRepository<Task, UUID> {
}
