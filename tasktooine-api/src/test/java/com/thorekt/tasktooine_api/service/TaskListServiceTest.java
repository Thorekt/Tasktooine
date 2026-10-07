package com.thorekt.tasktooine_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thorekt.tasktooine_api.dto.TaskListDto;
import com.thorekt.tasktooine_api.model.TaskList;
import com.thorekt.tasktooine_api.repository.TaskListRepository;

@ExtendWith(MockitoExtension.class)
class TaskListServiceTest {

    @Mock
    private TaskListRepository repository;

    @InjectMocks
    private TaskListService service;

    private final UUID id = UUID.randomUUID();
    private final TaskListDto dto = TaskListDto.builder()
            .id(id)
            .name("Test taskList")
            .build();

    @Test
    void createsTaskListFromDto() {
        service.createTaskList(dto);

        var captor = ArgumentCaptor.forClass(TaskList.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test taskList");
    }

    @Test
    void updatesExistingTaskList() {
        when(repository.existsById(id)).thenReturn(true);

        service.updateTaskList(dto);

        var captor = ArgumentCaptor.forClass(TaskList.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test taskList");
    }

    @Test
    void rejectsUpdateWhenTaskListDoesNotExist() {
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.updateTaskList(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task list with ID " + id + " does not exist.");
        verify(repository, never()).save(any());
    }

    @Test
    void deletesExistingTaskList() {
        when(repository.existsById(id)).thenReturn(true);

        service.deleteTaskList(dto);

        var captor = ArgumentCaptor.forClass(TaskList.class);
        verify(repository).delete(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test taskList");
    }

    @Test
    void rejectsDeletionWhenTaskListDoesNotExist() {
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteTaskList(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task list with ID " + id + " does not exist.");
        verify(repository, never()).delete(any(TaskList.class));
    }

    @Test
    void returnsDtoForExistingTaskList() {
        var model = TaskList.builder()
                .id(id)
                .name("Test taskList")
                .build();
        when(repository.findById(id)).thenReturn(Optional.of(model));

        assertThat(service.getTaskListById(id)).isEqualTo(dto);
    }

    @Test
    void rejectsLookupWhenTaskListDoesNotExist() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTaskListById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task list with ID " + id + " does not exist.");
    }
}
