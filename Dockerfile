# Stage 1: Build the JAR
FROM maven:3.8.5-openjdk-17

VOLUME /tmp

COPY ./target/*.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#port to deploy
EXPOSE 7070
