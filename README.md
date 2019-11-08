# MyNotes SpringBoot Service

Basic SpringBoot CRUD service with REST API.

Frontend clients for this project:
<br>
[Angular Bootstrap REST Client](https://github.com/alexshavlovsky/mynotes-ng-client.git).
<br>
[Angular Material NgRx  REST Client](https://github.com/alexshavlovsky/mynotes-material-client.git).

## Embedded frontend client

Prebuilt Angular client is included and served at `https://localhost:8443`
<br>
Default users:

Email              |Password |Role
---                |---      |---
user@example.com   |12345    |USER
admin@example.com  |12345    |ADMIN
admin2@example.com |12345    |ADMIN, USER

Granted authorities by the role:
<br>
ADMIN can view the list of users and execute commands on backend
<br>
USER can create, edit, delete notebooks and notes and send a feedback

## Build and run instructions

Tested on Maven 3.0.5 and Java 1.8.0_212:
```
git clone https://github.com/alexshavlovsky/mynotes-springboot-service.git
cd mynotes-springboot-service
mvn package
cd target
java -jar mynotes-service.jar
firefox https://localhost:8443
```

On Windows create a `.cmd` file and execute it from any folder:
```
git clone https://github.com/alexshavlovsky/mynotes-springboot-service.git
call mvn package -f mynotes-springboot-service\pom.xml --log-file maven.log
start cmd.exe /c java -jar .\mynotes-springboot-service\target\mynotes-service.jar
timeout 15
start firefox https://localhost:8443
```

With Docker:
```
git clone https://github.com/alexshavlovsky/mynotes-springboot-service.git \
  && cd mynotes-springboot-service \
  && mvn clean package

docker build -t mynotes-service . \
  && docker run -d -p 8080:8080 -p 8443:8443 -v /.h2:/.h2 --name mynotes_service mynotes-service
```

To deploy both API and frontend on the same host run:
```
docker pull docker.pkg.github.com/alexshavlovsky/mynotes-material-client/mynotes-front:v1.0 \
  && docker pull docker.pkg.github.com/alexshavlovsky/mynotes-springboot-service/mynotes-service:v3.2 \
  && docker run -d -p 8080:8080 -p 8443:8443 -v /.h2:/.h2 --name mynotes_service \
    docker.pkg.github.com/alexshavlovsky/mynotes-springboot-service/mynotes-service:v3.2 \
  && docker run -d -p 80:80 -p 443:443 --name mynotes_front \
    docker.pkg.github.com/alexshavlovsky/mynotes-material-client/mynotes-front:v1.0
``` 
A backend API will be accessible on port `8443`.
<br>
A Tomcat hosted Bootstrap client will be accessible on port `8080`.
<br>
An Nginx hosted Angular Material client will be accessible on ports `80` and `443`.

## Screenshot

<p align="center">
  <img src="screenshots/3_user-notes.png?raw=true"/>
</p>

## Technology Stack

Component          | Technology
---                | ---
Runtime            | Java 8
Build tool         | Maven
Server             | SpringBoot WEB (Tomcat)
Security           | SSL, JWT
JWT implementation | [jsonwebtoken.io](https://github.com/jwtk/jjwt)
Database           | SpringBoot JPA (Hibernate), H2, MySQL         
Email              | SpringBoot MAIL
Async tasks        | ThreadPoolTaskExecutor
REST data mapping  | Jackson, Bean Validation API, [ModelMapper](https://github.com/modelmapper/modelmapper)
Code reducer       | [ProjectLombok](https://github.com/rzwitserloot/lombok)
Testing            | Junit, Mockito, MockMvc, [GreenMail](https://github.com/greenmail-mail-test/greenmail)
REST Documentation | [SpringFox Swagger2](https://github.com/springfox/springfox/releases)
MS Excel export    | [Apache POI](http://poi.apache.org/)
Prod packaging     | Docker Engine Container, Alpine Linux, OpenJDK, SpringBoot JAR

## API specification

Default base API URL: `https://localhost:8443/api/`

User endpoint:
                       
Method  |URI            |Operation
---     |---            |---
POST    |/users         |create a user
POST    |/users/login   |create a token
GET     |/users/current |read the current
GET     |/users         |read all (ADMIN only)

Feedback Email queue endpoint:
                       
Method  |URI            |Operation
---     |---            |---
POST    |/feedback      |accept

Admin command controller endpoint:
                       
Method  |URI            |Operation
---     |---            |---
POST    |/command       |accept (ADMIN only)

Note resource endpoints:

Method  |URI                   |Operation
---     |---                   |---
GET     |/notebooks/{id}/notes |read by notebook id
GET     |/notes                |read all
GET     |/notes/export/xls     |export as an Excel 2003 .xls file
POST    |/notes                |create
PUT     |/notes/{id}           |update
DELETE  |/notes/{id}           |delete

Notebook resource endpoints:

Method  |URI                   |Operation
---     |---                   |---
GET     |/notebooks            |read all
POST    |/notebooks            |create
PUT     |/notebooks/{id}       |update
DELETE  |/notebooks/{id}       |delete

Status code conventions:

Method         |Success status code
---            |---
GET            |200 OK
POST resource  |201 Created
POST feedback  |202 Accepted
POST command   |202 Accepted
PUT            |200 OK
DELETE         |200 OK
