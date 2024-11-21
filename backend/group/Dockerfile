FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

# pipeline overrides this variable
ARG BACKEND_DOMAIN=localhost

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

RUN apk --no-cache add curl

EXPOSE 8020

ENV BACKEND_HOST=${BACKEND_DOMAIN}

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
