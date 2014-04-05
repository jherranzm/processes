package telefonica.aaee.services.services977r;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import telefonica.aaee.model.model977r.Informe;


public class InformeService  
	extends GenericService {
	
	private static final String PU_977R = "977R";
	private EntityManagerFactory factory;
	private EntityManager em;
	
    private static final Logger LOGGER = Logger.getLogger(InformeService.class.getCanonicalName());

    /**
     * 
     */
    public InformeService(){
    	
		LOGGER.setLevel(Level.INFO);
		
    	LOGGER.debug("Creamos la Factoría de Persistencia...");
    	factory = Persistence.createEntityManagerFactory(PU_977R);
    	LOGGER.debug("Creamos un EntityManager...");
		em = factory.createEntityManager();
    	LOGGER.debug("El entityManager está abierto? " + em.isOpen());
	}
    
    /**
     * 
     * @param em
     */
	public InformeService(EntityManager em) {
		this.em = em;
	}	
	
	/**
	 * 
	 * @return
	 */
	public EntityManager getEntityManager() {
		return em;
	}
	
	

	/**
	 * 
	 * @param nombre
	 * @return
	 */
	public Informe getInformeByNombre(String nombre) {
		Informe informe = null;
		String sql = "";
		
		try {
			sql = "SELECT u FROM Informe u WHERE u.nombre = :nombre";
			Query q = em.createQuery(sql);
			informe = (Informe)q.setParameter("nombre", nombre).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("sql:" + sql);
			LOGGER.warn("nombre:" + nombre);
			LOGGER.warn("exception.message:" + e.getMessage());
		}
				
		return informe;
	}

	/**
	 * 
	 * @param nombre
	 * @return
	 */
	public Informe getInformesByNombre(String nombre) {
		Informe informe = null;
		String sql = "";
		
		try {
			sql = "SELECT u FROM Informe u WHERE u.nombre LIKE :nombre";
			Query q = em.createQuery(sql);
			informe = (Informe)q.setParameter("nombre", "%" + nombre + "%").getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("sql:" + sql);
			LOGGER.warn("nombre:" + nombre);
			LOGGER.warn("exception.message:" + e.getMessage());
		}
				
		return informe;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Informe getInformeById(int id) {
		Informe informe = null;
		String sql = "";
		
		try {
			sql = "SELECT u FROM Informe u WHERE u.id = :id";
			Query q = em.createQuery(sql);
			informe = (Informe)q.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("sql:" + sql);
			LOGGER.warn("id:" + id);
			LOGGER.warn("exception.message:" + e.getMessage());
		}
				
		return informe;
	}
	
	/**
	 * 
	 * @param informe
	 * @return
	 */
	public Informe save(Informe informe){
		
		Informe ret = null;
		
		try {
			em.getTransaction().begin();
			em.persist(informe);
			em.getTransaction().commit();
			ret = informe;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("exception.message:" + e.getMessage());
		}
		
		return ret;
	}

	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return
	 */
	public List<Informe> getAllItems(){
		
		List<Informe> informes = new ArrayList<Informe>();
		String sql = "";
		
		try {
			sql = "SELECT u FROM Informe u";
			Query q = em.createQuery(sql);
			informes = (List<Informe>)q.getResultList();
			LOGGER.info("Número de registros:" + informes.size());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("sql:" + sql);
			LOGGER.warn("exception.message:" + e.getMessage());
		}
				
		return informes;
	}

}
