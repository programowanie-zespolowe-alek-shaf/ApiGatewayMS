FROM maven:3.6.3-openjdk-11
WORKDIR app
COPY ag-application ./ag-application
COPY pom.xml ./

ENV ORDER_MS_URL http://order-service:8080
ENV PRODUCT_MS_URL http://product-service:8080
ENV CUSTOMER_MS_URL http://customer-service:8080
ENV CART_MS_URL http://cart-service:8080
ENV PAYMENT_MS_URL http://payment-service:8080

RUN mvn clean install -DskipTests -P prod
RUN mv ag-application/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]
