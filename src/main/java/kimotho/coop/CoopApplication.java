package kimotho.coop;

import kimotho.coop.rest.CustomerResource;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;


@SpringBootApplication
public class CoopApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CoopApplication.class, args);
    }

}


