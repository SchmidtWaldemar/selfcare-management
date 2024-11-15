FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8030

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
