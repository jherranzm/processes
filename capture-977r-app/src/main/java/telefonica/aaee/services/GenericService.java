package telefonica.aaee.services;

import java.util.logging.Logger;

import javax.persistence.EntityManager;


public class GenericService {

	private static final Logger LOGGER = Logger.getLogger(GenericService.class.getCanonicalName());
	protected EntityManager em;

	public GenericService() {
	}
	
	public GenericService(EntityManager em) {
		LOGGER.info("Constructor con parámetros: EntityManager");
		this.em = em;
	}


}
