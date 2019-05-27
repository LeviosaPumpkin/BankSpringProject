package daopack;

import java.util.ArrayList;

/*TO DO:
 * implement return values of SELECT methods
 */

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class BankDaoImplBD extends JdbcDaoSupport implements BankDao{
	private enum Currency{
		USD(1), EUR(2), RUB(3);
		private int id;
		Currency(int id){
			this.id = id;
		}
		public int getId() {
			return id;
		}
	}
	private enum Type{
		WITHDRAW(1), OPEN(2), CLOSE(3), DEPOSIT(4);
		private int id;
		Type(int id){
			this.id = id;
		}
		public int getId() {
			return id;
		}
	}
	private static final String INSERT_CLIENT = "INSERT INTO Client (name) VALUES(?)";
	private static final String INSERT_ACCOUNT = "INSERT INTO Account (clientId, balance) VALUES(?,?)"; 
	private static final String INSERT_TRANSACTION = "INSERT INTO Transactions "
			+ "(clientId, accountId, type, sum, currency, date) "
			+ "VALUES(?, ?, ?, ?, ?, ?)";
	private static final String SELECT_LATEST_CREATED_ACCOUNT  = "SELECT ID FROM ACCOUNT "
			+ "WHERE  clientId=? "
			+ "ORDER BY ID DESC "
			+ "LIMIT 1";
	private static final String UPDATE_BALANCE = "UPDATE ACCOUNT SET balance = balance + ? "
			+ "WHERE clientId=? AND id=?";
	private static final String SELECT_ACCOUNTS = "SELECT Account.id, Client.Name, Account.balance FROM Account " + 
			"JOIN Client ON Account.clientId = Client.id " + 
			"WHERE Account.clientId = ?";
	private static final String SELECT_CLIENT_TRANSACTIONS = 
			"SELECT Transactions.clientId, Transactions.accountId, TransactionDict.type, Transactions.sum, CurrencyDict.name, Transactions.date " + 
			"FROM Transactions " + 
			"JOIN TransactionDict ON TransactionDict.id=Transactions.type " + 
			"JOIN CurrencyDict ON CurrencyDict.id=Transactions.currency " + 
			"WHERE datetime(date) in (datetime(?), datetime(?)) " + 
			"AND clientId=?";
		
	public BankDaoImplBD() {
		
	}
	
	@Transactional(rollbackFor = SQLException.class)
	public void addClient(String name) {
		getJdbcTemplate().update(INSERT_CLIENT, name);
	}
	
	@Transactional(rollbackFor = SQLException.class)
	public void makeDeposit(double sum, int idClient, int idAccount) {
		try{
			insertTransaction(idClient, idAccount, Type.DEPOSIT.getId(), sum, Currency.RUB.getId());
			getJdbcTemplate().update(UPDATE_BALANCE, sum, idClient, idAccount);
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Transactional(rollbackFor = SQLException.class)
	public void makeWithdraw(double sum, int idClient, int idAccount) {
		try {
			getJdbcTemplate().update(UPDATE_BALANCE, sum*(-1), idClient, idAccount);
			insertTransaction(idClient, idAccount, Type.WITHDRAW.getId(), sum, Currency.RUB.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional(rollbackFor = SQLException.class)
	public void makeAccount(double sum, int idClient, int currency) {
		if(sum < 0) {
			System.out.println("Opening balance cannot be negative");
			return;
		}
		try {
			if(currency == 1) {
				insertAccount(sum * 64, idClient);
			}else if (currency == 2) {
				insertAccount(sum * 74, idClient);
			}else if (currency == 3) {
				insertAccount(sum, idClient);
			}
			int idAccount = getLatestCreatedAccount(idClient);
			insertTransaction(idClient, idAccount, Type.OPEN.getId(), sum, currency);
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Transactional
	public List<String> getClientsAccounts(int idClient) {
		List<Map<String,Object>> rows = getJdbcTemplate().queryForList(SELECT_ACCOUNTS, idClient);

		List<String> result = new ArrayList<>();
		rows.forEach((row) -> {
			result.add(row.get("id")+" "+row.get("Name")+" "+row.get("balance"));
		});
		
		return result;
	}
	
	@Transactional
	public List<String> getListOfTransactions(int idClient, String dateFrom, String dateTo){
		
		List<Map<String,Object>> rows = getJdbcTemplate().queryForList(SELECT_CLIENT_TRANSACTIONS, new Object [] {dateFrom, dateTo, idClient});

		List<String> result = new ArrayList<>();
		rows.forEach((row) -> {
			result.add(row.get("clientId")+" "+row.get("accountId")+" "+row.get("type")+" "+row.get("sum")+" "+row.get("name")+" "+row.get("date"));
		});
		return result;
	}
	
	@Transactional()
	private void insertAccount(double sum, int idClient) throws SQLException{
		getJdbcTemplate().update(INSERT_ACCOUNT, idClient, sum);
	}
	
	@Transactional
	private int getLatestCreatedAccount(int idClient) {		
		int accountId = getJdbcTemplate().queryForObject(SELECT_LATEST_CREATED_ACCOUNT, Integer.class, idClient);
		return accountId;
	}
	
	@Transactional(
			rollbackFor = SQLException.class)
	private void insertTransaction(
			int idClient, 
			int idAccount,
			int type,
			double sum, 
			int currency) throws SQLException{

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		getJdbcTemplate().update(INSERT_TRANSACTION, idClient, idAccount, type, sum, currency, dateFormat.format(date).toString());
				
	}

}
