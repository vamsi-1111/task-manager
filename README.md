# Task Manager

## Project Overview

Task Manager is a personal task management REST API built with Spring Boot. It provides full CRUD operations for tasks stored in an in-memory H2 database, along with a simple web frontend for interacting with the API without a REST client.

Each task includes a title, optional description, due date, priority (LOW, MEDIUM, HIGH), and status (TODO, IN_PROGRESS, DONE). The application also includes an AI-powered feature that uses the Anthropic Claude API to generate a plain-language summary of any existing task.

## Tech Stack

- **Java 17**
- **Spring Boot 4.1**
- **Maven**
- **H2 Database** (in-memory)
- **Anthropic Claude API** (`claude-haiku-4-5-20251001`)

## Prerequisites

Before running the application, ensure you have:

1. **Java 17** installed and available on your PATH
2. An **Anthropic API key** for the AI summary feature

Internet access is required for Maven dependency resolution and for calling the Claude API.

## Setup Instructions

1. **Clone the repository**

   ```bash
   git clone <github.com/vamsi-1111/task-manager>
   cd task-manager
   ```

2. **Configure your Anthropic API key**

   Open `src/main/resources/application.properties` and replace the placeholder with your API key:

   ```properties
   anthropic.api.key=YOUR_ANTHROPIC_API_KEY_HERE
   ```

   Do not commit your real API key to version control.

3. **Start the application** (see below)

## How to Run

From the project root directory:

```bash
./mvnw spring-boot:run
```

The API and frontend will be available at `http://localhost:8080`.

## How to Run Tests

From the project root directory:

```bash
./mvnw test
```

All unit and integration tests should pass without requiring a live Anthropic API call (the AI endpoint is mocked in tests).

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/tasks` | Create a new task |
| `GET` | `/tasks` | List all tasks |
| `GET` | `/tasks/{id}` | Get a single task by ID |
| `PUT` | `/tasks/{id}` | Update an existing task |
| `DELETE` | `/tasks/{id}` | Delete a task by ID |
| `POST` | `/tasks/{id}/summarize` | Generate an AI summary of a task |

### Example: Create a task

**Request**

```http
POST /tasks
Content-Type: application/json

{
  "title": "Submit quarterly report",
  "description": "Compile Q2 metrics and send to leadership",
  "dueDate": "2026-06-20",
  "priority": "HIGH",
  "status": "TODO"
}
```

## AI Endpoint

### `POST /tasks/{id}/summarize`

Generates a 2–3 sentence plain-language summary of the task with the given ID using the Anthropic Claude API. The task must already exist in the database.

**Request**

```http
POST /tasks/1/summarize
```

**Example response**

```json
{
  "summary": "This task involves submitting the quarterly report with Q2 metrics to leadership. It is marked as high priority and is currently in TODO status, with a due date of 2026-06-20."
}
```

## Frontend

The web interface is served at **http://localhost:8080** and supports:

- **Viewing** all tasks in a table
- **Creating** new tasks via a form
- **Updating** existing tasks via an edit form
- **Deleting** tasks
- **Generating AI summaries** for tasks by ID

Open `http://localhost:8080` in your browser after starting the application with `./mvnw spring-boot:run`.
