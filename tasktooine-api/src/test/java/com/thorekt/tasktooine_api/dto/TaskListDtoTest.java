package com.thorekt.tasktooine_api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.thorekt.tasktooine_api.model.Task;
import com.thorekt.tasktooine_api.model.TaskList;

class TaskListDtoTest {

    @Test
    void convertsFieldsAndTasksToModel() {
        var id = UUID.randomUUID();
        var taskId = UUID.randomUUID();
        var dto = new TaskListDto(id, "List", List.of(new TaskDto(taskId, "Task", "Description")));

        var model = dto.toModel();

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("List");
        assertThat(model.getTasks()).singleElement().satisfies(task -> {
            assertThat(task.getTaskList()).isSameAs(model);
            assertThat(task.getId()).isEqualTo(taskId);
            assertThat(task.getName()).isEqualTo("Task");
            assertThat(task.getDescription()).isEqualTo("Description");
        });
    }

    @Test
    void convertsFieldsAndTasksFromModel() {
        var id = UUID.randomUUID();
        var taskId = UUID.randomUUID();
        var task = Task.builder().id(taskId).name("Task").description("Description").build();
        var model = TaskList.builder().id(id).name("List").tasks(List.of(task)).build();

        var dto = TaskListDto.fromModel(model);

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo("List");
        assertThat(dto.getTasks()).containsExactly(new TaskDto(taskId, "Task", "Description"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void preservesNullAndEmptyTasksToModel(List<TaskDto> tasks) {
        var dto = new TaskListDto(null, "List", tasks);

        assertThat(dto.toModel().getTasks()).isEqualTo(tasks);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void preservesNullAndEmptyTasksFromModel(List<Task> tasks) {
        var model = TaskList.builder().name("List").tasks(tasks).build();

        assertThat(TaskListDto.fromModel(model).getTasks()).isEqualTo(tasks);
    }
}
