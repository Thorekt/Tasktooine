package com.thorekt.tasktooine_api.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.thorekt.tasktooine_api.model.Project;

public interface ProjectRepository extends CrudRepository<Project, UUID> {
}
