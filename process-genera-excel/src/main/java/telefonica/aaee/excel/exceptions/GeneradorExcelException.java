/**
 *
 */
package telefonica.aaee.excel.exceptions;

import java.util.logging.Logger;

/**
 * @author t130796
 *
 * 19/01/2009
 *
 */
public class GeneradorExcelException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -1620793929292501066L;
	private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
	/**
	 * Constructor
	 *
	 */
	public GeneradorExcelException() {
		super();
	}
	/**
	 * Constructor
	 *
	 * @param message
	 */
	public GeneradorExcelException(String message) {
		super(message);
		logger.info(message);
	}
}
