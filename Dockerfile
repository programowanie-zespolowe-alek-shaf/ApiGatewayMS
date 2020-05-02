FROM maven:3.6.3-openjdk-11
WORKDIR app
COPY ag-application ./ag-application
COPY pom.xml ./

ENV MYSQL_CUSTOMER_DB_HOST name
ENV MYSQL_CUSTOMER_DB_PORT 3306
ENV MYSQL_CUSTOMER_DATABASE customer
ENV MYSQL_CUSTOMER_DB_USERNAME customer
ENV MYSQL_CUSTOMER_DB_PASSWORD customer
ENV EUREKA_HOST name
ENV EUREKA_PORT 8761

RUN mvn clean install -DskipTests -P prod
RUN mv ag-application/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]
