package daopack;

import java.util.ArrayList;

/*TO DO:
 * implement return values of SELECT methods
 */

import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

public class BankDaoImplBD implements BankDao{
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
	private static final String INSERT_RUB_TRANSACTION = "INSERT INTO Transactions "
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
	
	private final DataSource dataSource;
	
	public BankDaoImplBD(DataSource ds) {
		dataSource = ds;
	}
	
	@Transactional(rollbackFor = SQLException.class)
	public void addClient(String name) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(INSERT_CLIENT)){
			ps.setString(1,  name);
			ps.executeUpdate();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Transactional(rollbackFor = SQLException.class)
	public void makeDeposit(double sum, int idClient, int idAccount) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(UPDATE_BALANCE)){
			
			ps.setDouble(1,  sum);
			ps.setInt(2, idClient);
			ps.setInt(3, idAccount);
			ps.executeUpdate();
			
			insertTransaction(idClient, idAccount, Type.DEPOSIT.getId(), sum, Currency.RUB.getId());
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Transactional(rollbackFor = SQLException.class)
	public void makeWithdraw(double sum, int idClient, int idAccount) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(UPDATE_BALANCE)){
			
			sum=sum*(-1);
			ps.setDouble(1,  sum);
			ps.setInt(2, idClient);
			ps.setInt(3, idAccount);
			ps.executeUpdate();
			
			insertTransaction(idClient, idAccount, Type.WITHDRAW.getId(), sum, Currency.RUB.getId());
		}catch(SQLException e) {
			throw new RuntimeException(e);
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
	
	public String getClientsAccounts(int idClient) {
		String result = "";
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_ACCOUNTS)){
			ps.setInt(1, idClient);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				System.out.print(rs.getInt("id")+"\t");
				System.out.print(rs.getString("name")+"\t");
				System.out.println(rs.getDouble("balance"));
			}
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public List<String> getListOfTransactions(int idClient, String dateFrom, String dateTo){
		List<String> result = new ArrayList<>();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_CLIENT_TRANSACTIONS)){
			ps.setString(1, dateFrom);
			ps.setString(2, dateTo);
			ps.setInt(3, idClient);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String res = rs.getInt("clientId") + " ";
				res += rs.getInt(2) + " ";
				res += rs.getString(3) + " ";
				res += rs.getDouble(4) + " ";
				res += rs.getString(5) + " ";
				res += rs.getString(6);
				result.add(res);
				System.out.println(res);
			}
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	@Transactional(
			rollbackFor = SQLException.class)
	private void insertAccount(double sum, int idClient) throws SQLException{
		Connection conn = dataSource.getConnection();
		
		PreparedStatement ps = conn.prepareStatement(INSERT_ACCOUNT);
		
		ps.setInt(1,  idClient);
		ps.setDouble(2, sum);
		
		ps.executeUpdate();
	}
	
	private int getLatestCreatedAccount(int clientId) {
		int accountId = -1;
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_LATEST_CREATED_ACCOUNT)){
			ps.setInt(1, clientId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			accountId = rs.getInt("id");
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return accountId;
	}
	
	@Transactional(
			rollbackFor = SQLException.class)
	private void insertTransaction(
			int clientId, 
			int accountId,
			int type,
			double sum, 
			int currency) throws SQLException{
		Connection conn = dataSource.getConnection();
		
		PreparedStatement ps = conn.prepareStatement(INSERT_RUB_TRANSACTION);
		
		ps.setInt(1,  clientId);
		ps.setInt(2, accountId);
		ps.setInt(3,  type);
		ps.setDouble(4, sum);
		ps.setInt(5, currency);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		ps.setString(6, dateFormat.format(date).toString());
		
		ps.executeUpdate();
	}

}
