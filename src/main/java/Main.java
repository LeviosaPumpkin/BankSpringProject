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
		//bankDao.addClient("Max");
		//bankDao.makeAccount(17000, 3, 3);
		//bankDao.makeDeposit(1000, 3, 8);
		//bankDao.makeWithdraw(500, 3, 8);
		//bankDao.getClientsAccounts(1);
		//bankDao.getListOfTransactions(1, "2019-05-08 12:51:12", "2019-05-09 11:06:34");
		//bankDao.makeAccount(12000, 2, 1);
		bankDao.getListOfTransactions(3,"2019-05-08 13:00:13", "2019-05-15 20:34:01").forEach((row) -> {
			System.out.println(row);
		});

	}
}
