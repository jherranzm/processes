package telefonica.aaee.segmentacion.config;

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

@Configuration //Specifies the class as configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "maestrasEntityManagerFactory", 
        transactionManagerRef = "maestrasTransactionManager",
        basePackages = {"telefonica.aaee.segmentacion"}
        )
//@EnableWebMvc //Enables to use Spring's annotations in the code
@ComponentScan("telefonica.aaee.segmentacion") //Specifies which package to scan
@PropertySource("classpath:application.properties")
public class JPAMaestrasConfig 
	//extends WebMvcConfigurationSupport 
	{

	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String DATABASE_DRIVER = "db.driver";
	private static final String DATABASE_URL_MAESTRAS = "db.url.maestras";
    private static final String DATABASE_USERNAME_MAESTRAS = "db.username.maestras";
    private static final String DATABASE_PASSWORD_MAESTRAS = "db.password.maestras";

	@Resource
    private Environment environment;
	
	
	@Bean(name = "maestrasDataSource")
	   public DataSource dataSource(){
		
	      DriverManagerDataSource dataSource = new DriverManagerDataSource();
	      
	      dataSource.setDriverClassName(environment.getRequiredProperty(DATABASE_DRIVER));
	      dataSource.setUrl(environment.getRequiredProperty(DATABASE_URL_MAESTRAS));
	      dataSource.setUsername( environment.getRequiredProperty(DATABASE_USERNAME_MAESTRAS) );
	      dataSource.setPassword( environment.getRequiredProperty(DATABASE_PASSWORD_MAESTRAS) );
	      
	      return dataSource;
	   }

    @Bean(name = "maestrasEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() 
    		throws ClassNotFoundException {
    	
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan("telefonica.aaee.segmentacion.model");
        em.setJpaProperties(additionalProperties());
        em.setPersistenceUnitName("JPASegmentacionApp");
        
        JpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        logger.info("***** maestrasEntityManagerFactory!");
        
        return em;
    }
    
    @Bean(name = "maestrasTransactionManager")
    public JpaTransactionManager transactionManager() throws ClassNotFoundException {
    	
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());

		logger.info("***** maestrasTransactionManager!");

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
