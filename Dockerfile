FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean install -DskipTests

FROM openjdk:17-slim 

WORKDIR /app

EXPOSE 8080

ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]