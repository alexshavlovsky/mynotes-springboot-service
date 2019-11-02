FROM openjdk:8-jdk-alpine
COPY target/mynotes-service.jar mynotes-service.jar
ENV SPRING_PROFILES_ACTIVE h2file
ENV SPRING_DATASOURCE_URL "jdbc:h2:file:./.h2/mynotes"
ENV SPRING_H2_CONSOLE_ENABLED false
VOLUME /.h2
EXPOSE 8080 8443
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","mynotes-service.jar"]

# docker build -t mynotes-service .
# && docker run
# -p 8080:8080 -p 8443:8443
# -v /.h2:/.h2
# --name mynotes_service
# mynotes-service
