FROM maven:3.6.3-openjdk-11
WORKDIR app
COPY ag-application ./ag-application
COPY pom.xml ./
RUN mvn clean install -DskipTests -P prod
RUN mv ag-application/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]
