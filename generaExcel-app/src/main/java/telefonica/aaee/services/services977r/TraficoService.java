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

import telefonica.aaee.model.model977r.Trafico;

/**
 * @author Usuario
 *
 */
public class TraficoService 
	extends GenericService {

	private static final String PU_977R = "977R";
	EntityManagerFactory factory;
	EntityManager em;
	
    private static final Logger LOGGER = Logger.getLogger(TraficoService.class.getCanonicalName());

	public TraficoService() {
		LOGGER.info("Constructor genérico de Tráfico Service...");
		factory = Persistence.createEntityManagerFactory(PU_977R);
		em = factory.createEntityManager();
	}
	

	public TraficoService(EntityManager em) {
		LOGGER.info("Constructor específico de Tráfico Service...");
		this.em = em;
	}
	
	/**
	 * 
	 * @param id
	 * @return el Concepto Facturable con identificativo id
	 */
	public Trafico getCFById(long id){
		Trafico cf = null;
		String sql = "";
		
		try {
			sql = "select u from Trafico u WHERE u.id = :id";
			Query q = em.createQuery(sql);
			cf = (Trafico)q.setParameter("id", id).getSingleResult();
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
	public List<Trafico> getCFByAcuerdo(String acuerdo){
		List<Trafico> lista = new ArrayList<Trafico>();
		String sql = "";
		
		try {
			sql = "select u from Trafico u WHERE u.acuerdo = :elAcuerdo";
			Query q = em.createQuery(sql);
			lista = (List<Trafico>)q.setParameter("elAcuerdo", acuerdo).getResultList();
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
	public List<Trafico> getCFByAcuerdoPendientesRevisar(String acuerdo){
		List<Trafico> lista = new ArrayList<Trafico>();
		String sql = "";
		
		try {
			sql = "select u from Trafico u WHERE u.acuerdo = :elAcuerdo AND u.precio_especial = 'REVISAR'";
			Query q = em.createQuery(sql);
			lista = (List<Trafico>)q.setParameter("elAcuerdo", acuerdo).getResultList();
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
	public List<Trafico> findByName(String acuerdo, String concepto){
		List<Trafico> lista = new ArrayList<Trafico>();
		String sql = "";
		
		try {
			sql = "select u from Trafico u WHERE u.acuerdo = :elAcuerdo AND LOWER(u.descTrafico) LIKE :elConcepto ";
			Query q = em.createQuery(sql);
			lista = (List<Trafico>)q.setParameter("elAcuerdo", acuerdo).setParameter("elConcepto", concepto).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	public List<Trafico> getAllItems(){
		List<Trafico> lista = new ArrayList<Trafico>();
		String sql = "";
		
		try {
			sql = "select u from Trafico u";
			Query q = em.createQuery(sql);
			lista = (List<Trafico>)q.getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return lista;
	}


	public boolean save(Trafico i) {
		
		em.getTransaction().begin();
		em.merge(i);
		em.getTransaction().commit();
		
		return true;
	}
	
	public boolean delete(Trafico i) {
		
		em.getTransaction().begin();
		em.remove(i);
		em.getTransaction().commit();
		
		return true;
	}


	@SuppressWarnings("unchecked")
	public List<Trafico> findByAcuerdo(String acuerdo) {
		List<Trafico> lista = new ArrayList<Trafico>();
		String sql = "";
		
		try {
			sql = "select u from Trafico u WHERE u.acuerdo = :elAcuerdo ";
			Query q = em.createQuery(sql);
			lista = (List<Trafico>)q.setParameter("elAcuerdo", acuerdo).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("acuerdo:" + acuerdo);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return lista;
	}
}
