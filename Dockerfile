FROM eclipse-temurin:25-jdk

COPY target/client-management-api-0.0.1-SNAPSHOT.jar /app/client-management-api.jar

CMD ["java", "-jar", "/app/client-management-api.jar"]