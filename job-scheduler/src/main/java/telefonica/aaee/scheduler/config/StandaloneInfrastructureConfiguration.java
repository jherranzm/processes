package telefonica.aaee.scheduler.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@EnableBatchProcessing
@ComponentScan(value ={
		"telefonica.aaee.scheduler.config",
		"telefonica.aaee.dao.config"
		})
@PropertySource("classpath:application.properties")
public class StandaloneInfrastructureConfiguration {

	private static final String DB_PASSWORD = "db.password.jobs";
	private static final String DB_USERNAME = "db.username.jobs";
	private static final String DB_URL = "db.url.jobs";
	private static final String DB_DRIVER = "db.driver";

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource
    private Environment environment;

	@Autowired
	private JpaTransactionManager transactionManager;

	@Autowired
	private DataSource dataSource;
	
	
	@Bean
	public JobRepository getJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.afterPropertiesSet();
		return  (JobRepository) factory.getObject();
	}
	
	
	
	@Bean
	public JobLauncher getJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}
	
	
}
