FROM openjdk:8-jdk-alpine
COPY target/mynotes-service-v3.1-SNAPSHOT.jar app.jar
EXPOSE 8080 8443
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
