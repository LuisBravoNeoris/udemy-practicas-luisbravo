FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/client-app.jar /app/client-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/client-app.jar"]