package configpack;

import javax.sql.DataSource;

import org.apache.derby.jdbc.ClientDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import daopack.BankDaoImplBD;

@Configuration
@EnableTransactionManagement
public class BankConfiguration {
    @Bean
    public BankDaoImplBD bankDao(DataSource dataSource, TransactionTemplate transactionTemplate) {
    	BankDaoImplBD bankDao = new BankDaoImplBD();
    	bankDao.setDataSource(dataSource);
    	return bankDao;
    }
    
    @Bean
    public DataSource dataSource(){
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	dataSource.setDriverClassName(ClientDriver.class.getName());
    	dataSource.setUrl("jdbc:sqlite:resources/bank.db");
    	return dataSource;
    }
    
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    	DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
    	transactionManager.setDataSource(dataSource);
    	return transactionManager;
    }
    
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager){
    	TransactionTemplate transactionTemplate = new TransactionTemplate();
    	transactionTemplate.setTransactionManager(transactionManager);
    	return transactionTemplate;
    }
	
}
