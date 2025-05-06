

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "app", "controller", "service", "dto" })
public class CardCreationSpringApplication {
	public static void main(String[] args) {
		SpringApplication.run(CardCreationSpringApplication.class, args);
	}
}