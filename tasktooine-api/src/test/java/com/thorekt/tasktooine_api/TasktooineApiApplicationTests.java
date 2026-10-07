package com.thorekt.tasktooine_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.thorekt.tasktooine_api.dto.ProjectDto;
import com.thorekt.tasktooine_api.dto.TaskDto;
import com.thorekt.tasktooine_api.dto.TaskListDto;
import com.thorekt.tasktooine_api.repository.ProjectRepository;
import com.thorekt.tasktooine_api.repository.TaskListRepository;
import com.thorekt.tasktooine_api.repository.TaskRepository;

import com.thorekt.tasktooine_api.service.ProjectService;
import com.thorekt.tasktooine_api.service.TaskListService;
import com.thorekt.tasktooine_api.service.TaskService;

import jakarta.persistence.EntityManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@ActiveProfiles("test")
class TasktooineApiApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskListRepository taskListRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void contextLoads() {
		assertThat(context.getBean(ProjectService.class)).isNotNull();
		assertThat(context.getBean(TaskListService.class)).isNotNull();
		assertThat(context.getBean(TaskService.class)).isNotNull();
	}

	@Test
	@Transactional
	void persistsAndDeletesProjectHierarchyWithCascade() {
		var taskDto = new TaskDto(null, "Task", "Task description");
		var listDto = new TaskListDto(null, "List", List.of(taskDto));
		var projectDto = new ProjectDto(null, "Project", "Description", List.of(listDto));

		var saved = projectRepository.save(projectDto.toModel());
		entityManager.flush();
		var projectId = saved.getId();
		var listId = saved.getTaskLists().getFirst().getId();
		var taskId = saved.getTaskLists().getFirst().getTasks().getFirst().getId();
		assertThat(projectId).isNotNull();
		assertThat(listId).isNotNull();
		assertThat(taskId).isNotNull();
		entityManager.clear();

		var reloaded = projectRepository.findById(projectId).orElseThrow();
		var dto = ProjectDto.fromModel(reloaded);
		assertThat(dto.getName()).isEqualTo("Project");
		assertThat(dto.getDescription()).isEqualTo("Description");
		assertThat(dto.getTaskLists()).singleElement().satisfies(list -> {
			assertThat(list.getId()).isEqualTo(listId);
			assertThat(list.getName()).isEqualTo("List");
			assertThat(list.getTasks()).containsExactly(new TaskDto(taskId, "Task", "Task description"));
		});
		assertThat(taskListRepository.findById(listId).orElseThrow().getProject().getId())
				.isEqualTo(projectId);
		assertThat(taskRepository.findById(taskId).orElseThrow().getTaskList().getId())
				.isEqualTo(listId);

		projectRepository.delete(reloaded);
		entityManager.flush();
		entityManager.clear();

		assertThat(projectRepository.findById(projectId)).isEmpty();
		assertThat(taskListRepository.findById(listId)).isEmpty();
		assertThat(taskRepository.findById(taskId)).isEmpty();
	}

}
