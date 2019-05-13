package daopack;

import java.util.ArrayList;
import java.util.List;

public interface BankDao {
	void addClient(String name);
	void makeDeposit(double sum, int idClient, int idAccount);
	void makeWithdraw(double sum, int idClient, int idAccount);
	default void makeAccount(double sum, int idClient) {};
	default void makeAccount(double sum, int idClient, int currency) {};
	default List<String> getListOfTransactions(int idClient, String dateFrom, String dateTo){
		return new ArrayList<String>();
	};
	//public String getInfoAboutTransaction(int idTransaction); 
	default String getClientsAccounts(int i) {
		return "";
	};
	
}
