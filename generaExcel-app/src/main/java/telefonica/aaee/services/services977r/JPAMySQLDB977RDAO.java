/**
 *
 */
package telefonica.aaee.services.services977r;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import telefonica.aaee.exceptions.NoSeHaObtenidoConexionDelEntityManager;
import telefonica.aaee.utils.ConstantsREG;

/**
 * @author t130796
 * 
 */
public class JPAMySQLDB977RDAO 

	extends MySQLCloseConnection
	implements IDB977RDAO {

	private static final Logger LOGGER = Logger.getLogger(JPAMySQLDB977RDAO.class.getCanonicalName());
	private static final String PU_977R = "977R";
	
	private EntityManagerFactory factory;
	
	
	
	
	
	
	

	public JPAMySQLDB977RDAO() {
		super();
		LOGGER.setLevel(Level.INFO);
		
    	LOGGER.debug("Creamos la Factoría de Persistencia...");
    	factory = Persistence.createEntityManagerFactory(PU_977R);
	}



	@Override
	public CachedRowSet getCachedRowSetFromSQL(
			String sql, 
			String[] params) {
		
		CachedRowSet crs = null;

		try {
			LOGGER.debug("Inicio getCachedRowSetFromSQL ...");
	    	LOGGER.debug("Creamos un EntityManager...");
	    	
	    	EntityManager em = factory.createEntityManager();
	    	LOGGER.debug("El entityManager está abierto? " + em.isOpen());

	    	em.getTransaction().begin();

			LOGGER.debug("Recuperamos la conexión del EntityManager... ");
			Connection con = em.unwrap(java.sql.Connection.class);
			if(con == null) throw new NoSeHaObtenidoConexionDelEntityManager();
			LOGGER.debug("Ya tenemos la conexión del EntityManager...!");

			LOGGER.debug("Creamos el CachedRowSet... ");
			crs = CachedRowSetFactory.getCachedRowSet();

			LOGGER.debug("La consulta a ejecutar, sql:" + sql);
			String sqlTrimmed = sql.trim();
			sqlTrimmed = sqlTrimmed.substring(0, 4).toLowerCase();
			LOGGER.debug("sqlTrimmed:[" + sqlTrimmed + "]");
			
			if("call".equals(sqlTrimmed)){
				CallableStatement cs = con.prepareCall("{"+sql+"}");
				for (int x = 0; x < params.length; x++) {
					cs.setString(x + 1, params[x]);
					LOGGER.debug("params["+x+"]:" + params[x]);
				}
				LOGGER.debug("Tenemos la consulta y los parámetros...");
				cs.execute();
				crs.populate(cs.getResultSet());
				
				LOGGER.debug("Se han recuperado " + crs.size() + " registros!");
				
				
			}else{

				crs.setCommand(sql);
				LOGGER.debug("params.length:" + params.length);
				for (int x = 0; x < params.length; x++) {
					if(!params[x].equals("")) crs.setString(x + 1, params[x]);
					LOGGER.debug("params["+x+"]:#" + params[x] + "#");
				}
				crs.execute(con);
				
				LOGGER.debug("Se han recuperado " + crs.size() + " registros!");
				
				
			}
			LOGGER.debug("Cerramos la transacción...");
			em.getTransaction().commit();
			LOGGER.debug("Cerramos el entityManager...");

		} catch(NoSeHaObtenidoConexionDelEntityManager e){
			LOGGER.fatal(ConstantsREG.SEP_VERTICAL);
			LOGGER.fatal("ERROR:NoSeHaObtenidoConexionDelEntityManager.");
			
		} catch (SQLException e) {
			LOGGER.fatal(ConstantsREG.SEP_VERTICAL);
			LOGGER.fatal("ERROR:SQLException: " + e.getMessage());
			LOGGER.fatal("ERROR:SQLState: " + e.getSQLState());
			LOGGER.fatal("ERROR:VendorError: " + e.getErrorCode());
			
		} catch (Exception e) {
			LOGGER.fatal(ConstantsREG.SEP_VERTICAL + "ERROR:Exception:"
					+ e.getMessage());
		} finally {
			LOGGER.debug("Fin!"+ConstantsREG.SEP_VERTICAL);
		}

		return crs;
	}




	@Override
	public int getNumRegistros(
//				EntityManager em, 
				String sql, 
				String[] params) {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int ret = 0;
		try {
			LOGGER.debug("Inicio getNumRegistros ...");

	    	LOGGER.debug("Creamos un EntityManager...");
			EntityManager em = factory.createEntityManager();
	    	LOGGER.debug("El entityManager está abierto? " + em.isOpen());
			em.getTransaction().begin();
			
			LOGGER.debug("Recuperamos la conexión del EntityManager... ");
			con = em.unwrap(java.sql.Connection.class);
			if(con == null) throw new NoSeHaObtenidoConexionDelEntityManager();
			LOGGER.debug("Ya tenemos la conexión del EntityManager...!" + con.getMetaData().toString());
			
			preparedStatement = con.prepareStatement(sql);
			for (int x = 0; x < params.length; x++) {
				preparedStatement.setString(x + 1, params[x]);
				LOGGER.debug("params["+x+"]:" + params[x]);
			}

			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				if (resultSet.next()) {
					ret = resultSet.getInt(1);
				}
			}

			LOGGER.debug("Cerramos la transacción...");
			em.getTransaction().commit();
			LOGGER.debug("Cerramos el entityManager...");
			em.close();
			LOGGER.debug("Fin getNumRegistros ...");
			
		} catch(NoSeHaObtenidoConexionDelEntityManager e){
			LOGGER.fatal(ConstantsREG.SEP_VERTICAL);
			LOGGER.fatal("ERROR:NoSeHaObtenidoConexionDelEntityManager.");
			
			ret = -1;

		} catch (SQLException e) {
			LOGGER.fatal(ConstantsREG.SEP_VERTICAL);
			LOGGER.fatal("ERROR:SQLException: " + e.getMessage());
			LOGGER.fatal("ERROR:SQLState: " + e.getSQLState());
			LOGGER.fatal("ERROR:VendorError: " + e.getErrorCode());
			
			ret = -1;
		} catch (Exception e) {
			LOGGER.fatal(ConstantsREG.SEP_VERTICAL + "ERROR:Exception:"
					+ e.getMessage());
			ret = -2;
		} finally {
		}

		return ret;
	}

}
