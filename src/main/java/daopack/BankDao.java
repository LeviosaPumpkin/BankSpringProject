package daopack;

public interface BankDao {
	public void addClient(String name);
	public void makeDeposit(double sum, int idClient, int idAccount);
	public void makeWithdraw(double sum, int idClient, int idAccount);
	public void makeAccount(double sum, int idClient);
	//public List<String> getListOfTransactions();
	//public String getInfoAboutTransaction(int idTransaction); 
}
