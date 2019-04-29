package testpack;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configpack.ClientConfiguration;
import objpack.Client;

import static junit.framework.TestCase.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class ClientTest {
	
	/*@Test
	public void checkClientConstruction() {
		Client client = new Client("M");
		assertEquals("Client created with id: ", 0, client.getId());
	}*/
	
	List<Client> clients;
	
	@Before
	public void init() {
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(ClientConfiguration.class);
		
		clients = new ArrayList<>();
				
		clients.add((Client) context.getBean("clientGenerator", "Mary"));
		
		clients.add((Client) context.getBean("clientGenerator", "John"));
	}
	
	@Test
	public void checkListOfClients() {
		assertEquals("Client created with id: ", 5, clients.get(1).getId());
	}
	
	@Test
	public void checkAccountCreation() {
		clients.get(0).addAccount(1000);
		
		assertEquals("Account id is ", 0, clients.get(0).getAccountById(0).getId());
	}
	
	@Test
	public void checkAccountBalance() {
		clients.get(0).addAccount(1000);
		
		double balance = clients.get(0).getAccountById(1).getBalance();
		
		assertEquals("Balance of account 0 is ", 1000.0, balance);
	}
	
	@Test
	public void checkMakeDeposit() {
		clients.get(0).addAccount(1000);
		
		clients.get(0).makeDeposit(1000, 0);
		
		double balance = clients.get(0).getAccountById(0).getBalance();
		
		assertEquals("Balance of account 0 is", 2000.0, balance);

	}
	
	@Test
	public void checkMakeWithDraw() {
		clients.get(0).addAccount(1000);
		
		clients.get(0).makeWithdraw(500, 0);
		
		double balance = clients.get(0).getAccountById(0).getBalance();

		
		assertEquals("Balance of account 0 is", 500.0, balance);

	}
	
	@Test
	public void checkMakeWithDrawNotexistAccount() {
		clients.get(0).addAccount(1000);
		
		clients.get(0).makeWithdraw(500, 1);		
	}

}
