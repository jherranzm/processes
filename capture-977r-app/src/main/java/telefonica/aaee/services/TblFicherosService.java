package telefonica.aaee.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import telefonica.aaee.model.datos.TblFicheros;

public class TblFicherosService 
	extends GenericService{
	
	private static final String PU_977R = "977R";
	private EntityManagerFactory factory;
	private EntityManager em;

    private static final Logger LOGGER = Logger.getLogger(TblFicherosService.class.getCanonicalName());

    public TblFicherosService(){
		LOGGER.info("Constructor genérico de TblFicheros Service...");
		factory = Persistence.createEntityManagerFactory(PU_977R);
		em = factory.createEntityManager();
	}
    
	public TblFicherosService(EntityManager em) {
		LOGGER.info("Constructor específico de TblFicheros Service...");
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public List<TblFicheros> getAllItems(){
		
		List<TblFicheros> ficheros = new ArrayList<TblFicheros>();
		String sql = "";
		
		try {
			//sql = "select u from Acuerdo977R u";
			//sql = "select u from Acuerdo977R u GROUP BY u.acuerdo";
			sql = "select u from TblFicheros u";
			LOGGER.info("SQL:" + sql);
			Query q = em.createQuery(sql);
			ficheros = (List<TblFicheros>)q.getResultList();
			
			LOGGER.info("Número de registros:" + ficheros.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return ficheros;
	}
	
	public boolean save(TblFicheros i) {
		
		em.getTransaction().begin();
		em.persist(i);	
		em.getTransaction().commit();
		
		return true;
	}
	
	/**
	 * @param fichero
	 * @return
	 */
	public TblFicheros getByFichero(String fichero){
		TblFicheros unFichero = null;
		String sql = "";
		
		try {
			sql = "select u from TblFicheros u WHERE u.fichero = :elfichero";
			Query q = em.createQuery(sql);
			unFichero = (TblFicheros)q.setParameter("elfichero", fichero).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("fichero:" + fichero);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return unFichero;
	}
	
	
	/**
	 * @param fichero
	 * @return
	 */
	public TblFicheros getByFicheroFechaFactura(String fichero, String fechaFactura){
		TblFicheros unFichero = null;
		String sql = "";
		
		try {
			sql = "select u from TblFicheros u WHERE u.fichero = :elfichero and u.fechaFactura = :laFecha";
			Query q = em.createQuery(sql);
			unFichero = (TblFicheros)q
					.setParameter("elfichero", fichero)
					.setParameter("laFecha", fechaFactura)
					.getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warning("sql:" + sql);
			LOGGER.warning("fichero:" + fichero);
			LOGGER.warning("laFecha:" + fechaFactura);
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return unFichero;
	}
	
	/**
	 * @param fichero
	 * @return
	 */
	public TblFicheros getByFicheroFechaFacturaCif(
			final String fichero, final String fechaFactura, final String cifSupra){
		
		TblFicheros unFichero = null;
		String sql = "";
		
		try {
			sql = "select u " +
					"from TblFicheros u " +
					"WHERE " +
					"u.fichero = :elfichero " +
					"and u.fechaFactura = :laFecha " +
					"and u.cifSupraCliente = :elCif " +
					" ";
			Query q = em.createQuery(sql);
			unFichero = (TblFicheros)q
					.setParameter("elfichero", fichero)
					.setParameter("laFecha", fechaFactura)
					.setParameter("elCif", cifSupra)
					.getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			LOGGER.warning("sql:[" + sql + "]");
			LOGGER.warning("fichero:[" + fichero + "]");
			LOGGER.warning("laFecha:[" + fechaFactura + "]");
			LOGGER.warning("elCif:[" + cifSupra + "]");
			LOGGER.warning("exception.message:" + e.getMessage());
		}
				
		return unFichero;
	}
	
	
}
