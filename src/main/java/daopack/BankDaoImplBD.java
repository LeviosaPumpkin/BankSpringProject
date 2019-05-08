package daopack;

import java.util.Date;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

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
	
	private final DataSource dataSource;
	
	public BankDaoImplBD(DataSource ds) {
		dataSource = ds;
	}
	
	public void addClient(String name) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(INSERT_CLIENT)){
			ps.setString(1,  name);
			ps.executeUpdate();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void makeDeposit(double sum, int idClient, int idAccount) {}
	public void makeWithdraw(double sum, int idClient, int idAccount) {}
	public void makeAccount(double sum, int idClient) {
		if(sum < 0) {
			System.out.println("Opening balance cannot be negative");
			return;
		}
		insertAccount(sum, idClient);
		int idAccount = getLatestCreatedAccount(idClient);
		insertTransaction(idClient, idAccount, Type.OPEN.getId(), sum, Currency.RUB.getId());
	}

	private void insertAccount(double sum, int idClient) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(INSERT_ACCOUNT)){
			ps.setInt(1,  idClient);
			ps.setDouble(2, sum);
			ps.executeUpdate();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
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
	
	private void insertTransaction(
			int clientId, 
			int accountId,
			int type,
			double sum, 
			int currency) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(INSERT_RUB_TRANSACTION)){
			ps.setInt(1,  clientId);
			ps.setInt(2, accountId);
			ps.setInt(3,  type);
			ps.setDouble(4, sum);
			ps.setInt(5, currency);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			ps.setString(6, dateFormat.format(date).toString());
			ps.executeUpdate();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
