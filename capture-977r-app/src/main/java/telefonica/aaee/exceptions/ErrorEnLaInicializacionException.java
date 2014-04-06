/**
 * 
 */
package telefonica.aaee.exceptions;

import java.util.logging.Logger;
/**
 * @author Usuario
 *
 */
public class ErrorEnLaInicializacionException extends Exception {

	private static final Logger LOGGER = Logger.getLogger(ErrorEnLaInicializacionException.class.getCanonicalName());

	public ErrorEnLaInicializacionException(String mensaje) {
		System.err.println(mensaje);
		LOGGER.severe(mensaje);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
