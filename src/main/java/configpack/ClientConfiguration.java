package configpack;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import objpack.Client;

@Configuration
public class ClientConfiguration {
	@Bean 
	@Scope(value = "prototype")
	@Lazy(value = true)
	public Client clientGenerator(String name) {
		Client client = new Client(name);
		return client;
	}
}
