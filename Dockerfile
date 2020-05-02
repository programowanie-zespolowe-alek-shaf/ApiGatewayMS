FROM maven:latest
WORKDIR app
COPY ag-application ./ag-application
COPY pom.xml ./
RUN mvn clean install -DskipTests -P prod
RUN mv ag-application/target/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]
