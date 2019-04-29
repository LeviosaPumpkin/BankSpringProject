package testpack;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configpack.BankConfiguration;
import daopack.BankDao;

public class BankDaoImplBDTest {
	//@Test
	public void checkConstructor() {
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(BankConfiguration.class);
		
		BankDao bankDao = context.getBean(BankDao.class);
		bankDao.addClient("Mary");
		
	}
}
