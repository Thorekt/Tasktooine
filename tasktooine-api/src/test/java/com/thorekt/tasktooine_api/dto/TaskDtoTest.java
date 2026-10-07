package com.thorekt.tasktooine_api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.thorekt.tasktooine_api.model.Task;

class TaskDtoTest {

    @Test
    void convertsAllFieldsToModel() {
        var id = UUID.randomUUID();
        var dto = new TaskDto(id, "Task", "Description");

        var model = dto.toModel();

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Task");
        assertThat(model.getDescription()).isEqualTo("Description");
    }

    @Test
    void convertsAllFieldsFromModel() {
        var id = UUID.randomUUID();
        var model = Task.builder().id(id).name("Task").description("Description").build();

        var dto = TaskDto.fromModel(model);

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo("Task");
        assertThat(dto.getDescription()).isEqualTo("Description");
    }

    @Test
    void preservesOptionalNullFieldsInBothDirections() {
        var dto = new TaskDto(null, "Task", null);

        var model = dto.toModel();

        assertThat(model.getId()).isNull();
        assertThat(model.getDescription()).isNull();
        assertThat(TaskDto.fromModel(model)).isEqualTo(dto);
    }
}
