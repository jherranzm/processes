/**
 * 
 */
package telefonica.aaee.services.services977r;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author t130796
 *
 */
public class MySQLCloseConnection {

    private static final Logger LOGGER = Logger.getLogger(MySQLCloseConnection.class.getCanonicalName());
    
    
    
    
    public MySQLCloseConnection() {
		super();
		LOGGER.setLevel(Level.INFO);
		
	}

	/**
     * 
     * @param con
     */
    public void closeAll(  Connection con){
        try {
            if (con != null) {
        	con.close();
            }
        } catch (SQLException e) {
            LOGGER.fatal( "ERROR:SQLException: " + e.getErrorCode() + ":" + e.getMessage());
        }
    }

    /**
     * 
     * @param con
     * @param preparedStatement
     */
    public void closeAll( 
             Connection con, 
             PreparedStatement preparedStatement){
        try {
            if (preparedStatement != null) {
        	preparedStatement.close ();
            }
            if (con != null) {
        	con.close();
            }
        } catch (SQLException e) {
            LOGGER.fatal( "ERROR:SQLException: " + e.getErrorCode() + ":" + e.getMessage());
        }
    }

    /**
     * 
     * @param con
     * @param preparedStatement
     * @param resultSet
     */
    public void closeAll( 
             Connection con, 
             PreparedStatement preparedStatement, 
             ResultSet resultSet){
        try {
            if (resultSet != null){
            	LOGGER.debug("Cerramos el ResultSet...");
            	resultSet.close();
            }
            if (preparedStatement != null) {
            	LOGGER.debug("Cerramos el PreparedStatement...");
            	preparedStatement.close ();
            }
            if (con != null) {
            	LOGGER.debug("Cerramos el Connection...");
            	con.close();
            }
        } catch (SQLException e) {
            LOGGER.fatal( "ERROR:SQLException: " + e.getErrorCode() + ":" + e.getMessage());
        }
    }

    
}
