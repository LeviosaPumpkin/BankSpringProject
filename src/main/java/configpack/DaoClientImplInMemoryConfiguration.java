package configpack;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import daopack.BankDaoImplInMemory;

@Configuration
public class DaoClientImplInMemoryConfiguration {
		@Bean 
		public BankDaoImplInMemory daoClientImplInMemoryGenerator() {
			return new BankDaoImplInMemory();
		}
		
}
