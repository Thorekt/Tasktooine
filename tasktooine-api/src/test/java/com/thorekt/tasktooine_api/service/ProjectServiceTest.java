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

import com.thorekt.tasktooine_api.dto.ProjectDto;
import com.thorekt.tasktooine_api.model.Project;
import com.thorekt.tasktooine_api.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository repository;

    @InjectMocks
    private ProjectService service;

    private final UUID id = UUID.randomUUID();
    private final ProjectDto dto = ProjectDto.builder()
            .id(id)
            .name("Test project")
            .description("Description")
            .build();

    @Test
    void createsProjectFromDto() {
        service.createProject(dto);

        var captor = ArgumentCaptor.forClass(Project.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test project");
        assertThat(captor.getValue().getDescription()).isEqualTo("Description");
    }

    @Test
    void updatesExistingProject() {
        when(repository.existsById(id)).thenReturn(true);

        service.updateProject(dto);

        var captor = ArgumentCaptor.forClass(Project.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test project");
        assertThat(captor.getValue().getDescription()).isEqualTo("Description");
    }

    @Test
    void rejectsUpdateWhenProjectDoesNotExist() {
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.updateProject(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Project with ID " + id + " does not exist.");
        verify(repository, never()).save(any());
    }

    @Test
    void deletesExistingProject() {
        when(repository.existsById(id)).thenReturn(true);

        service.deleteProject(dto);

        var captor = ArgumentCaptor.forClass(Project.class);
        verify(repository).delete(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getName()).isEqualTo("Test project");
    }

    @Test
    void rejectsDeletionWhenProjectDoesNotExist() {
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteProject(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Project with ID " + id + " does not exist.");
        verify(repository, never()).delete(any(Project.class));
    }

    @Test
    void returnsDtoForExistingProject() {
        var model = Project.builder()
                .id(id)
                .name("Test project")
                .description("Description")
                .build();
        when(repository.findById(id)).thenReturn(Optional.of(model));

        assertThat(service.getProjectById(id)).isEqualTo(dto);
    }

    @Test
    void rejectsLookupWhenProjectDoesNotExist() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProjectById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Project with ID " + id + " does not exist.");
    }
}
