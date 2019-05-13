import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configpack.BankConfiguration;
import configpack.ClientConfiguration;
import configpack.DaoClientImplInMemoryConfiguration;
import daopack.BankDao;
import daopack.BankDaoImplInMemory;
import objpack.Client;

public class Main {
	public static void main(String [] args) {
		
		/*ApplicationContext context = 
				new AnnotationConfigApplicationContext(DaoClientImplInMemoryConfiguration.class);
				
		BankDaoImplInMemory dao = context.getBean(BankDaoImplInMemory.class);
		
		dao.addClient("Mary");
		
		dao.makeAccount(200, 0);
		
		dao.makeDeposit(200, 0, 0);
		
		dao.makeWithdraw(300, 0, 0);
		
		dao.printAllTransaction();*/
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(BankConfiguration.class);
		
		BankDao bankDao = context.getBean(BankDao.class);
		bankDao.addClient("");
		//bankDao.makeAccount(20000, 2);
		//bankDao.makeDeposit(1000, 1, 1);
		//bankDao.makeWithdraw(50, 1, 2);
		//bankDao.getClientsAccounts(1);
		//bankDao.getListOfTransactions(1, "2019-05-08 12:51:12", "2019-05-09 11:06:34");

	}
}
