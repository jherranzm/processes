package telefonica.aaee.exceptions;

import java.util.logging.Logger;

public class NoSeHaPodidoGuardarElFichero extends Exception {

	private static final Logger LOGGER = Logger.getLogger(NoSeHaPodidoGuardarElFichero.class.getCanonicalName());

	private String mensaje = "";
	
	public NoSeHaPodidoGuardarElFichero(
			String nombreFicheroOriginal,
			String fechaFactura, 
			String cifActual000000) {
		
		this.setMensaje("El fichero " + nombreFicheroOriginal + " de fecha " + fechaFactura + " no se ha podido guardar en la tabla.");
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
