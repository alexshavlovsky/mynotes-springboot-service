# MyNotes SpringBoot Service

Basic SpringBoot database service with REST API.

Frontend client for this project: [MyNotes Angular Client](https://github.com/alexshavlovsky/mynotes-ng-client.git).

## Technology Stack
Component         | Technology
---               | ---
Runtime           | Java 8
Build tool        | Maven
Server            | SpringBoot WEB (Tomcat)
Security          | SSL, basic auth
Database          | SpringBoot JPA (Hibernate), H2         
Email             | SpringBoot MAIL
Async tasks       | ThreadPoolTaskExecutor
REST data mapping | Jackson, Bean Validation API
Testing           | Junit, Mockito, MockMvc, GreenMail
REST Documentation| SpringFox Swagger2

## API specification

Default base API URL: https://localhost:8443/api/

Feedback Email queue endpoint:
                       
Method  |URI           |Operation
---     |---           |---
POST    |/feedback     |accept

Note resource endpoints:

Method  |URI           |Operation
---     |---           |---
GET     |/notes        |read all
POST    |/notes        |create
GET     |/notes/:id    |read
PUT     |/notes/:id    |update
DELETE  |/notes/:id    |delete

Notebook resource endpoints:

Method  |URI            |Operation
---     |---            |---
GET     |/notebooks     |read all
POST    |/notebooks     |create
GET     |/notebooks/:id |read
PUT     |/notebooks/:id |update
DELETE  |/notebooks/:id |delete

Status code conventions:

Method         |Success status code
---            |---
GET            |200 OK
POST           |201 Created
POST feedback  |202 Accepted
PUT            |200 OK
DELETE         |200 OK
