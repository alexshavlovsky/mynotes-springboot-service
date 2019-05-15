# MyNotes SpringBoot Service

Basic SpringBoot database service with REST API.

Frontend client for this project: [MyNotes Angular Client](https://github.com/alexshavlovsky/mynotes-ng-client.git).

## Embedded frontend client

Prebuilt Angular client is included and served at `https://localhost:8443`
<br>
Default username: usr1
<br>
Default password: pwd1

## Build and run instructions

Tested on Maven 3.0.5 and Java 1.8.0_212:
```
git clone https://github.com/alexshavlovsky/mynotes-springboot-service.git
cd mynotes-springboot-service
mvn package
cd target
java -jar spring-angular-sandbox-0.0.1-SNAPSHOT.jar
firefox https://localhost:8443
```

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

Admin command controller endpoint:
                       
Method  |URI           |Operation
---     |---           |---
POST    |/command      |accept

Note resource endpoints:

Method  |URI           |Operation
---     |---           |---
GET     |/notes        |read all
POST    |/notes        |create
GET     |/notes/:id    |read
PUT     |/notes/:id    |update
DELETE  |/notes/:id    |delete

Notebook resource endpoints:

Method  |URI                  |Operation
---     |---                  |---
GET     |/notebooks           |read all
GET     |/notebooks?nbId=:id  |read by notebook id
POST    |/notebooks           |create
GET     |/notebooks/:id       |read
PUT     |/notebooks/:id       |update
DELETE  |/notebooks/:id       |delete

Status code conventions:

Method         |Success status code
---            |---
GET            |200 OK
POST resource  |201 Created
POST feedback  |202 Accepted
POST command   |202 Accepted
PUT            |200 OK
DELETE         |200 OK
