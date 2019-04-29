package daopack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class BankDaoImplBD implements BankDao{
	private static final String INSERT_CLIENT = "INSERT INTO Client (name) VALUES(?)";
	
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
	public void makeAccount(double sum, int idClient) {}

}
