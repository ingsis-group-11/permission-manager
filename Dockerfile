FROM gradle:8.10.1-jdk21 AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble
FROM openjdk:21-jdk-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/permission-manager-service.jar
COPY ./newrelic-agent/newrelic.jar /app/newrelic.jar
COPY ./newrelic-agent/newrelic.yml /app/newrelic.yml
ENTRYPOINT ["java", "-javaagent:/app/newrelic.jar", "-jar", "-Dspring.profiles.active=production", "/app/permission-manager-service.jar"]