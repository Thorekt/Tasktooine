package com.thorekt.tasktooine_api.dto;

import java.util.UUID;

import com.thorekt.tasktooine_api.model.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private UUID id;
    private String name;
    private String description;

    public Task toModel() {
        return Task.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static TaskDto fromModel(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .build();
    }
}
