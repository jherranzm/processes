package telefonica.aaee.capture977;

public class Split977Config {

	/** Es el valor del CIF del registro 100000 */
	private String acuerdo;

	private boolean borrarTablas;
	private boolean borrarAcuerdo;
	private boolean detalleLlamadas;
	private boolean detalleLlamadasRI;

	private String directorioOut;
	private String directorioZipFiles;

	static final String COMILLAS_DOBLES = "\"";

	final static String TAB = "\t";

	final static String CRLF = "\r\n";

	final static String CODIFICACION_FICHERO_ORIGEN = "ISO-8859-1";

	public Split977Config() {
	}

	public String getAcuerdo() {
		return acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}

	public boolean isBorrarTablas() {
		return borrarTablas;
	}

	public void setBorrarTablas(boolean borrarTablas) {
		this.borrarTablas = borrarTablas;
	}

	public boolean isBorrarAcuerdo() {
		return borrarAcuerdo;
	}

	public void setBorrarAcuerdo(boolean borrarAcuerdo) {
		this.borrarAcuerdo = borrarAcuerdo;
	}

	public boolean isDetalleLlamadas() {
		return detalleLlamadas;
	}

	public void setDetalleLlamadas(boolean detalleLlamadas) {
		this.detalleLlamadas = detalleLlamadas;
	}

	public boolean isDetalleLlamadasRI() {
		return detalleLlamadasRI;
	}

	public void setDetalleLlamadasRI(boolean detalleLlamadasRI) {
		this.detalleLlamadasRI = detalleLlamadasRI;
	}

	public String getDirectorioOut() {
		return directorioOut;
	}

	public void setDirectorioOut(String directorioOut) {
		this.directorioOut = directorioOut;
	}

	public String getDirectorioZipFiles() {
		return directorioZipFiles;
	}

	public void setDirectorioZipFiles(String directorioZipFiles) {
		this.directorioZipFiles = directorioZipFiles;
	}

	public static class Builder {
		private String acuerdo;
		private boolean borrarTablas;
		private boolean borrarAcuerdo;
		private boolean detalleLlamadas;
		private boolean detalleLlamadasRI;
		private String directorioOut;
		private String directorioZipFiles;

		public Builder acuerdo(String acuerdo) {
			this.acuerdo = acuerdo;
			return this;
		}

		public Builder borrarTablas(boolean borrarTablas) {
			this.borrarTablas = borrarTablas;
			return this;
		}

		public Builder borrarAcuerdo(boolean borrarAcuerdo) {
			this.borrarAcuerdo = borrarAcuerdo;
			return this;
		}

		public Builder detalleLlamadas(boolean detalleLlamadas) {
			this.detalleLlamadas = detalleLlamadas;
			return this;
		}

		public Builder detalleLlamadasRI(boolean detalleLlamadasRI) {
			this.detalleLlamadasRI = detalleLlamadasRI;
			return this;
		}

		public Builder directorioOut(String directorioOut) {
			this.directorioOut = directorioOut;
			return this;
		}

		public Builder directorioZipFiles(String directorioZipFiles) {
			this.directorioZipFiles = directorioZipFiles;
			return this;
		}

		public Split977Config build() {
			return new Split977Config(this);
		}
	}

	private Split977Config(Builder builder) {
		this.acuerdo = builder.acuerdo;
		this.borrarTablas = builder.borrarTablas;
		this.borrarAcuerdo = builder.borrarAcuerdo;
		this.detalleLlamadas = builder.detalleLlamadas;
		this.detalleLlamadasRI = builder.detalleLlamadasRI;
		this.directorioOut = builder.directorioOut;
		this.directorioZipFiles = builder.directorioZipFiles;
	}
}