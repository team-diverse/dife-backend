FROM openjdk:17-alpine AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

ENV DB_PORT $DB_PORT
ENV DB_HOST $DB_HOST

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:17-alpine
COPY --from=builder build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
VOLUME /tmp