package co.com.crediya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = {
        "co.com.crediya.api",
        "co.com.crediya.r2dbc",
        "co.com.crediya.restconsumer",
        "co.com.crediya.usecase",
        "co.com.crediya.config",
        "co.com.crediya.sqs.sender",
        "co.com.crediya.metrics"
})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
