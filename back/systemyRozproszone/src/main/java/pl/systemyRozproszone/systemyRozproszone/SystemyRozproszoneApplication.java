package pl.systemyRozproszone.systemyRozproszone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SystemyRozproszoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemyRozproszoneApplication.class, args);
	}

}
