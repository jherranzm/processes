/**
 * 
 */
package telefonica.aaee.services.services977r;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import telefonica.aaee.model.model977r.PlantaCondEspecial;

/**
 * @author Usuario
 *
 */
public class PlantaCondEspecialService 
	extends GenericService {

	private static final String PU_977R = "977R";
	EntityManagerFactory factory;
	EntityManager em;
	
    private static final Logger LOGGER = Logger.getLogger(PlantaCondEspecialService.class.getCanonicalName());

    public PlantaCondEspecialService(){
		factory = Persistence.createEntityManagerFactory(PU_977R);
		em = factory.createEntityManager();
	}
    
	public PlantaCondEspecialService(EntityManager em) {
		super.em = em;
	}
	
	public PlantaCondEspecial getCFById(long id){
		PlantaCondEspecial cf = null;
		String sql = "";
		
		try {
			sql = "select u from PlantaCondEspecial u WHERE u.id = :id";
			Query q = em.createQuery(sql);
			cf = (PlantaCondEspecial)q.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("id:" + id);
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
				
		return cf;
	}
	
	public PlantaCondEspecial save(PlantaCondEspecial pce){
		
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
	
	public boolean deleteByAcuerdo(String paramAcuerdo){
		boolean ret = false;
		try {
			String sql = "DELETE FROM PlantaCondEspecial p WHERE p.acuerdo = :acuerdo";
			Query query = em.createQuery(sql);
			query.setParameter("acuerdo", paramAcuerdo);
			em.getTransaction().begin();
			int numRegs = query.executeUpdate();
			em.getTransaction().commit();
			LOGGER.info("Registros eliminados: " + numRegs);
			ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("exception.message:" + e.getMessage());
			
		}
		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
