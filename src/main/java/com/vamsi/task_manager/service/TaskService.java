package com.vamsi.task_manager.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vamsi.task_manager.model.Task;
import com.vamsi.task_manager.repository.TaskRepository;

@Service
public class TaskService {

	private static final String CLAUDE_MODEL = "claude-haiku-4-5-20251001";
	private static final String CLAUDE_API_URL = "https://api.anthropic.com/v1/messages";

	@Value("${anthropic.api.key}")
	private String apiKey;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private RestClient restClient;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public Task getTaskById(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task not found"));
	}

	public Task updateTask(Long id, Task task) {
		validateTask(task);

		Task existingTask = taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task not found"));

		existingTask.setTitle(task.getTitle());
		existingTask.setDescription(task.getDescription());
		existingTask.setDueDate(task.getDueDate());
		existingTask.setPriority(task.getPriority());
		existingTask.setStatus(task.getStatus());

		return taskRepository.save(existingTask);
	}

	public void deleteTask(Long id) {
		if (!taskRepository.existsById(id)) {
			throw new RuntimeException("Id not found");
		}
		taskRepository.deleteById(id);
	}

	public Task createTask(Task task) {
		validateTask(task);
		return taskRepository.save(task);
	}

	public Map<String, String> summarizeTask(Long id) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task not found"));

		String description = task.getDescription() != null ? task.getDescription() : "";
		String prompt = String.format(
				"Summarize this task in 2-3 sentences: title: %s, description: %s, priority: %s, status: %s, dueDate: %s",
				task.getTitle(), description, task.getPriority(), task.getStatus(), task.getDueDate());

		Map<String, Object> requestBody = Map.of(
				"model", CLAUDE_MODEL,
				"max_tokens", 1024,
				"messages", List.of(Map.of("role", "user", "content", prompt)));

		String responseBody = restClient.post()
				.uri(CLAUDE_API_URL)
				.header("x-api-key", apiKey)
				.header("anthropic-version", "2023-06-01")
				.contentType(MediaType.APPLICATION_JSON)
				.body(requestBody)
				.retrieve()
				.body(String.class);

		String responseText;
		try {
			JsonNode response = objectMapper.readTree(responseBody);
			responseText = response.get("content").get(0).get("text").asText().trim();
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse Claude API response", e);
		}
		if (responseText.startsWith("```")) {
			responseText = responseText.replaceAll("^```(?:json)?\\s*", "").replaceAll("\\s*```$", "").trim();
		}

		String summary;
		try {
			JsonNode parsed = objectMapper.readTree(responseText);
			summary = parsed.has("summary") ? parsed.get("summary").asText() : responseText;
		} catch (Exception e) {
			summary = responseText;
		}

		return Map.of("summary", summary);
	}

	private void validateTask(Task task) {
		if (task.getTitle() == null || task.getTitle().isBlank()
				|| task.getDueDate() == null
				|| task.getPriority() == null
				|| task.getStatus() == null) {
			throw new IllegalArgumentException("Invalid task data");
		}
	}
}
