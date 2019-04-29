package objpack;

import java.util.Date;

public class Transaction {
	public enum Type {DEPOSIT, WITHDRAW, OPEN};
	
	static int previousId = -1;
	
	int id;
	int clientId;
	int accountId;
	Type type;
	Date date;
	
	public Transaction(int clientId, int accountId, Type type, Date date) {
		this.id = ++previousId;
		this.clientId = clientId;
		this.accountId = accountId;
		this.type = type;
		this.date = date;
	}
	
	public String toString(){
		return id+"\t"
				+clientId+"\t"
				+accountId+"\t"
				+type+"\t"
				+date+"\n";
	}

}
