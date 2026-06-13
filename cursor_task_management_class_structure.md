# Task management class structure
_Exported on 6/12/2026 at 18:39:31 EDT from Cursor (3.7.27)_

---

**User**

Build a Task.java class, which can be found in the through the pathway src/main/java/com/vamsi/task_manager where this class will be responsible for having a private Long id, private String title, private String description, private LocalDate dueDate, private Priority priority, which is an enum class so create a Priority.java class accordingly with the options LOW, MEDIUM, and HIGH, private Status status, which is an enum class so create a Status.java class accordingly with the options TODO, IN_PROGRESS, and DONE. All of these should have a setter and getter method as they are all private variables. Utilize @Data from Lombok to create all the setter and getter methods. Use JPA annotations for Task.java add @Entity on the class, add @Id on the id field, add @GeneratedValue(strategy = GenerationType.IDENTITY) on the id field, add @Column(nullable = false) on all fields except description field, and add @Column(nullable = true) on description field.

---

**Cursor**

Exploring the project structure and conventions before creating the Task, Priority, and Status classes.


Creating the three Java classes with the specified fields, Lombok annotations, and JPA mappings.


Created the three classes under `src/main/java/com/vamsi/task_manager/`:

**`Priority.java`** — enum with `LOW`, `MEDIUM`, and `HIGH`

**`Status.java`** — enum with `TODO`, `IN_PROGRESS`, and `DONE`

**`Task.java`** — JPA entity with:
- `@Data` from Lombok (generates getters, setters, and other boilerplate)
- `@Entity` on the class
- `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)` on `id`
- `@Column(nullable = false)` on `title`, `dueDate`, `priority`, and `status`
- `@Column(nullable = true)` on `description`

The project compiles successfully with Maven.

---

**User**

Within the pathway src/main/java/com/vamsi/task_manager add a folder called model where I want you to put Task.java, Priority.java, and Status.java within it for organization sake

---

**Cursor**

Moving the entity classes into a `model` package and updating references across the project.


Moved all three classes into `src/main/java/com/vamsi/task_manager/model/` and updated their package to `com.vamsi.task_manager.model`:

- `model/Task.java`
- `model/Priority.java`
- `model/Status.java`

The original files in the parent `task_manager` package were removed. The project still compiles successfully, and Spring Boot will continue to pick up the `Task` entity since it lives under the main application package.

---

**User**

Update applications.properites with spring.datasource.url=jdbc:h2:mem:taskdb spring.datasource.driver-class-name=org.h2.Driver spring.h2.console.enabled=true spring.jpa.hibernate.ddl-auto=update

---

**Cursor**

Updated `src/main/resources/application.properties` with the H2 and JPA settings:

- `spring.datasource.url=jdbc:h2:mem:taskdb`
- `spring.datasource.driver-class-name=org.h2.Driver`
- `spring.h2.console.enabled=true`
- `spring.jpa.hibernate.ddl-auto=update`

The existing `spring.application.name=task-manager` entry was kept.

---

**User**

Build a TaskRepository.java interface, which could be found through the pathway src/main/java/com/vamsi/task_manager within a folder called repository. This class will need only one line, which is public interface TaskRepository extends JpaRepository <Task, Long> {}


---

**Cursor**

Created `TaskRepository.java` at `src/main/java/com/vamsi/task_manager/repository/TaskRepository.java`:

```java
public interface TaskRepository extends JpaRepository<Task, Long> {
}
```

It imports `Task` from the `model` package and `JpaRepository` from Spring Data JPA. The project compiles successfully.

---

**User**

Build a TaskService.java class, which could be found in the pathway src/main/java/com/vamsi/task_manager within a folder called service. Use @Autowired to inject private TaskRepository taskRepository. I want you to write the methods: getAllTasks() return List<Task>, getTaskById(Long id) returns Task, updateTask(Long id, Task task) returns Task, deleteTask(Long id) returns void, and createTask(Task task) return Task. For getAllTasks() call the findAll() method. For getTaskById(Long id) call the findById() method and return a RuntimeException with message ‘Task not found’ if it does not exist. For updateTask(Long id, Task task) return Task check if all values such as title, dueDate, priority, and status are not null, empty, or invalid data types if they are then return IllegalArgumentException with message ‘Invalid task data’ and if all the values are valid then use save() method to update each data type with the new Task Object for the respective id being sent through as an argument. For deleteTask(Long id) returns void call the deleteById() method but if the id does not exist return a RuntimeException with message ‘Id not found’. For createTask(Task task) return Task check if all values such as title, dueDate, priority, and status are not null, empty, or invalid data types if they are then return IllegalArgumentException with message ‘Invalid task data’ and if all the values are valid then use save() method to update the db. 

---

**Cursor**

Checking existing project structure and conventions before creating `TaskService`.


Created `TaskService.java` at `src/main/java/com/vamsi/task_manager/service/TaskService.java` with the requested methods:

| Method | Behavior |
|--------|----------|
| `getAllTasks()` | Calls `taskRepository.findAll()` |
| `getTaskById(Long id)` | Calls `findById()`; throws `RuntimeException("Task not found")` if missing |
| `updateTask(Long id, Task task)` | Validates title, dueDate, priority, and status; throws `IllegalArgumentException("Invalid task data")` if invalid; loads the existing task, updates its fields, and saves |
| `deleteTask(Long id)` | Throws `RuntimeException("Id not found")` if the id does not exist; otherwise calls `deleteById()` |
| `createTask(Task task)` | Same validation as update; saves the new task via `save()` |

Validation checks that `title` is not null or blank, and that `dueDate`, `priority`, and `status` are not null. `@Service` was added so Spring can register the class as a bean (required for `@Autowired` to work). The project compiles successfully.

---

**User**

Build a TaskController.java class, which could be found in the pathway src/main/java/com/vamsi/task_manager within a folder called controller. Use @Autowired to inject private TaskService taskService. I need a POST HTTP request with the path /tasks called createTask, which will call the TaskService method createTask(Task task) and return its result. I need a GET HTTP request with the path /tasks/{id} called getTaskById, which will call the TaskService method getTaskById(Long id) and return its result. I need a Get HTTP request with path /tasks/ called getAllTasks(), which will call the TaskService method getAllTasks() and return its result. I need a PUT HTTP request with the path /tasks/{id} called updateTask, which will call the TaskService method updateTask(Long id, Task task) and return its result. I need a DELETE HTTP request with the path /tasks/{id} called deleteTask, which will call the TaskService method deleteTask(Long id) and return its result.

---

**Cursor**

Created `TaskController.java` at `src/main/java/com/vamsi/task_manager/controller/TaskController.java` with these endpoints:

| HTTP Method | Path | Method | Service Call |
|-------------|------|--------|--------------|
| POST | `/tasks` | `createTask` | `taskService.createTask(task)` |
| GET | `/tasks/{id}` | `getTaskById` | `taskService.getTaskById(id)` |
| GET | `/tasks/` | `getAllTasks` | `taskService.getAllTasks()` |
| PUT | `/tasks/{id}` | `updateTask` | `taskService.updateTask(id, task)` |
| DELETE | `/tasks/{id}` | `deleteTask` | `taskService.deleteTask(id)` |

The controller uses `@RestController`, `@RequestMapping("/tasks")`, and `@Autowired` for `TaskService`. POST and PUT accept a JSON body via `@RequestBody Task task`, and path variables use `@PathVariable Long id`. The project compiles successfully.

---

**User**

@GetMapping("/") should just be @GetMapping because the base path is "/tasks" so change that 

---

**Cursor**

Updated `getAllTasks` to use `@GetMapping`, so it maps to `/tasks` via the class-level `@RequestMapping("/tasks")`.

---

**User**

In TaskService.java add a new method breakdownTask(Long id) that: fetches the task by id from the database throwing RuntimeException 'Task not found' if it doesn't exist, sends the task title and description to the Claude API using model claude-haiku-4-5-20251001 with the prompt 'Break down this task into subtasks and return only a JSON array of objects with fields: title (String), description (String), priority (LOW/MEDIUM/HIGH), status (TODO), dueDate (yyyy-MM-dd format, schedule them within the next 2 weeks). Task: {title} - {description}', parse the JSON response using Jackson into a list of Task objects, call taskRepository.save() for each task, and return the list of saved tasks. Read the API key from the environment variable ANTHROPIC_API_KEY. Lastly, Add POST /tasks/{id}/breakdown to TaskController that calls taskService.breakdownTask(Long id) and returns the result.


---

**Cursor**

Implementing `breakdownTask` with Claude API integration. Checking the project setup and Anthropic API requirements.


Implementing `breakdownTask` with RestClient and Jackson (included via Spring Boot), then adding the controller endpoint.


Jackson may use different packages in Spring Boot 4 — checking dependencies.


Updating imports to Jackson 3 packages used by Spring Boot 4.


Added `breakdownTask` to `TaskService` and the matching controller endpoint.

**`TaskService.breakdownTask(Long id)`**
1. Loads the task by id; throws `RuntimeException("Task not found")` if missing
2. Reads `ANTHROPIC_API_KEY` from the environment
3. Calls the Claude API (`claude-haiku-4-5-20251001`) with the specified prompt using the task title and description
4. Parses the JSON array response with Jackson into `List<Task>` (strips markdown code fences if present)
5. Saves each subtask via `taskRepository.save()` and returns the saved list

**`TaskController`**
- `POST /tasks/{id}/breakdown` → calls `taskService.breakdownTask(id)` and returns `List<Task>`

Set your API key before calling the endpoint:

```bash
export ANTHROPIC_API_KEY=your-key-here
```

---

**User**

Fix the Jackson imports in TaskService.java replace tools.jackson with com.fasterxml.jackson


---

**Cursor**

Spring Boot 4 ships Jackson 3 (`tools.jackson`); adding a `com.fasterxml.jackson` dependency and adjusting `ObjectMapper` usage so it compiles.


Adding the `com.fasterxml.jackson` dependency and using a local `ObjectMapper` instance, since Spring Boot 4 auto-configures Jackson 3.


Updated `TaskService.java` to use `com.fasterxml.jackson` imports:

- `com.fasterxml.jackson.core.type.TypeReference`
- `com.fasterxml.jackson.databind.JsonNode`
- `com.fasterxml.jackson.databind.ObjectMapper`

Spring Boot 4 ships Jackson 3 (`tools.jackson`), so `com.fasterxml.jackson.core:jackson-databind` was added to `pom.xml` to make those classes available at compile time.

`ObjectMapper` is now created locally (`new ObjectMapper()`) instead of `@Autowired`, because Spring Boot 4 auto-configures the Jackson 3 `ObjectMapper`, which is a different type.

The project compiles successfully.

---

**User**

Build a single HTML file called index.html, which should be placed in the pathway src/main/resources/static/. This file will serve as the frontend for a Spring Boot Task Manager REST API running on localhost:8080. Use only plain HTML, CSS, and vanilla JavaScript. Do not use React, Vue, Bootstrap, jQuery, or any other frontend framework or library. Use the fetch() API for all HTTP requests.

The page should automatically load all tasks when it opens by sending a GET HTTP request to the path /tasks. Display all returned tasks in an HTML table showing the fields id, title, description, dueDate, priority, and status. The task list should be refreshable through a reusable JavaScript function so it can be called after creating or deleting tasks.

Add a form for creating a new task. The form should contain a text input for title, a text input for description, a date picker for dueDate, a dropdown for priority with options LOW, MEDIUM, and HIGH, and a dropdown for status with options TODO, IN_PROGRESS, and DONE. When the form is submitted, send a POST HTTP request to /tasks with a JSON request body containing the entered task information. After the task is successfully created, clear the form and automatically refresh the task table.

For every task displayed in the table, add a Delete button. When the button is clicked, send a DELETE HTTP request to /tasks/{id} using the task's id. After the task is successfully deleted, automatically refresh the task table so the deleted task no longer appears.

Add an AI Breakdown section containing a text input where the user can enter a task id and a button labeled Generate Breakdown. When the button is clicked, send a POST HTTP request to /tasks/{id}/breakdown using the entered id. Display the response from the API on the page as formatted JSON using a preformatted text element so the generated subtasks are easy to read.

Style the page using CSS within the same HTML file. Include sections for viewing tasks, creating tasks, and generating AI breakdowns. Make the layout clean and easy to use. All HTML, CSS, and JavaScript should be contained within the single index.html file.

---

**Cursor**

Creating a single-page `index.html` with HTML, CSS, and vanilla JavaScript for the task manager frontend.


Created `src/main/resources/static/index.html` as a single-file frontend with three sections:

**All Tasks**
- Loads tasks via `GET /tasks` on page open
- Displays them in a table (id, title, description, dueDate, priority, status)
- Refresh button and reusable `loadTasks()` function
- Delete button per row calls `DELETE /tasks/{id}` and refreshes the table

**Create New Task**
- Form with title, description, date picker, priority dropdown, and status dropdown
- Submits via `POST /tasks` with JSON body
- Clears the form and refreshes the table on success

**AI Breakdown**
- Task ID input and "Generate Breakdown" button
- Calls `POST /tasks/{id}/breakdown`
- Shows the response as formatted JSON in a `<pre>` element
- Refreshes the task table after a successful breakdown (new subtasks appear in the list)

Start the Spring Boot app and open `http://localhost:8080/` to use it. All requests use relative paths, so they work on the same origin as the API.

---

**User**

Write all tests for the Spring Boot Task Manager application in the file TaskManagerApplicationTests.java, which can be found in the pathway src/test/java/com/vamsi/task_manager/. Create a section for TaskService unit tests using JUnit 5, Mockito, and @ExtendWith(MockitoExtension.class). Mock the TaskRepository dependency using Mockito and inject it into TaskService. Write a test for getAllTasks() that verifies a list of tasks is returned from the repository. Write a test for getTaskById() that verifies a task is returned when the task exists in the repository. Write a test for createTask() that verifies taskRepository.save() is called and that the created task is returned. Write a test for updateTask() that verifies an existing task is updated with the new values, saved to the repository, and returned. Write a test for deleteTask() that verifies taskRepository.deleteById() is called with the correct id. Create integration tests using @SpringBootTest and @AutoConfigureMockMvc. Autowire MockMvc and test the REST API endpoints end to end. Write a test for POST /tasks that sends a valid Task JSON object, verifies the task is created successfully, and returns HTTP 200. Write a test for GET /tasks that verifies a list of tasks is returned with HTTP 200. Write a test for GET /tasks/{id} that creates a task, retrieves it by id, and verifies the returned data is correct. Write a test for PUT /tasks/{id} that updates an existing task and verifies the updated values are returned. Write a test for DELETE /tasks/{id} that deletes an existing task and verifies the request returns HTTP 200. Create a test for the AI breakdown endpoint. Mock the TaskService breakdownTask(Long id) method to return a sample list of Task objects. Send a POST request to /tasks/{id}/breakdown using MockMvc. Verify the endpoint returns HTTP 200 and that the response contains the expected list of generated tasks. Use proper assertions for all tests and verify Mockito interactions where appropriate. Include all required imports, annotations, setup methods, and test data. The final TaskManagerApplicationTests.java file should compile successfully and all tests should pass when running: ./mvnw test Ensure the tests are written to match the existing package structure, controller endpoints, service methods, repository methods, entity fields, and JSON formats already implemented in the project.

---

**Cursor**

Reviewing the codebase and existing test file to write matching unit and integration tests.


Writing comprehensive unit and integration tests, then running the test suite.


All tests are in `TaskManagerApplicationTests.java` and pass with `./mvnw test` (13 tests, 0 failures).

### TaskService Unit Tests (`@ExtendWith(MockitoExtension.class)`)
- **`getAllTasks_returnsListFromRepository`** — verifies `findAll()` is called and returns the task list
- **`getTaskById_returnsTaskWhenExists`** — verifies a task is returned when found
- **`getTaskById_throwsWhenNotFound`** — verifies `RuntimeException` when missing
- **`createTask_savesAndReturnsTask`** — verifies `save()` is called and the created task is returned
- **`updateTask_updatesExistingTaskAndReturnsSavedTask`** — verifies fields are updated, saved, and returned
- **`deleteTask_callsDeleteByIdWhenTaskExists`** — verifies `deleteById(1L)` is called
- **`deleteTask_throwsWhenIdNotFound`** — verifies `deleteById` is never called when id is missing

### REST API Integration Tests (`@SpringBootTest`, `@AutoConfigureMockMvc`)
- **`POST /tasks`** — creates a task with valid JSON, expects HTTP 200 and correct response fields
- **`GET /tasks`** — returns task list with HTTP 200
- **`GET /tasks/{id}`** — creates then retrieves a task and verifies all fields
- **`PUT /tasks/{id}`** — updates a task and verifies updated values
- **`DELETE /tasks/{id}`** — deletes a task, expects HTTP 200, confirms DB is empty

### AI Breakdown Integration Tests
- **`POST /tasks/{id}/breakdown`** — mocks `TaskService.breakdownTask()` with sample subtasks, expects HTTP 200 and correct JSON array response; verifies Mockito interaction

Shared helpers (`buildSampleTask`, `sampleTaskJson`) keep test data consistent across unit and integration tests. Spring Boot 4’s `@MockitoBean` is used instead of the deprecated `@MockBean` for the breakdown endpoint mock.

---

**User**

In TaskService.java, remove the lines that read the API key using System.getenv('ANTHROPIC_API_KEY') and the null check for it. Instead add this field at the top of the class: @Value("${anthropic.api.key}") private String apiKey; and add the import org.springframework.beans.factory.annotation.Value.

---

**Cursor**

Updated `TaskService.java`:

- Added `@

Value("${anthropic.api.key}") private String apiKey;` at the top of the class
- Added the `org.springframework.beans.factory.annotation.Value` import
- Removed the `System.getenv("ANTHROPIC_API_KEY")` call and its null check

`breakdownTask` now uses the injected `apiKey` field for the `x-api-key` header. The key is read from `anthropic.api.key` in `application.properties`.

---

**User**

In src/main/resources/static/index.html fix the API base URL to make sure all fetch calls use http://localhost:8080 as the base. Also create a HomeController.java in com.vamsi.task_manager.controller that redirects GET / to /index.html so that localhost:8080 automatically opens the frontend.

---

**Cursor**

Two updates are in place:

**`index.html`** — `API_BASE` is now `http://localhost:8080/tasks`, so all `fetch()` calls hit the full localhost URL.

**`HomeController.java`** — Added in `com.vamsi.task_manager.controller` with a `GET /` mapping that redirects to `/index.html`, so opening `http://localhost:8080` loads the frontend automatically.

---

**User**

In index.html add an Update button for each task row in the table. When clicked, populate a pre-filled edit form below the task table with that task's current id, title, description, dueDate, priority, and status values. When the form is submitted send a PUT request to /tasks/{id} with the updated JSON body then refresh the task list and hide the form. 

---

**Cursor**

Adding an Update button per row and a pre-filled edit form below the task table.


Updated `index.html` with task editing:

**Update button** — Each table row now has an Update button next to Delete.

**Edit form** — Hidden by default below the task table. Clicking Update fills it with the task’s id (read-only), title, description, dueDate, priority, and status.

**Submit** — Sends `PUT http://localhost:8080/tasks/{id}` with the updated JSON, then refreshes the task list and hides the form.

**Cancel** — Hides the form without saving. The edit form also closes automatically if the task being edited is deleted.

---

**User**

In TaskService.java change the breakdownTask method to instead call the Claude API with a prompt that returns a plain-language summary of the task. The prompt should be: 'Summarize this task in 2-3 sentences: title: {title}, description: {description}, priority: {priority}, status: {status}, dueDate: {dueDate}'. Return the response as a simple JSON object with one field: summary (String). Rename the method to summarizeTask and update TaskController.java to rename the endpoint from POST /tasks/{id}/breakdown to POST /tasks/{id}/summarize and call summarizeTask instead. Update index.html to rename the AI Breakdown section to AI Summary, change the button label to Generate Summary, and display the returned summary text instead of JSON.

---

**Cursor**

Updating the summarize flow across the service, controller, frontend, and tests.


Refactored breakdown into summarize across the stack:

**`TaskService.java`**
- Renamed `breakdownTask` → `summarizeTask`
- Uses the new prompt with title, description, priority, status, and dueDate
- Returns `Map<String, String>` with a `summary` field (parses JSON from Claude if present, otherwise uses the plain-text response)

**`TaskController.java`**
- Endpoint changed from `POST /tasks/{id}/breakdown` → `POST /tasks/{id}/summarize`
- Calls `summarizeTask(id)` and returns the summary object

**`index.html`**
- Section renamed to **AI Summary**
- Button label changed to **Generate Summary**
- Calls `POST /tasks/{id}/summarize`
- Displays `data.summary` as readable text instead of JSON

**Tests** updated to match the new endpoint and response shape. All 13 tests pass with `./mvnw test`.

---

**User**

In TaskService.java replace the RestClient call that returns JsonNode.class with one that returns String.class instead. Then parse the response string manually using objectMapper to extract the text from content[0].text

---

**Cursor**

Updated `summarizeTask` in `TaskService.java`:

- The RestClient call now uses `.body(String.class)` instead of `.body(JsonNode.class)`
- The raw response string is parsed with `objectMapper.readTree(responseBody)` to read `content[0].text`

The project compiles successfully.

---

**User**

In index.html, in the generateSummary function, before sending the fetch request check if the entered id is a valid positive integer. If it is not, display 'Sorry, ID is not valid' in the summary result box and return early without making the API call.

---

**Cursor**

Updated `generateSummary` to validate the ID before calling the API. It checks that the value is a positive integer using `Number.isInteger(numericId) && numericId > 0`. If not, it shows `Sorry, ID is not valid` in the summary result box and returns without sending the request.

---

**User**

I think we completed everything based on these instructions did complete everything except read me and trascript of this chat would you say that this a 100 and we did not miss a single thing and everything works perfectly Enable desktop notifications for Gmail.
   OK  No thanks
Eulerity Agentic Hackathon
Inbox

no-reply@eulerity.com
Attachments
Jun 9, 2026, 9:55 AM (3 days ago)
to me

Hi Vamsi,

Thanks for applying for the Backend Intern position at Eulerity! We're moving your application forward and inviting you to complete a take-home Agentic Hackathon. 

This is a project-based assignment we want you to have fun completing. Please find the README attached. It will instruct you on how to complete this assignment, which tools to use, and what is expected. In the age of AI and agentic driven development, we want to understand a candidates' aptitude for interacting with, and using AI. Not only to work with you, but to work for you. With that in mind, please take up to 3 business days to complete this assignment. It should not take more than 3 to 5 hours of your time, so please prepare accordingly. The attached README also has instructions on how to submit your work. 

We request that you submit through Greenhouse to help us better keep track of your submission.

If you have any questions or need assistance feel free to reach out to angelo.veres@eulerity.com. If you have already submitted through Greenhouse, you do not need to email as well.

Best,


Please submit here:
https://app6.greenhouse.io/tests/7ee3907a17df35845d54f72ad1fbe49f?utm_medium=email&utm_source=TakeHomeTest

 One attachment
  •  Scanned by Gmail
# Backend Engineering Intern — Take-Home Assessment

Welcome, and thank you for your interest in joining our team. This assessment is designed to take **3–5 hours**. We are not looking for perfection — we are looking for how you think, how you work, and how you collaborate with AI tools.

---

## Overview

You will build a small Java 17 REST API for a **personal task manager**, using an AI agentic model as your primary development tool. When you submit, you will include both your code and a transcript of your conversation with the model.

The transcript is as important as the code. We want to see how you direct the model, evaluate its output, and recover when things go wrong.

---

## Requirements

### Technical stack
- **Java 17**
- **Spring Boot** (latest stable)
- **Maven or Gradle** (your choice)
- **H2 in-memory database** (no external database setup required)

### Your API must include

**1. Task CRUD endpoints**

A `Task` has at minimum:
- `id` (auto-generated)
- `title` (string, required)
- `description` (string, optional)
- `dueDate` (date)
- `priority` (LOW / MEDIUM / HIGH)
- `status` (TODO / IN_PROGRESS / DONE)

Implement the following endpoints:

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/tasks` | Create a new task |
| `GET` | `/tasks` | List all tasks |
| `GET` | `/tasks/{id}` | Get a single task |
| `PUT` | `/tasks/{id}` | Update a task |
| `DELETE` | `/tasks/{id}` | Delete a task |

**2. At least one AI-powered endpoint**

Integrate a call to an AI model (Claude, GPT-4, Gemini, or similar) within your API. You may choose your approach — here are some ideas to get you started, but you are welcome to come up with your own:

- `POST /tasks/suggest` — accepts a plain-language description (e.g., `"remind me to submit the quarterly report before Friday"`) and returns a structured task object
- `POST /tasks/{id}/summarize` — returns a plain-language summary or explanation of a task
- `POST /tasks/{id}/breakdown` — returns a suggested list of subtasks for a complex task

Your AI-powered endpoint should return well-structured JSON. It does not need to persist anything — a stateless call to the model is fine.

**3. A simple UI**

Include a minimal frontend that allows a reviewer to interact with your API without using a REST client. It does not need to be polished or feature-complete — a basic HTML page is fine. It should support:

- Viewing the list of tasks
- Creating a new task
- Triggering your AI-powered endpoint and displaying the result

Styling and aesthetics are not evaluated. The UI exists purely to make your project easier to explore.

**4. Basic tests**

Include a test suite that covers the core behavior of your API. At minimum:

- At least one unit test per service-layer method (happy path)
- At least one integration test that starts the Spring context and exercises each CRUD endpoint end-to-end
- At least one test for your AI-powered endpoint (mocking the external model call is fine and expected)

Tests must pass when running:

```bash
./mvnw test
```

or

```bash
./gradlew test
```

**4. A working README**

Your README (separate from this document) must allow a reviewer to clone your repo, run a single command, and have the API running locally. Include:
- Setup instructions
- How to run the project
- A description of your AI-powered endpoint and example request/response

### Build requirement

Your project must build and start with a single command, for example:

```bash
./mvnw spring-boot:run
```

or

```bash
./gradlew bootRun
```

We will run this command cold, with no prior setup beyond having Java 17 and internet access.

---

## What You Do Not Need to Build

- Authentication or authorization
- A production-grade database
- High test coverage beyond the basics described above
- Deployment configuration

---

## Using AI

You must use an AI agentic model as your **primary builder** for this project — not just as an assistant or autocomplete. This is the most important skill we are evaluating.

What we mean by agentic use: you should be directing the model to take on substantial, multi-step work — asking it to scaffold the project, implement entire features, debug its own output, and iterate based on your feedback. Think of yourself as the architect and reviewer, and the model as the engineer doing the building. Your job is to give it clear direction, evaluate what it produces, and guide it toward a working result.

Tools that support this style of working well include [Claude](https://claude.ai), [Cursor](https://cursor.sh), [ChatGPT](https://chat.openai.com), and [Gemini](https://gemini.google.com). Any tool that allows multi-turn, context-aware conversations with a model is acceptable.

What we are looking for in your transcript: evidence that you used the model to drive real construction work, not just to answer one-off questions. A strong transcript will show the model generating code, you reviewing and redirecting it, the model fixing problems, and so on — a genuine back-and-forth build session.

### Capturing your transcript

Before you start, open a conversation in your AI tool of choice and keep it running for the duration of the project. At submission time, export or copy the full conversation.

- **Claude:** Use [claude.ai](https://claude.ai). At the end of your session, copy the full conversation from the browser or use the export feature if available.
- **ChatGPT:** Use [chat.openai.com](https://chat.openai.com). You can share a conversation link or copy the text.
- **Cursor / Copilot / other tools:** Copy the chat panel contents, or take a sequential export if the tool supports it.

Submit your transcript as a plain text or Markdown file alongside your code. If your tool does not support export, a copy-paste of the full conversation is fine.

---

## Submission

Please submit through the Greenhouse link that was part of the email you received this README from.

**Deadline:** 3 business days from receiving this README.

---

## What We Are Looking For

The most important thing we evaluate is **how you use the AI model to build**. We want to see you directing it through real construction work — not just asking it questions or using it for autocomplete. Your transcript is the primary window into this, and we read it carefully.

Beyond that, we care about: whether your project builds and runs correctly, whether your Java is clean and well-structured, whether your tests are meaningful, and whether your README gives us everything we need to run it cold.

The code matters — but a candidate who shows strong, intentional agentic use with a rougher project will always be more interesting to us than one with polished code and a thin transcript.

---

## A Note on What "Good" Looks Like

A strong submission is not necessarily one with the most features or the cleanest code. We are looking for candidates who:

- Direct the AI with clear, thoughtful prompts
- Push back or ask for clarification when the model produces something unclear or wrong
- Understand the code they submit — even if the model helped write it
- Make deliberate choices and can explain them in their README

A transcript showing one debugging session where you identified a flaw in the model's suggestion, corrected it, and explained why — is worth more to us than a polished project with no visible thinking.

---

## Questions

If anything in this document is unclear, please reach out to angelo.veres@eulerity.com before starting. We would rather answer a question upfront than have you blocked or heading in the wrong direction.

Please make sure to not include your AI API keys in your submission. We will not use your key even if you forgot to remove it.

Good luck — we look forward to reading your submission.

---

**Cursor**

Auditing the project against the hackathon requirements to give an accurate assessment.


You’re in very good shape on the **code and project**, but I would **not** call it a 100 yet — mainly because two required submission items are still missing, and there are a couple of small gaps to fix before you submit.

## Requirements checklist

| Requirement | Status |
|---|---|
| Java 17 + Spring Boot + Maven + H2 | Done |
| Task model (id, title, description, dueDate, priority, status) | Done |
| CRUD: `POST/GET/PUT/DELETE /tasks`, `GET /tasks/{id}` | Done |
| AI endpoint (`POST /tasks/{id}/summarize`) | Done |
| UI: view tasks, create task, trigger AI endpoint | Done (plus update/delete — nice extras) |
| Unit tests per service method (happy path) | **Mostly done — one gap** |
| Integration tests for all CRUD endpoints | Done |
| AI endpoint test (mocked) | Done |
| `./mvnw test` passes | Done (verified) |
| `./mvnw spring-boot:run` cold start | Likely fine — worth one manual run |
| README | **Not done** |
| AI chat transcript | **Not done** |

## What’s solid

The core assignment is well covered:

- Clean layering: `model` → `repository` → `service` → `controller`
- Full CRUD API with validation
- AI summary endpoint returning structured JSON (`{"summary": "..."}`)
- Frontend at `index.html` with task list, create form, and AI summary
- 13 passing tests with good unit + integration coverage
- `mvnw` wrapper present

For the **technical/build portion**, you’re roughly at **90–95%**.

## Gaps before submission

### 1. README (required)
You still need a project README with:
- Setup instructions
- How to run (`./mvnw spring-boot:run`)
- Description of `POST /tasks/{id}/summarize` with example request/response

Without this, a reviewer can’t run the project cold as instructed.

### 2. Transcript (required)
The assignment treats the transcript as **as important as the code**. Export/copy this Cursor conversation into a `.md` or `.txt` file.

### 3. API key in `application.properties` (critical)
Your `application.properties` currently contains a real Anthropic API key. The README explicitly says:

> *Please make sure to not include your AI API keys in your submission.*

Before submitting:
- Remove the key from the repo
- Use a placeholder like `anthropic.api.key=${ANTHROPIC_API_KEY:}`
- Document in the README that reviewers must set the env var to use summarize

This is a **submission blocker** if left as-is.

### 4. Missing unit test for `summarizeTask`
The spec asks for **at least one unit test per service-layer method**. You have happy-path unit tests for:

- `getAllTasks`
- `getTaskById`
- `createTask`
- `updateTask`
- `deleteTask`

But **not** `summarizeTask`. You do have an integration test with `@MockitoBean TaskService`, which satisfies the “AI endpoint test” requirement, but not the “unit test per service method” one. A mocked RestClient test (or refactoring to inject RestClient) would close this gap.

### 5. Minor polish (not blockers)
- `index.html` hardcodes `http://localhost:8080/tasks`. Works for the default setup; relative `/tasks` would be slightly more robust.
- I haven’t verified a **live** AI summarize call end-to-end in this session — only that tests compile and pass.

## Verdict

| Area | Score |
|---|---|
| Code / architecture | ~95% |
| Tests | ~90% (missing `summarizeTask` unit test) |
| Submission readiness | ~60% (no README, no transcript, API key exposed) |

**Overall: not 100 yet**, but you’re close. The implementation itself is strong; what’s left is mostly **submission packaging and two small fixes**.

## Recommended pre-submit checklist

1. Remove API key from `application.properties`; use env var instead  
2. Add a `summarizeTask` unit test (mock external API)  
3. Write README with run instructions + summarize example  
4. Export this chat as transcript  
5. Run once manually:
   ```bash
   ./mvnw test
   ./mvnw spring-boot:run
   ```
   Then open `http://localhost:8080`, create a task, and test Generate Summary  
6. Submit via Greenhouse

If you want, I can help you knock out the README, remove the API key safely, and add the missing `summarizeTask` unit test next.

---

**User**

In TaskManagerApplicationTests.java add a new unit test in the TaskService unit test section for the summarizeTask(Long id) method. Mock the TaskRepository using Mockito so that findById() returns a sample Task object containing a title and description. Mock the RestClient used by the Claude API call so that it returns a fake API response containing a summary. Call summarizeTask(Long id) and verify that the returned result is a Map containing a key called summary. Also verify that the repository and mocked API client interactions occur as expected. Use assertions to confirm the summary value is present and not null. Create a README.md file in the root directory of the project. Add a Project Overview section describing the Task Manager application and its AI-powered task features. Add a Tech Stack section listing Java 17, Spring Boot 4.1, Maven, H2 Database, and the Anthropic Claude API. Add a Prerequisites section explaining that Java 17 and an Anthropic API key are required before running the application. Add a Setup Instructions section explaining how to clone the repository, configure the ANTHROPIC_API_KEY in application.properties, and start the application. Add a How to Run section showing the command ./mvnw spring-boot:run. Add a How to Run Tests section showing the command ./mvnw test. Add an API Endpoints section containing a table listing all available endpoints including POST /tasks, GET /tasks, GET /tasks/{id}, PUT /tasks/{id}, DELETE /tasks/{id}, and POST /tasks/{id}/summarize with a brief description of each. Add an AI Endpoint section specifically describing POST /tasks/{id}/summarize with an example response in the format: {"summary": "This task involves..."}. Add a Frontend section explaining that the web interface is available at localhost:8080 and supports creating, viewing, updating, deleting tasks, and generating AI summaries.

---

**Cursor**

Adding the `summarizeTask` unit test requires making `RestClient` mockable. Reviewing the service and test setup, then implementing the test and README.


Refactoring `TaskService` to inject `RestClient` so we can mock it in the unit test, then adding the test and README.


Fixing the Mockito stubbing mismatch for `RestClient.body()`.


All requested changes are in place. Summary:

### `summarizeTask` unit test
Added `summarizeTask_returnsSummaryMap` in the `TaskServiceUnitTests` section that:
- Mocks `TaskRepository.findById()` to return a sample task with title and description
- Mocks the full `RestClient` chain to return a fake Claude API JSON response
- Asserts the result is a `Map` with a non-null `summary` key
- Verifies `findById`, `restClient.post()`, and `responseSpec.body(String.class)` were called

To support mocking, `RestClient` is now injected via a new `RestClientConfig` bean instead of being created inside `TaskService`.

### `README.md`
Created at the project root with all requested sections:
- Project Overview
- Tech Stack
- Prerequisites
- Setup Instructions
- How to Run (`./mvnw spring-boot:run`)
- How to Run Tests (`./mvnw test`)
- API Endpoints table
- AI Endpoint (`POST /tasks/{id}/summarize` with example response)
- Frontend

### Security fix
Replaced the real API key in `application.properties` with `YOUR_ANTHROPIC_API_KEY_HERE` so it is safe to submit. Set your real key locally before testing the summarize feature.

All **14 tests pass** with `./mvnw test`.

---

**User**

okay one last time is everything good except for transcript do a check for a 100 Enable desktop notifications for Gmail.
   OK  No thanks
Eulerity Agentic Hackathon
Inbox

no-reply@eulerity.com
Attachments
Jun 9, 2026, 9:55 AM (3 days ago)
to me

Hi Vamsi,

Thanks for applying for the Backend Intern position at Eulerity! We're moving your application forward and inviting you to complete a take-home Agentic Hackathon. 

This is a project-based assignment we want you to have fun completing. Please find the README attached. It will instruct you on how to complete this assignment, which tools to use, and what is expected. In the age of AI and agentic driven development, we want to understand a candidates' aptitude for interacting with, and using AI. Not only to work with you, but to work for you. With that in mind, please take up to 3 business days to complete this assignment. It should not take more than 3 to 5 hours of your time, so please prepare accordingly. The attached README also has instructions on how to submit your work. 

We request that you submit through Greenhouse to help us better keep track of your submission.

If you have any questions or need assistance feel free to reach out to angelo.veres@eulerity.com. If you have already submitted through Greenhouse, you do not need to email as well.

Best,


Please submit here:
https://app6.greenhouse.io/tests/7ee3907a17df35845d54f72ad1fbe49f?utm_medium=email&utm_source=TakeHomeTest

 One attachment
  •  Scanned by Gmail
# Backend Engineering Intern — Take-Home Assessment

Welcome, and thank you for your interest in joining our team. This assessment is designed to take **3–5 hours**. We are not looking for perfection — we are looking for how you think, how you work, and how you collaborate with AI tools.

---

## Overview

You will build a small Java 17 REST API for a **personal task manager**, using an AI agentic model as your primary development tool. When you submit, you will include both your code and a transcript of your conversation with the model.

The transcript is as important as the code. We want to see how you direct the model, evaluate its output, and recover when things go wrong.

---

## Requirements

### Technical stack
- **Java 17**
- **Spring Boot** (latest stable)
- **Maven or Gradle** (your choice)
- **H2 in-memory database** (no external database setup required)

### Your API must include

**1. Task CRUD endpoints**

A `Task` has at minimum:
- `id` (auto-generated)
- `title` (string, required)
- `description` (string, optional)
- `dueDate` (date)
- `priority` (LOW / MEDIUM / HIGH)
- `status` (TODO / IN_PROGRESS / DONE)

Implement the following endpoints:

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/tasks` | Create a new task |
| `GET` | `/tasks` | List all tasks |
| `GET` | `/tasks/{id}` | Get a single task |
| `PUT` | `/tasks/{id}` | Update a task |
| `DELETE` | `/tasks/{id}` | Delete a task |

**2. At least one AI-powered endpoint**

Integrate a call to an AI model (Claude, GPT-4, Gemini, or similar) within your API. You may choose your approach — here are some ideas to get you started, but you are welcome to come up with your own:

- `POST /tasks/suggest` — accepts a plain-language description (e.g., `"remind me to submit the quarterly report before Friday"`) and returns a structured task object
- `POST /tasks/{id}/summarize` — returns a plain-language summary or explanation of a task
- `POST /tasks/{id}/breakdown` — returns a suggested list of subtasks for a complex task

Your AI-powered endpoint should return well-structured JSON. It does not need to persist anything — a stateless call to the model is fine.

**3. A simple UI**

Include a minimal frontend that allows a reviewer to interact with your API without using a REST client. It does not need to be polished or feature-complete — a basic HTML page is fine. It should support:

- Viewing the list of tasks
- Creating a new task
- Triggering your AI-powered endpoint and displaying the result

Styling and aesthetics are not evaluated. The UI exists purely to make your project easier to explore.

**4. Basic tests**

Include a test suite that covers the core behavior of your API. At minimum:

- At least one unit test per service-layer method (happy path)
- At least one integration test that starts the Spring context and exercises each CRUD endpoint end-to-end
- At least one test for your AI-powered endpoint (mocking the external model call is fine and expected)

Tests must pass when running:

```bash
./mvnw test
```

or

```bash
./gradlew test
```

**4. A working README**

Your README (separate from this document) must allow a reviewer to clone your repo, run a single command, and have the API running locally. Include:
- Setup instructions
- How to run the project
- A description of your AI-powered endpoint and example request/response

### Build requirement

Your project must build and start with a single command, for example:

```bash
./mvnw spring-boot:run
```

or

```bash
./gradlew bootRun
```

We will run this command cold, with no prior setup beyond having Java 17 and internet access.

---

## What You Do Not Need to Build

- Authentication or authorization
- A production-grade database
- High test coverage beyond the basics described above
- Deployment configuration

---

## Using AI

You must use an AI agentic model as your **primary builder** for this project — not just as an assistant or autocomplete. This is the most important skill we are evaluating.

What we mean by agentic use: you should be directing the model to take on substantial, multi-step work — asking it to scaffold the project, implement entire features, debug its own output, and iterate based on your feedback. Think of yourself as the architect and reviewer, and the model as the engineer doing the building. Your job is to give it clear direction, evaluate what it produces, and guide it toward a working result.

Tools that support this style of working well include [Claude](https://claude.ai), [Cursor](https://cursor.sh), [ChatGPT](https://chat.openai.com), and [Gemini](https://gemini.google.com). Any tool that allows multi-turn, context-aware conversations with a model is acceptable.

What we are looking for in your transcript: evidence that you used the model to drive real construction work, not just to answer one-off questions. A strong transcript will show the model generating code, you reviewing and redirecting it, the model fixing problems, and so on — a genuine back-and-forth build session.

### Capturing your transcript

Before you start, open a conversation in your AI tool of choice and keep it running for the duration of the project. At submission time, export or copy the full conversation.

- **Claude:** Use [claude.ai](https://claude.ai). At the end of your session, copy the full conversation from the browser or use the export feature if available.
- **ChatGPT:** Use [chat.openai.com](https://chat.openai.com). You can share a conversation link or copy the text.
- **Cursor / Copilot / other tools:** Copy the chat panel contents, or take a sequential export if the tool supports it.

Submit your transcript as a plain text or Markdown file alongside your code. If your tool does not support export, a copy-paste of the full conversation is fine.

---

## Submission

Please submit through the Greenhouse link that was part of the email you received this README from.

**Deadline:** 3 business days from receiving this README.

---

## What We Are Looking For

The most important thing we evaluate is **how you use the AI model to build**. We want to see you directing it through real construction work — not just asking it questions or using it for autocomplete. Your transcript is the primary window into this, and we read it carefully.

Beyond that, we care about: whether your project builds and runs correctly, whether your Java is clean and well-structured, whether your tests are meaningful, and whether your README gives us everything we need to run it cold.

The code matters — but a candidate who shows strong, intentional agentic use with a rougher project will always be more interesting to us than one with polished code and a thin transcript.

---

## A Note on What "Good" Looks Like

A strong submission is not necessarily one with the most features or the cleanest code. We are looking for candidates who:

- Direct the AI with clear, thoughtful prompts
- Push back or ask for clarification when the model produces something unclear or wrong
- Understand the code they submit — even if the model helped write it
- Make deliberate choices and can explain them in their README

A transcript showing one debugging session where you identified a flaw in the model's suggestion, corrected it, and explained why — is worth more to us than a polished project with no visible thinking.

---

## Questions

If anything in this document is unclear, please reach out to angelo.veres@eulerity.com before starting. We would rather answer a question upfront than have you blocked or heading in the wrong direction.

Please make sure to not include your AI API keys in your submission. We will not use your key even if you forgot to remove it.

Good luck — we look forward to reading your submission.

---

**Cursor**

Running a final audit against the hackathon requirements.


