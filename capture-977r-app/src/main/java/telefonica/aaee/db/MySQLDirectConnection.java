/**
 * 
 */
package telefonica.aaee.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author Usuario
 *
 */
public class MySQLDirectConnection 
	implements DbConnection {
	
	private static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private String dbHost
					, dbName 
					, dbUser 
					, dbPass 
					;
	
	

	public MySQLDirectConnection(String dbHost, String dbName, String dbUser, String dbPass) {
		super();
		this.dbHost = dbHost;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
	}


	/* (non-Javadoc)
	 * @see telefonica.aaee.db.DbConnection#getConnection()
	 */
	@Override
	public Connection getConnection() {
		Connection conn = null;
		
		try {
			Class.forName(COM_MYSQL_JDBC_DRIVER);
			conn = DriverManager.getConnection("jdbc:mysql://" + getDbHost() + "/" + getDbName() + ""
					, getDbUser()
					, getDbPass()
					);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e); 
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e); 
		}
		
		return conn;
	}

	
	/**
	 * @return dbHost
	 */
	public final String getDbHost() {
		return dbHost;
	}

	
	/**
	 * @param dbHost valor a asignar al campo dbHost
	 */
	public final void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	
	/**
	 * @return dbName
	 */
	public final String getDbName() {
		return dbName;
	}

	
	/**
	 * @param dbName valor a asignar al campo dbName
	 */
	public final void setDbName(String dbName) {
		this.dbName = dbName;
	}

	
	/**
	 * @return dbUser
	 */
	public final String getDbUser() {
		return dbUser;
	}

	
	/**
	 * @param dbUser valor a asignar al campo dbUser
	 */
	public final void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	
	/**
	 * @return dbPass
	 */
	public final String getDbPass() {
		return dbPass;
	}

	
	/**
	 * @param dbPass valor a asignar al campo dbPass
	 */
	public final void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

}
