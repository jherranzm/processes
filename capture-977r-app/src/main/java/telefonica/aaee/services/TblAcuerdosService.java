package telefonica.aaee.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import telefonica.aaee.model.datos.TblAcuerdos;

public class TblAcuerdosService 
	extends GenericService{
	
	private static final String PU_977R = "977R";
	private EntityManagerFactory factory;
	private EntityManager em;

    private static final Logger LOGGER = Logger.getLogger(TblAcuerdosService.class.getCanonicalName());

    public TblAcuerdosService(){
		LOGGER.info("Constructor genérico de TblAcuerdos Service...");
		factory = Persistence.createEntityManagerFactory(PU_977R);
		em = factory.createEntityManager();
	}
    
	public TblAcuerdosService(EntityManager em) {
		LOGGER.info("Constructor específico de TblAcuerdos Service...");
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public List<TblAcuerdos> getAllItems(){
		
		List<TblAcuerdos> acuerdos = new ArrayList<TblAcuerdos>();
		String sql = "";
		
		try {
			//sql = "select u from Acuerdo977R u";
			//sql = "select u from Acuerdo977R u GROUP BY u.acuerdo";
			sql = "select u from TblAcuerdos u";
			LOGGER.info("SQL:" + sql);
			Query q = em.createQuery(sql);
			List<TblAcuerdos> acuerdosAll = (List<TblAcuerdos>)q.getResultList();
			
			Set<String> setAcuerdos = new TreeSet<String>();
			for(TblAcuerdos acuerdo : acuerdosAll){
				setAcuerdos.add(acuerdo.getAcuerdo());
			}
//			List<String> results = (List<String>)q.getResultList();
			for (String result : setAcuerdos) {
			    String str = (String) result;
			    TblAcuerdos acuerdo = new TblAcuerdos();
			    acuerdo.setAcuerdo(str);
			    acuerdos.add(acuerdo);
			}
			LOGGER.info("Número de registros:" + acuerdos.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return acuerdos;
	}
	
	public boolean save(TblAcuerdos i) {
		
		em.getTransaction().begin();
		em.persist(i);	
		em.getTransaction().commit();
		
		return true;
	}
	
	/**
	 * @param acuerdo
	 * @return
	 */
	public TblAcuerdos getByAcuerdo(String acuerdo){
		TblAcuerdos unAcuerdo = null;
		String sql = "";
		
		try {
			sql = "select u from TblAcuerdos u WHERE u.acuerdo = :elAcuerdo";
			Query q = em.createQuery(sql);
			unAcuerdo = (TblAcuerdos)q.setParameter("elAcuerdo", acuerdo).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("acuerdo:" + acuerdo);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return unAcuerdo;
	}
	
	
}
