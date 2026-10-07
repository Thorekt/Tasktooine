package com.thorekt.tasktooine_api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.thorekt.tasktooine_api.model.Project;
import com.thorekt.tasktooine_api.model.Task;
import com.thorekt.tasktooine_api.model.TaskList;

class ProjectDtoTest {

    @Test
    void convertsFieldsAndNestedListsToModel() {
        var id = UUID.randomUUID();
        var listId = UUID.randomUUID();
        var taskId = UUID.randomUUID();
        var taskDto = new TaskDto(taskId, "Task", "Task description");
        var listDto = new TaskListDto(listId, "List", List.of(taskDto));
        var dto = new ProjectDto(id, "Project", "Description", List.of(listDto));

        var model = dto.toModel();

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Project");
        assertThat(model.getDescription()).isEqualTo("Description");
        assertThat(model.getTaskLists()).singleElement().satisfies(list -> {
            assertThat(list.getProject()).isSameAs(model);
            assertThat(list.getId()).isEqualTo(listId);
            assertThat(list.getName()).isEqualTo("List");
            assertThat(list.getTasks()).singleElement().satisfies(task -> {
                assertThat(task.getTaskList()).isSameAs(list);
                assertThat(task.getId()).isEqualTo(taskId);
                assertThat(task.getName()).isEqualTo("Task");
                assertThat(task.getDescription()).isEqualTo("Task description");
            });
        });
    }

    @Test
    void convertsFieldsAndNestedListsFromModel() {
        var id = UUID.randomUUID();
        var listId = UUID.randomUUID();
        var taskId = UUID.randomUUID();
        var task = Task.builder().id(taskId).name("Task").description("Task description").build();
        var list = TaskList.builder().id(listId).name("List").tasks(List.of(task)).build();
        var model = Project.builder().id(id).name("Project").description("Description")
                .taskLists(List.of(list)).build();

        var dto = ProjectDto.fromModel(model);

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo("Project");
        assertThat(dto.getDescription()).isEqualTo("Description");
        assertThat(dto.getTaskLists()).containsExactly(new TaskListDto(listId, "List",
                List.of(new TaskDto(taskId, "Task", "Task description"))));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void preservesNullAndEmptyListsToModel(List<TaskListDto> lists) {
        var dto = new ProjectDto(null, "Project", null, lists);

        assertThat(dto.toModel().getTaskLists()).isEqualTo(lists);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void preservesNullAndEmptyListsFromModel(List<TaskList> lists) {
        var model = Project.builder().name("Project").taskLists(lists).build();

        assertThat(ProjectDto.fromModel(model).getTaskLists()).isEqualTo(lists);
    }
}
