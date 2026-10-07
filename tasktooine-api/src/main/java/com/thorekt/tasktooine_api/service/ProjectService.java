package com.thorekt.tasktooine_api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.thorekt.tasktooine_api.dto.ProjectDto;
import com.thorekt.tasktooine_api.repository.ProjectRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional 
    public void createProject(ProjectDto projectDto) {
        projectRepository.save(projectDto.toModel());
    }

    @Transactional 
    public void updateProject(ProjectDto projectDto) {
        if (!projectRepository.existsById(projectDto.getId())) {
            throw new IllegalArgumentException("Project with ID " + projectDto.getId() + " does not exist.");
        }
        projectRepository.save(projectDto.toModel());
    }

    @Transactional 
    public void deleteProject(ProjectDto projectDto) {
        if (!projectRepository.existsById(projectDto.getId())) {
            throw new IllegalArgumentException("Project with ID " + projectDto.getId() + " does not exist.");
        }
        projectRepository.delete(projectDto.toModel());
    }

    public ProjectDto getProjectById(UUID projectId) {
        return projectRepository.findById(projectId)
                .map(ProjectDto::fromModel)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));
    }
}
