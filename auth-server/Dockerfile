FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/auth-server.jar /app/auth-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/auth-server.jar"]