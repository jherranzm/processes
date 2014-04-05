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

import telefonica.aaee.model.model977r.Pestanya;

/**
 * @author Usuario
 *
 */
public class PestanyaService 
	extends GenericService {

	private static final String PU_977R = "977R";
	EntityManagerFactory factory;
	EntityManager em;
	
    private static final Logger LOGGER = Logger.getLogger(PestanyaService.class.getCanonicalName());

    
    /**
     * 
     */
    public PestanyaService(){
		factory = Persistence.createEntityManagerFactory(PU_977R);
		em = factory.createEntityManager();
	}
    
	/**
	 * @param em
	 */
	public PestanyaService(EntityManager em) {
		this.em = em;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public Pestanya getPestanyaById(long id){
		Pestanya cf = null;
		String sql = "";
		
		try {
			sql = "select u from Pestanya u WHERE u.id = :id";
			Query q = em.createQuery(sql);
			cf = (Pestanya)q.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("id:" + id);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return cf;
	}

	
	
	/**
	 * @return todos los objetos Pestanya que hay en la tabla
	 */
	public List<Pestanya> getAllItems(){
		return getAllPestanyes();
	}
	
	/**
	 * @return todos los objetos Pestanya que hay en la tabla
	 */
	@SuppressWarnings("unchecked")
	public List<Pestanya> getAllPestanyes(){
		List<Pestanya> cf = new ArrayList<Pestanya>();
		String sql = "";
		
		try {
			sql = "select u from Pestanya u";
			Query q = em.createQuery(sql);
			cf = (List<Pestanya>)q.getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return cf;
	}
	
	/**
	 * @param pce
	 * @return
	 */
	public Pestanya save(Pestanya pce){
		
		try {
			em.getTransaction().begin();
			em.persist(pce);
			em.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return pce;
	}

	/**
	 * @param pNombre
	 * @return
	 */
	public Pestanya getPestanyaByNombre(String pNombre){
		Pestanya cf = null;
		String sql = "";
		
		try {
			sql = "select u from Pestanya u WHERE u.nombre = :nombre";
			Query q = em.createQuery(sql);
			cf = (Pestanya)q.setParameter("nombre", pNombre).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("nombre:" + pNombre);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return cf;
	}
	
	/**
	 * @param pNombre
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Pestanya> getPestanyesByNombre(String pNombre){
		List<Pestanya> lista = null;
		String sql = "";
		
		try {
			sql = "select u from Pestanya u WHERE u.nombre LIKE :nombre";
			Query q = em.createQuery(sql);
			lista = (List<Pestanya>)q.setParameter("nombre", "%" + pNombre + "%").getResultList();
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("nombre:" + pNombre);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return lista;
	}
	

}
