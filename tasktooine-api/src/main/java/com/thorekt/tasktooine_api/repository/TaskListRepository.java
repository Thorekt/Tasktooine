package com.thorekt.tasktooine_api.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.thorekt.tasktooine_api.model.TaskList;

public interface TaskListRepository extends CrudRepository<TaskList, UUID> {
}
