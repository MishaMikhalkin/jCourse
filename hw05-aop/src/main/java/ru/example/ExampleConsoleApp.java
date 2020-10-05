package ru.example;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExampleConsoleApp {

    private static Logger LOG = LoggerFactory
            .getLogger(ExampleConsoleApp.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        ConfigurableApplicationContext context =SpringApplication.run(ExampleConsoleApp.class, args);
        TestService service = (TestService) context.getBean("testService");
        LOG.info("bean result: " + service.calculate(1, 2) );

        LOG.info("APPLICATION FINISHED");

    }

    @Bean
    TestService testService() {
        return new TestService();
    }
}
