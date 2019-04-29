package objpack;

public class Account {
	public static Account NULL_ACCOUNT = null;
	static int previousId = -1;
	
	int id;
	int clientId;
	double balance;
	
	public Account(int clientId, double balance) {
		this.clientId = clientId;
		this.balance = balance;
		this.id = ++previousId;
	}
	
	void changeBalance(double sum) {
		this.balance += sum;
	}
	
	public int getId() {
		return id;
	}
	
	public double getBalance() {
		return balance;
	}
}
