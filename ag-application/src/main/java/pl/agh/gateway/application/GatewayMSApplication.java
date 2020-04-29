package pl.agh.gateway.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@ComponentScan(
        basePackages = {
                "pl.agh.gateway"
        }
)
public class GatewayMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayMSApplication.class, args);
    }
}
