# Maven base image
FROM maven:amazoncorretto AS build

# Setting the working directory in the container
WORKDIR /app

# Copying the POM file
COPY pom.xml .

# Downloading maven dependencies
RUN mvn dependency:go-offline -B

# Copying the application source code
COPY src src

# Building the application
RUN mvn clean package -DskipTests

# Java base
FROM openjdk:11-jre-slim

# Setting the working directory in the container
WORKDIR /app

# Copying the JAR file from the build folder to the containe
COPY --from=build /app/target/*.jar app.jar

# Exposing the port your application runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
