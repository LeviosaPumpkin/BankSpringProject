package objpack;

import java.util.ArrayList;
import java.util.List;
import static objpack.Account.NULL_ACCOUNT;

public class Client {
	static int previousId = -1;
	int id;
	String name;
	List<Account> accounts;
	
	public Client(String name) {
		this.name = name;
		this.id = ++previousId;
		this.accounts = new ArrayList<>();
	}
	
	public int getId(){
		return id;
	}
	private Account result;
	public Account getAccountById(int accountId) {
		result = NULL_ACCOUNT;
		accounts.forEach(account -> {
			if(account.getId() == accountId) {
				result = account;
			}
		});
		return result;
	}
	
	public void makeDeposit(double sum, int idAccount){
		Account account = getAccountById(idAccount);
		if(account != NULL_ACCOUNT) {
			account.changeBalance(sum);
		}else {
			System.out.println("Account does not exists");
		}
	}
	
	public void makeWithdraw(double sum, int idAccount){
		Account account = getAccountById(idAccount);
		if(account != NULL_ACCOUNT) {
			account.changeBalance(0-sum);
		}else {
			System.out.println("Account does not exists");
		}
	}
	public void addAccount(double sum) {
		accounts.add(new Account(this.getId(), sum));
	}
	
	public int getNewestAccountId() {
		return accounts.get(accounts.size()-1).getId();
	}
	
}
