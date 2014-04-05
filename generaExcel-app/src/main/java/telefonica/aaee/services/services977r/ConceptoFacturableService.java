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

import telefonica.aaee.model.model977r.ConceptoFacturable;

/**
 * @author Usuario
 *
 */
public class ConceptoFacturableService 
	extends GenericService {

	private static final String PU_977R = "977R";
	EntityManagerFactory factory;
	EntityManager em;
	
    private static final Logger LOGGER = Logger.getLogger(ConceptoFacturableService.class.getCanonicalName());

	public ConceptoFacturableService() {
		LOGGER.info("Constructor por defecto...");
		factory = Persistence.createEntityManagerFactory(PU_977R);
		em = factory.createEntityManager();
	}
	

	public ConceptoFacturableService(EntityManager em) {
		LOGGER.info("Constructor con parámetro EntityManager...");
		this.em = em;
	}
	
	/**
	 * 
	 * @param id
	 * @return el Concepto Facturable con identificativo id
	 */
	public ConceptoFacturable getCFById(long id){
		ConceptoFacturable cf = null;
		String sql = "";
		
		try {
			sql = "select u " +
					"from ConceptoFacturable u " +
					"WHERE u.id = :id";
			Query q = em.createQuery(sql);
			cf = (ConceptoFacturable)q.setParameter("id", id).getSingleResult();
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
	 * @param acuerdo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturable> getCFByAcuerdo(String acuerdo){
		List<ConceptoFacturable> lista = new ArrayList<ConceptoFacturable>();
		String sql = "";
		
		try {
			sql = "select u " +
					"from ConceptoFacturable u " +
					"WHERE u.acuerdo = :elAcuerdo";
			Query q = em.createQuery(sql);
			lista = (List<ConceptoFacturable>)q.setParameter("elAcuerdo", acuerdo).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("acuerdo:" + acuerdo);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return lista;
	}

	
	/**
	 * @param acuerdo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturable> getCFByAcuerdoPendientesRevisar(String acuerdo){
		List<ConceptoFacturable> lista = new ArrayList<ConceptoFacturable>();
		String sql = "";
		
		try {
			sql = "select u " +
					"from ConceptoFacturable u " +
					"WHERE u.acuerdo = :elAcuerdo AND u.precio_especial = 'REVISAR'";
			Query q = em.createQuery(sql);
			lista = (List<ConceptoFacturable>)q.setParameter("elAcuerdo", acuerdo).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("acuerdo:" + acuerdo);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return lista;
	}

	/**
	 * @param acuerdo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturable> findByName(String acuerdo, String concepto){
		List<ConceptoFacturable> lista = new ArrayList<ConceptoFacturable>();
		String sql = "";
		
		try {
			sql = "select u from ConceptoFacturable u " +
					"WHERE u.acuerdo = :elAcuerdo AND LOWER(u.descConceptoFacturable) LIKE :elConcepto ";
			Query q = em.createQuery(sql);
			lista = (List<ConceptoFacturable>)q.setParameter("elAcuerdo", acuerdo).setParameter("elConcepto", concepto).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("acuerdo:" + acuerdo);
			LOGGER.warning("concepto:" + concepto);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return lista;
	}
	
	/**
	 * @param acuerdo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturable> getAllItems(){
		List<ConceptoFacturable> lista = new ArrayList<ConceptoFacturable>();
		String sql = "";
		
		try {
			sql = "select u " +
					"from ConceptoFacturable u";
			Query q = em.createQuery(sql);
			lista = (List<ConceptoFacturable>)q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return lista;
	}



	public boolean save(ConceptoFacturable i) {
		
		em.getTransaction().begin();
		em.merge(i);
		em.getTransaction().commit();
		
		return true;
	}
	
	public boolean delete(ConceptoFacturable i) {
		
		em.getTransaction().begin();
		em.remove(i);
		em.getTransaction().commit();
		
		return true;
	}


	@SuppressWarnings("unchecked")
	public List<ConceptoFacturable> findByAcuerdo(String acuerdo) {
		List<ConceptoFacturable> lista = new ArrayList<ConceptoFacturable>();
		String sql = "";
		
		try {
			sql = "select u " +
					"from ConceptoFacturable u " +
					"WHERE u.acuerdo = :elAcuerdo ";
			Query q = em.createQuery(sql);
			lista = (List<ConceptoFacturable>)q.setParameter("elAcuerdo", acuerdo).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("acuerdo:" + acuerdo);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return lista;
	}
}
