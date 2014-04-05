package telefonica.aaee.informes.init;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration //Specifies the class as configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "977rEntityManagerFactory", 
        transactionManagerRef = "977rTransactionManager",
        basePackages = {"telefonica.aaee.informes.repository.db977r"}
        )
@EnableWebMvc //Enables to use Spring's annotations in the code
@ComponentScan("telefonica.aaee.informes") //Specifies which package to scan

@PropertySource("classpath:application.properties")

public class JPA977Config extends WebMvcConfigurationSupport {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String DATABASE_DRIVER = "db.driver";
	private static final String DATABASE_URL_977R = "db.url.977r";
    private static final String DATABASE_USERNAME_977R = "db.username.977r";
    private static final String DATABASE_PASSWORD_977R = "db.password.977r";

	@Resource
    private Environment environment;
	
	
	@Bean(name = "977rDataSource")
	   public DataSource dataSource(){
	      DriverManagerDataSource dataSource = new DriverManagerDataSource();
	      dataSource.setDriverClassName(environment.getRequiredProperty(DATABASE_DRIVER));
	      dataSource.setUrl(environment.getRequiredProperty(DATABASE_URL_977R));
	      dataSource.setUsername( environment.getRequiredProperty(DATABASE_USERNAME_977R) );
	      dataSource.setPassword( environment.getRequiredProperty(DATABASE_PASSWORD_977R) );
	      
	      return dataSource;
	   }

    @Bean(name = "977rEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() 
    		throws ClassNotFoundException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan("telefonica.aaee.informes.model");
        em.setJpaProperties(additionalProperties());
        em.setPersistenceUnitName("JPAInformesWebApp");
        
        JpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        logger.info("***** 977rEntityManagerFactory!");
        
        return em;
    }
    
    @Bean(name = "977rTransactionManager")
    public JpaTransactionManager transactionManager() throws ClassNotFoundException {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());

		logger.info("***** 977rTransactionManager!");

        return transactionManager;
    }


    Properties additionalProperties() {
        return new Properties() {
			private static final long serialVersionUID = 1L;

		{  // Hibernate Specific: 
              setProperty("eclipselink.weaving", "false");
           }
        };
     }
}
