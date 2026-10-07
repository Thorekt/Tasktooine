package com.thorekt.tasktooine_api.dto;

import java.util.List;
import java.util.UUID;

import com.thorekt.tasktooine_api.model.TaskList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDto {

    private UUID id;
    @NonNull 
    private String name;
    private List<TaskDto> tasks;

    public TaskList toModel() {
        return TaskList.builder()
                .id(id)
                .name(name)
                .tasks(tasks == null ? null : tasks.stream()
                        .map(task -> task.toModel())
                        .toList())
                .build();
    }

    public static TaskListDto fromModel(TaskList taskList) {
        return TaskListDto.builder()
                .id(taskList.getId())
                .name(taskList.getName())
                .tasks(taskList.getTasks() == null ? null : taskList.getTasks().stream()
                        .map(TaskDto::fromModel)
                        .toList())
                .build();
    }
}
