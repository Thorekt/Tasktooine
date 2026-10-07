package com.thorekt.tasktooine_api.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.thorekt.tasktooine_api.model.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
public class ProjectDto {

    private UUID id;
    private String name;
    private String description;
    private List<TaskListDto> taskLists;


    public Project toModel() {
        var project = Project.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
        if (taskLists != null) {
            project.setTaskLists(taskLists.stream()
                    .map(taskList -> {
                        var model = taskList.toModel();
                        model.setProject(project);
                        return model;
                    })
                    .collect(Collectors.toList()));
        }
        return project;
    }

    public static ProjectDto fromModel(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .taskLists(project.getTaskLists() == null ? null : project.getTaskLists().stream()
                        .map(TaskListDto::fromModel)
                        .toList())
                .build();
    }
}
