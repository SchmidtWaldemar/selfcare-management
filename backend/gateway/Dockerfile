FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests -DactivatedProperties=dev

FROM openjdk:17-jdk-alpine

ARG PROFILE=dev

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8222

ENV ACTIVE_PROFILE=${PROFILE}

# Run the application
ENTRYPOINT ["java", "-DactivatedProperties=${ACTIVE_PROFILE}", "-jar", "app.jar"]
