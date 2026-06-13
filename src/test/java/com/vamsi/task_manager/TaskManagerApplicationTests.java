package com.vamsi.task_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import com.vamsi.task_manager.model.Priority;
import com.vamsi.task_manager.model.Status;
import com.vamsi.task_manager.model.Task;
import com.vamsi.task_manager.repository.TaskRepository;
import com.vamsi.task_manager.service.TaskService;

class TaskManagerApplicationTests {

	private static Task buildSampleTask() {
		Task task = new Task();
		task.setTitle("Sample Task");
		task.setDescription("Sample description");
		task.setDueDate(LocalDate.of(2026, 6, 15));
		task.setPriority(Priority.MEDIUM);
		task.setStatus(Status.TODO);
		return task;
	}

	private static Task buildSampleTaskWithId(Long id) {
		Task task = buildSampleTask();
		task.setId(id);
		return task;
	}

	private static String sampleTaskJson() {
		return """
				{
				  "title": "Sample Task",
				  "description": "Sample description",
				  "dueDate": "2026-06-15",
				  "priority": "MEDIUM",
				  "status": "TODO"
				}
				""";
	}

	@Nested
	@DisplayName("TaskService Unit Tests")
	@ExtendWith(MockitoExtension.class)
	class TaskServiceUnitTests {

		@Mock
		private TaskRepository taskRepository;

		@Mock
		private RestClient restClient;

		@Mock
		private RestClient.RequestBodyUriSpec requestBodyUriSpec;

		@Mock
		private RestClient.RequestBodySpec requestBodySpec;

		@Mock
		private RestClient.ResponseSpec responseSpec;

		@InjectMocks
		private TaskService taskService;

		private Task sampleTask;

		@BeforeEach
		void setUp() {
			sampleTask = buildSampleTaskWithId(1L);
		}

		@Test
		void getAllTasks_returnsListFromRepository() {
			List<Task> tasks = Arrays.asList(
					buildSampleTaskWithId(1L),
					buildSampleTaskWithId(2L));
			when(taskRepository.findAll()).thenReturn(tasks);

			List<Task> result = taskService.getAllTasks();

			assertEquals(2, result.size());
			assertEquals("Sample Task", result.get(0).getTitle());
			verify(taskRepository).findAll();
		}

		@Test
		void getTaskById_returnsTaskWhenExists() {
			when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

			Task result = taskService.getTaskById(1L);

			assertNotNull(result);
			assertEquals(1L, result.getId());
			assertEquals("Sample Task", result.getTitle());
			verify(taskRepository).findById(1L);
		}

		@Test
		void getTaskById_throwsWhenNotFound() {
			when(taskRepository.findById(99L)).thenReturn(Optional.empty());

			assertThrows(RuntimeException.class, () -> taskService.getTaskById(99L));
			verify(taskRepository).findById(99L);
		}

		@Test
		void createTask_savesAndReturnsTask() {
			Task newTask = buildSampleTask();
			Task savedTask = buildSampleTaskWithId(1L);
			when(taskRepository.save(newTask)).thenReturn(savedTask);

			Task result = taskService.createTask(newTask);

			assertNotNull(result);
			assertEquals(1L, result.getId());
			assertEquals("Sample Task", result.getTitle());
			verify(taskRepository).save(newTask);
		}

		@Test
		void updateTask_updatesExistingTaskAndReturnsSavedTask() {
			Task updatedData = buildSampleTask();
			updatedData.setTitle("Updated Task");
			updatedData.setDescription("Updated description");
			updatedData.setDueDate(LocalDate.of(2026, 7, 1));
			updatedData.setPriority(Priority.HIGH);
			updatedData.setStatus(Status.IN_PROGRESS);

			Task existingTask = buildSampleTaskWithId(1L);
			when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
			when(taskRepository.save(existingTask)).thenAnswer(invocation -> invocation.getArgument(0));

			Task result = taskService.updateTask(1L, updatedData);

			assertEquals("Updated Task", result.getTitle());
			assertEquals("Updated description", result.getDescription());
			assertEquals(LocalDate.of(2026, 7, 1), result.getDueDate());
			assertEquals(Priority.HIGH, result.getPriority());
			assertEquals(Status.IN_PROGRESS, result.getStatus());
			verify(taskRepository).findById(1L);
			verify(taskRepository).save(existingTask);
		}

		@Test
		void deleteTask_callsDeleteByIdWhenTaskExists() {
			when(taskRepository.existsById(1L)).thenReturn(true);

			taskService.deleteTask(1L);

			verify(taskRepository).existsById(1L);
			verify(taskRepository).deleteById(1L);
		}

		@Test
		void deleteTask_throwsWhenIdNotFound() {
			when(taskRepository.existsById(99L)).thenReturn(false);

			assertThrows(RuntimeException.class, () -> taskService.deleteTask(99L));
			verify(taskRepository).existsById(99L);
			verify(taskRepository, never()).deleteById(99L);
		}

		@Test
		void summarizeTask_returnsSummaryMap() {
			ReflectionTestUtils.setField(taskService, "apiKey", "test-api-key");

			when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
			when(restClient.post()).thenReturn(requestBodyUriSpec);
			when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
			when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
			when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
			doReturn(requestBodySpec).when(requestBodySpec).body(any(Object.class));
			when(requestBodySpec.retrieve()).thenReturn(responseSpec);

			String claudeApiResponse = """
					{
					  "content": [
					    {
					      "type": "text",
					      "text": "This task involves completing sample work by the due date with medium priority."
					    }
					  ]
					}
					""";
			when(responseSpec.body(String.class)).thenReturn(claudeApiResponse);

			Map<String, String> result = taskService.summarizeTask(1L);

			assertNotNull(result);
			assertTrue(result.containsKey("summary"));
			assertNotNull(result.get("summary"));
			assertEquals("This task involves completing sample work by the due date with medium priority.",
					result.get("summary"));
			verify(taskRepository).findById(1L);
			verify(restClient).post();
			verify(requestBodyUriSpec).uri(anyString());
			verify(requestBodySpec).retrieve();
			verify(responseSpec).body(String.class);
		}
	}

	@Nested
	@DisplayName("REST API Integration Tests")
	@SpringBootTest
	@AutoConfigureMockMvc
	class RestApiIntegrationTests {

		@Autowired
		private MockMvc mockMvc;

		@Autowired
		private TaskRepository taskRepository;

		@BeforeEach
		void cleanDatabase() {
			taskRepository.deleteAll();
		}

		@Test
		void postTasks_createsTaskSuccessfully() throws Exception {
			mockMvc.perform(post("/tasks")
					.contentType(MediaType.APPLICATION_JSON)
					.content(sampleTaskJson()))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").isNumber())
					.andExpect(jsonPath("$.title").value("Sample Task"))
					.andExpect(jsonPath("$.description").value("Sample description"))
					.andExpect(jsonPath("$.dueDate").value("2026-06-15"))
					.andExpect(jsonPath("$.priority").value("MEDIUM"))
					.andExpect(jsonPath("$.status").value("TODO"));

			assertEquals(1, taskRepository.count());
		}

		@Test
		void getTasks_returnsListWithOkStatus() throws Exception {
			Task task = buildSampleTask();
			taskRepository.save(task);

			mockMvc.perform(get("/tasks"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$").isArray())
					.andExpect(jsonPath("$[0].title").value("Sample Task"))
					.andExpect(jsonPath("$[0].priority").value("MEDIUM"))
					.andExpect(jsonPath("$[0].status").value("TODO"));
		}

		@Test
		void getTaskById_returnsCorrectTask() throws Exception {
			Task saved = taskRepository.save(buildSampleTask());

			mockMvc.perform(get("/tasks/" + saved.getId()))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(saved.getId()))
					.andExpect(jsonPath("$.title").value("Sample Task"))
					.andExpect(jsonPath("$.description").value("Sample description"))
					.andExpect(jsonPath("$.dueDate").value("2026-06-15"))
					.andExpect(jsonPath("$.priority").value("MEDIUM"))
					.andExpect(jsonPath("$.status").value("TODO"));
		}

		@Test
		void putTaskById_updatesExistingTask() throws Exception {
			Task saved = taskRepository.save(buildSampleTask());

			String updatedJson = """
					{
					  "title": "Updated Task",
					  "description": "Updated description",
					  "dueDate": "2026-07-01",
					  "priority": "HIGH",
					  "status": "IN_PROGRESS"
					}
					""";

			mockMvc.perform(put("/tasks/" + saved.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(updatedJson))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(saved.getId()))
					.andExpect(jsonPath("$.title").value("Updated Task"))
					.andExpect(jsonPath("$.description").value("Updated description"))
					.andExpect(jsonPath("$.dueDate").value("2026-07-01"))
					.andExpect(jsonPath("$.priority").value("HIGH"))
					.andExpect(jsonPath("$.status").value("IN_PROGRESS"));
		}

		@Test
		void deleteTaskById_deletesExistingTask() throws Exception {
			Task saved = taskRepository.save(buildSampleTask());

			mockMvc.perform(delete("/tasks/" + saved.getId()))
					.andExpect(status().isOk());

			assertEquals(0, taskRepository.count());
		}
	}

	@Nested
	@DisplayName("AI Summary Integration Tests")
	@SpringBootTest
	@AutoConfigureMockMvc
	class SummaryIntegrationTests {

		@Autowired
		private MockMvc mockMvc;

		@MockitoBean
		private TaskService taskService;

		@Test
		void postSummarize_returnsSummary() throws Exception {
			when(taskService.summarizeTask(1L)).thenReturn(Map.of(
					"summary", "This task involves completing sample work by the due date with medium priority."));

			mockMvc.perform(post("/tasks/1/summarize"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.summary").value(
							"This task involves completing sample work by the due date with medium priority."));

			verify(taskService).summarizeTask(1L);
		}
	}
}
