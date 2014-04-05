package telefonica.aaee.services.services977r;

import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class PersistenceManager {
	 
	  public static final boolean DEBUG = true;
	  private static final Logger LOGGER = Logger.getLogger(PersistenceManager.class.getCanonicalName());
	  
	  private static final PersistenceManager singleton = new PersistenceManager();
	  
	  protected EntityManagerFactory emf;
	  
	  public static PersistenceManager getInstance() {
	    
	    return singleton;
	  }
	  
	  private PersistenceManager() {
	  }
	 
	  public EntityManagerFactory getEntityManagerFactory() {
	    
	    if (emf == null)
	      createEntityManagerFactory();
	    return emf;
	  }
	  
	  public void closeEntityManagerFactory() {
	    
	    if (emf != null) {
	      emf.close();
	      emf = null;
	      if (DEBUG)
	        LOGGER.info("n*** Persistence finished at " + new java.util.Date());
	    }
	  }
	  
	  protected void createEntityManagerFactory() {
	    
	    this.emf = Persistence.createEntityManagerFactory("977R");
	    if (DEBUG)
	      LOGGER.info("n*** Persistence started at " + new java.util.Date());
	  }
	}