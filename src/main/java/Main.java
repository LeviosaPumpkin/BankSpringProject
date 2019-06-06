import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configpack.BankConfiguration;
import configpack.ClientConfiguration;
import configpack.DaoClientImplInMemoryConfiguration;
import currencyratepack.CurrencyRate;
import daopack.BankDao;
import daopack.BankDaoImplInMemory;
import objpack.Client;

public class Main {
	public static void main(String [] args) throws IOException {
		
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(BankConfiguration.class);
		
		BankDao bankDao = context.getBean(BankDao.class);
		
		bankDao.makeDeposit(500, 4, 9, 2);

	}
}
