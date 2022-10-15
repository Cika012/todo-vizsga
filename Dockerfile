FROM eclipse-temurin:17
WORKDIR app
COPY target/*.jar todo.jar
ENTRYPOINT ["java", "-jar", "todo.jar"]