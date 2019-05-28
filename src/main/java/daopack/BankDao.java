package daopack;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public interface BankDao {
	void addClient(String name);
	default void makeDeposit(double sum, int idClient, int idAccount) {};
	default void makeDeposit(double sum, int idClient, int idAccount, int currency) {};
	default void makeWithdraw(double sum, int idClient, int idAccount) {};
	default void makeWithdraw(double sum, int idClient, int idAccount,int currency) {};
	default void makeAccount(double sum, int idClient) {};
	default void makeAccount(double sum, int idClient, int currency) {};
	default void makeAccount(double sum, int idClient, int currency, double rate) {};

	default List<String> getListOfTransactions(int idClient, String dateFrom, String dateTo){
		return new ArrayList<String>();
	};
	//public String getInfoAboutTransaction(int idTransaction); 
	default List<String> getClientsAccounts(int i) {
		return new ArrayList<String>();
	}
	
}
