/**
 * 
 */
package telefonica.aaee.services.services977r;

import javax.sql.rowset.CachedRowSet;

/**
 * @author t130796
 * 
 */
public interface IDB977RDAO {

    CachedRowSet getCachedRowSetFromSQL(
    		String sql, 
    		String params[]);
	public int getNumRegistros(
			String sql, 
			String[] params);
}
