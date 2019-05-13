package configpack;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.apache.derby.jdbc.ClientDriver;

import daopack.BankDao;
import daopack.BankDaoImplBD;

@Configuration
@EnableTransactionManagement
public class BankConfiguration {
    @Bean
    public BankDao bankDao() {
    	return new BankDaoImplBD(dataSource());
    }
    
    @Bean
    public DataSource dataSource(){
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	dataSource.setDriverClassName(ClientDriver.class.getName());
    	dataSource.setUrl("jdbc:sqlite:resources/bank.db");
    	return dataSource;
    }
}
