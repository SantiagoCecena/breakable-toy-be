# 📝 To-Do App Backend

## Description
This is a backend service for managing "to do" tasks for the client. The application allows users to create, update, mark as done/undone, and filter tasks. 

## 🚀 Technologies used
- Java
- Spring boot
- Maven
- Spring boot starter validation

## ⚒️ Installation instructions
### Prerequisites
- Java 21
- Maven

### Setup:

1. Clone the repository:
```git clone https://github.com/SantiagoCecena/breakable-toy-be.git```
```cd breakable-toy-be```  

2. Build the project:  
```mvn clean install```  

3. Run the project:
```mvn spring-boot:run```
The application will be available at http://localhost:9090 

## 📖 API Documentation
### Endpoints

- **GET api/todos:** Fetch all to-do tasks with pagination.

- **POST api/todos:** Create a new to-do task.
- **PUT api/todos/{id}:** Update a to-do task by ID.
- **POST api/todos/{id}/done:** Mark a to-do task as done.
- **PUT api/todos/{id}/undone:** Mark a to-do task as undone.
- **DELETE /api/todos/{id}:** Delete a to-do task by its ID.
- **GET /api/todos/averages:** Get average metrics for to-do tasks