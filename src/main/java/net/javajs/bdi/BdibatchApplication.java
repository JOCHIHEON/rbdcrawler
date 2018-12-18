package net.javajs.bdi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@PropertySource("conf/crawl.properties")
public class BdibatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdibatchApplication.class, args);
	}
}
///MongoAutoConfiguration.class, MongoDataAutoConfiguration.class
