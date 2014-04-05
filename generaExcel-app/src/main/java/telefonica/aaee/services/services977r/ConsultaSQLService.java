/**
 * 
 */
package telefonica.aaee.services.services977r;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import telefonica.aaee.model.model977r.ConsultaSQL;

/**
 * @author Usuario
 *
 */
public class ConsultaSQLService 
	extends GenericService {

	private static final String PU_977R = "977R";
	private EntityManagerFactory factory;
	private EntityManager entityManager;
	
    private static final Logger LOGGER = Logger.getLogger(ConsultaSQLService.class.getCanonicalName());

    public ConsultaSQLService(){
		factory = Persistence.createEntityManagerFactory(PU_977R);
		entityManager = factory.createEntityManager();
	}
    
	public ConsultaSQLService(EntityManager em) {
		entityManager = em;
	}
	
	public ConsultaSQL getConsultaSQLById(long id){
		ConsultaSQL cf = null;
		String sql = "";
		
		try {
			sql = "select u from ConsultaSQL u WHERE u.id = :id";
			Query q = entityManager.createQuery(sql);
			cf = (ConsultaSQL)q.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("id:" + id);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return cf;
	}
	
	public List<ConsultaSQL> getAllItems(){
		return getAllConsultasSQL();
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsultaSQL> getAllConsultasSQL(){
		List<ConsultaSQL> cf = new ArrayList<ConsultaSQL>();
		String sql = "";
		
		try {
			sql = "select u from ConsultaSQL u";
			Query q = entityManager.createQuery(sql);
			cf = (List<ConsultaSQL>)q.getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return cf;
	}
	
	public ConsultaSQL save(ConsultaSQL pce){
		
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(pce);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return pce;
	}

	public ConsultaSQL getConsultaSQLByNombre(String pNombre){
		ConsultaSQL cf = null;
		String sql = "";
		
		try {
			sql = "select u from ConsultaSQL u WHERE u.nombre = :nombre";
			Query q = entityManager.createQuery(sql);
			cf = (ConsultaSQL)q.setParameter("nombre", pNombre).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("nombre:" + pNombre);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return cf;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsultaSQL> getConsultasSQLByNombre(String pNombre){
		List<ConsultaSQL> cfs = null;
		String sql = "";
		
		try {
			sql = "select u from ConsultaSQL u WHERE u.nombre LIKE :nombre";
			Query q = entityManager.createQuery(sql);
			cfs = (List<ConsultaSQL>)q.setParameter("nombre", "%" + pNombre + "%").getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("nombre:" + pNombre);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return cfs;
	}

}
