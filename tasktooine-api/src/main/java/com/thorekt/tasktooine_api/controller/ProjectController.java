package com.thorekt.tasktooine_api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.thorekt.tasktooine_api.dto.ProjectDto;
import com.thorekt.tasktooine_api.service.ProjectService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping ("/project")
@RequiredArgsConstructor 
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ProjectDto getProject(@PathVariable("id") UUID id) {
        try {
            return projectService.getProjectById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    

}
