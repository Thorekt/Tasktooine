package com.thorekt.tasktooine_api.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.thorekt.tasktooine_api.dto.ProjectDto;
import com.thorekt.tasktooine_api.service.ProjectService;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProjectController(projectService)).build();
    }

    @Test
    void returnsProjectForValidId() throws Exception {
        // Given
        var id = UUID.randomUUID();
        var dto = new ProjectDto(id, "Project", "Description", List.of());
        given(projectService.getProjectById(id)).willReturn(dto);

        // When
        var result = mockMvc.perform(get("/project/{id}", id));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Project"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.taskLists").isEmpty());
        then(projectService).should().getProjectById(id);
    }

    @Test
    void returnsNotFoundForMissingProject() throws Exception {
        // Given
        var id = UUID.randomUUID();
        given(projectService.getProjectById(id))
                .willThrow(new IllegalArgumentException("Project with ID " + id + " does not exist."));

        // When
        var result = mockMvc.perform(get("/project/{id}", id));

        // Then
        result.andExpect(status().isNotFound());
        then(projectService).should().getProjectById(id);
    }

    @Test
    void returnsBadRequestForInvalidUuid() throws Exception {
        // Given
        var invalidId = "invalid-uuid";

        // When
        var result = mockMvc.perform(get("/project/{id}", invalidId));

        // Then
        result.andExpect(status().isBadRequest());
        then(projectService).shouldHaveNoInteractions();
    }
}
