FROM eclipse-temurin:17 as builder
WORKDIR app
COPY target/*.jar todo.jar
RUN java -Djarmode=layertools -jar todo.jar extract

FROM eclipse-temurin:17
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]