package telefonica.aaee.exceptions;

import java.util.logging.Logger;

public class ElFicheroYaExisteException extends Exception {

	private static final Logger LOGGER = Logger.getLogger(ElFicheroYaExisteException.class.getCanonicalName());

	private String mensaje = "";
	
	public ElFicheroYaExisteException(
			String nombreFicheroOriginal,
			String fechaFactura, 
			String cifActual000000) {
		
		this.setMensaje("El fichero " + nombreFicheroOriginal + " de fecha " + fechaFactura + " ya está cargado!!");
		LOGGER.severe(this.getMensaje());
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	private static final long serialVersionUID = 1L;
	
	

}
