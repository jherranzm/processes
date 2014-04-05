package telefonica.aaee.services.services977r;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import telefonica.aaee.model.model977r.Acuerdo977R;

/**
 * @author Usuario
 *
 */
public class Acuerdo977RService 
	extends GenericService {
	
	private static final String PU_977R = "977R";
	private EntityManagerFactory factory;
	private EntityManager em;

    private static final Logger LOGGER = Logger.getLogger(Acuerdo977RService.class.getCanonicalName());

    public Acuerdo977RService(){
		LOGGER.info("Constructor genérico de Acuerdo977R Service...");
		factory = Persistence.createEntityManagerFactory(PU_977R);
		em = factory.createEntityManager();
	}
    
	public Acuerdo977RService(EntityManager em) {
		LOGGER.info("Constructor específico de Acuerdo977R Service...");
		this.em = em;
	}
	
	@SuppressWarnings("unchecked")
	public List<Acuerdo977R> getAllItems(){
		
		List<Acuerdo977R> acuerdos = new ArrayList<Acuerdo977R>();
		String sql = "";
		
		try {
			//sql = "select u from Acuerdo977R u";
			//sql = "select u from Acuerdo977R u GROUP BY u.acuerdo";
			sql = "select u from Acuerdo977R u";
			LOGGER.info("SQL:" + sql);
			Query q = em.createQuery(sql);
			List<Acuerdo977R> acuerdosAll = (List<Acuerdo977R>)q.getResultList();
			
			Set<String> setAcuerdos = new TreeSet<String>();
			for(Acuerdo977R acuerdo : acuerdosAll){
				setAcuerdos.add(acuerdo.getAcuerdo());
			}
//			List<String> results = (List<String>)q.getResultList();
			for (String result : setAcuerdos) {
			    String str = (String) result;
			    Acuerdo977R acuerdo = new Acuerdo977R();
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
	
	public int callAddIncludeAll(String acuerdo){
		int ret = -1;
		Connection con = null;
		
		if(acuerdo == null) return -1;
		if("".equals(acuerdo)) return -2;
		
		String sql = "CALL 977R_ADDINCLUDE_ALL( ? )";
		try {
			em.getTransaction().begin();
			con = em.unwrap(java.sql.Connection.class);
			if(con == null) return -3;
			CallableStatement cs = con.prepareCall("{"+sql+"}");
			cs.setString(1, acuerdo);
			cs.execute();
			ret = 1;
			em.getTransaction().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e); 
		}
		
		return ret;
	}
	
	public int copyCondicionesAcuerdo(String acuerdo, String acuerdoAnt){
		int ret = -1;
		Connection con = null;
		
		if(acuerdo == null || acuerdoAnt == null) return -1;
		if("".equals(acuerdo) || "".equals(acuerdoAnt)) return -2;
		
		String sql = "CALL 977R_COPY_ESCENARIO_ByAcuerdo( ?, ? )";
		try {
			em.getTransaction().begin();
			con = em.unwrap(java.sql.Connection.class);
			if(con == null) return -3;
			CallableStatement cs = con.prepareCall("{"+sql+"}");
			cs.setString(1, acuerdo);
			cs.setString(2, acuerdoAnt);
			cs.execute();
			ret = 1;
			em.getTransaction().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e); 
		}
		
		return ret;
	}
	
	public int aplicarCondicionesAcuerdo(String acuerdo){
		int ret = -1;
		Connection con = null;
		
		if(acuerdo == null) return -1;
		if("".equals(acuerdo)) return -2;
		
		String sql = "CALL 977R_SP_APPLY_COND_ALL( ? )";
		try {
			em.getTransaction().begin();
			con = em.unwrap(java.sql.Connection.class);
			if(con == null) return -3;
			CallableStatement cs = con.prepareCall("{"+sql+"}");
			cs.setString(1, acuerdo);
			cs.execute();
			ret = 1;
			em.getTransaction().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e); 
		}
		
		return ret;
	}
	
	public int actualizarTablaResumenAcuerdo(String acuerdo){
		int ret = -1;
		Connection con = null;
		
		if(acuerdo == null) return -1;
		if("".equals(acuerdo)) return -2;
		
		String sql = "CALL 977R_SP_UPDATE_TABLA_RESUMEN( ? )";
		try {
			em.getTransaction().begin();
			con = em.unwrap(java.sql.Connection.class);
			if(con == null) return -3;
			CallableStatement cs = con.prepareCall("{"+sql+"}");
			cs.setString(1, acuerdo);
			cs.execute();
			ret = 1;
			em.getTransaction().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e); 
		}
		
		return ret;
	}
	
	
	
	public int generaTablaResumen(String acuerdo){
		int ret = -1;
		Connection con = null;
		
		if(acuerdo == null) return -1;
		if("".equals(acuerdo)) return -2;
		
		String sql = "CALL 977R_SP_UPDATE_TABLA_RESUMEN( ? )";
		try {
			em.getTransaction().begin();
			con = em.unwrap(java.sql.Connection.class);
			if(con == null) return -3;
			CallableStatement cs = con.prepareCall("{"+sql+"}");
			cs.setString(1, acuerdo);
			cs.execute();
			ret = 1;
			em.getTransaction().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e); 
		}
		
		return ret;
	}
	
}
