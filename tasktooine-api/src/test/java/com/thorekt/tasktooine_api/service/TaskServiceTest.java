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

import com.thorekt.tasktooine_api.dto.TaskDto;
import com.thorekt.tasktooine_api.model.Task;
import com.thorekt.tasktooine_api.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private final UUID id = UUID.randomUUID();
    private final TaskDto dto = TaskDto.builder()
            .id(id)
            .name("Test task")
            .description("Description")
            .build();

    @Test
    void createsTaskFromDto() {
        service.createTask(dto);

        var captor = ArgumentCaptor.forClass(Task.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test task");
        assertThat(captor.getValue().getDescription()).isEqualTo("Description");
    }

    @Test
    void updatesExistingTask() {
        when(repository.existsById(id)).thenReturn(true);

        service.updateTask(dto);

        var captor = ArgumentCaptor.forClass(Task.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test task");
        assertThat(captor.getValue().getDescription()).isEqualTo("Description");
    }

    @Test
    void rejectsUpdateWhenTaskDoesNotExist() {
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.updateTask(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task with ID " + id + " does not exist.");
        verify(repository, never()).save(any());
    }

    @Test
    void deletesExistingTask() {
        when(repository.existsById(id)).thenReturn(true);

        service.deleteTask(dto);

        var captor = ArgumentCaptor.forClass(Task.class);
        verify(repository).delete(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test task");
    }

    @Test
    void rejectsDeletionWhenTaskDoesNotExist() {
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteTask(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task with ID " + id + " does not exist.");
        verify(repository, never()).delete(any(Task.class));
    }

    @Test
    void returnsDtoForExistingTask() {
        var model = Task.builder()
                .id(id)
                .name("Test task")
                .description("Description")
                .build();
        when(repository.findById(id)).thenReturn(Optional.of(model));

        assertThat(service.getTaskById(id)).isEqualTo(dto);
    }

    @Test
    void rejectsLookupWhenTaskDoesNotExist() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTaskById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task with ID " + id + " does not exist.");
    }
}
