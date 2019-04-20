# SpringBoot REST service

Basic SpringBoot database service with REST API.

## Technology Stack
Component         | Technology
---               | ---
Build tool        | Maven
Server            | SpringBoot WEB (Tomcat)
Database          | SpringBoot JPA (Hibernate), H2         
Email             | SpringBoot MAIL
REST data mapping | Jackson, Bean Validation API
Testing           | Junit, Mockito, MockMvc, GreenMail
REST Documentation| SpringFox Swagger2

## API description

Feedback Email Controller

Method  |Endpoint           |Action
---     |---                |---
POST    |/api/feedback/     |sendFeedback

Note CRUD Controller

Method  |Endpoint           |Action
---     |---                |---
GET     |/api/notes/        |getAllNotes
POST    |/api/notes/        |saveNote
GET     |/api/notes/{id}    |getNote
PUT     |/api/notes/{id}    |updateNote
DELETE  |/api/notes/{id}    |deleteNote

Notebook CRUD Controller

Method  |Endpoint           |Action
---     |---                |---
GET     |/api/notebooks/    |getAllNotebooks
POST    |/api/notebooks/    |saveNotebook
GET     |/api/notebooks/{id}|getNotebook
PUT     |/api/notebooks/{id}|updateNotebook
DELETE  |/api/notebooks/{id}|deleteNotebook

