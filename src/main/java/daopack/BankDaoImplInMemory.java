package daopack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configpack.ClientConfiguration;
import objpack.Client;
import static objpack.Account.NULL_ACCOUNT;
import objpack.Transaction.Type.*; 
import objpack.Transaction;

public class BankDaoImplInMemory implements BankDao {
	
	List<Client> clients;
	List<Transaction> transactions;
	
	public BankDaoImplInMemory() {
		clients = new ArrayList<>();
		transactions = new ArrayList<>();
	}
	
	public void addClient(String name) {
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(ClientConfiguration.class);
		clients.add((Client) context.getBean("clientGenerator", name));
		//clients.add(new Client(name));
		int id = clients.size()-1;
	}
	
	public Client getClient(int idClient) {
		return clients.get(idClient);
	}
	public void makeDeposit(double sum, int idClient, int idAccount) {
		clients.get(idClient).makeDeposit(sum, idAccount);
		transactions.add(new Transaction(idClient, idAccount, Transaction.Type.DEPOSIT, new Date()));
	}
	public void makeWithdraw(double sum, int idClient, int idAccount) {
		clients.get(idClient).makeDeposit(sum, idAccount);
		transactions.add(new Transaction(idClient, idAccount, Transaction.Type.WITHDRAW, new Date()));
	}
	public void makeAccount(double sum, int idClient) {
		clients.get(idClient).addAccount(sum);
		int idAccount = clients.get(idClient).getNewestAccountId();
		transactions.add(new Transaction(idClient, idAccount, Transaction.Type.OPEN, new Date()));
	}
	
	public void printAllTransaction() {
		transactions.forEach((t) -> {
			System.out.println(t.toString());
		});
	}

}
