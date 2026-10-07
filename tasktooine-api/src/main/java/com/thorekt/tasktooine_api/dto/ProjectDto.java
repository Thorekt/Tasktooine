package com.thorekt.tasktooine_api.dto;

import java.util.List;
import java.util.UUID;

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
        return Project.builder()
                .id(id)
                .name(name)
                .description(description)
                .taskLists(taskLists == null ? null : taskLists.stream()
                        .map(taskList -> taskList.toModel())
                        .toList())
                .build();
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
