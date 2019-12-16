FROM openjdk:8-jdk-alpine
ARG api_uri="api"
# install standart fonts
# otherwise column autosize function of org.apache.poi will not work
# when exporting notes to Excel
ENV LANG en_GB.UTF-8
RUN apk add --no-cache ttf-dejavu
# persist data in H2 database in folder /.h2
ENV SPRING_PROFILES_ACTIVE h2file
ENV SPRING_DATASOURCE_URL "jdbc:h2:file:./.h2/mynotes"
ENV SPRING_H2_CONSOLE_ENABLED false
ENV APP_API_URL_BASE "/$api_uri/"
ENV APP_STATIC_SERVER_ENABLED true
VOLUME /.h2
EXPOSE 8080 8443
COPY target/mynotes-service.jar mynotes-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","mynotes-service.jar"]

# docker build -t mynotes-service .
# docker -d run -p 8080:8080 -p 8443:8443 -v /.h2:/.h2 --name mynotes_service mynotes-service
