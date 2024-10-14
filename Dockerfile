FROM eclipse-temurin:21-jdk
COPY wgo/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080