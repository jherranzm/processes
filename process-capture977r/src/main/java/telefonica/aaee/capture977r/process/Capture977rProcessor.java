/**
 * 
 */
package telefonica.aaee.capture977r.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telefonica.aaee.capture977r.exceptions.ElFicheroYaExisteException;
import telefonica.aaee.capture977r.exceptions.ErrorEnLaInicializacionException;
import telefonica.aaee.capture977r.exceptions.NoSeHaPodidoGuardarElFichero;
import telefonica.aaee.capture977r.util.Mensajes;
import telefonica.aaee.dao.model.Acuerdo;
import telefonica.aaee.dao.model.Fichero;
import telefonica.aaee.dao.model.TipoRegistroFactel5;
import telefonica.aaee.dao.service.AcuerdoService;
import telefonica.aaee.dao.service.FicheroService;
import telefonica.aaee.dao.service.TipoRegistroFactel5Service;
import telefonica.aaee.util.UtilNombre;
import telefonica.aaee.util.zip.Unzip977RFile;

/**
 * @author t130796
 * 
 */
@Service
public class Capture977rProcessor {

	protected final Log logger = LogFactory.getLog(getClass());

	private int[] posiciones = { 25, 1, 15, 4, 3, 1, 3 };

	private Hashtable<String, TipoRegistro> registros = new Hashtable<String, TipoRegistro>();
	private Hashtable<String, File> filesOut = new Hashtable<String, File>();
	private Hashtable<String, BufferedWriter> bwOut = new Hashtable<String, BufferedWriter>();
	private Hashtable<String, String> codigoRegistroExistente = new Hashtable<String, String>();
	private Hashtable<String, Vector<String>> camposPorTabla = new Hashtable<String, Vector<String>>();

	private SortedMap<String, Long> numRegistros = new TreeMap<String, Long>();
	private Hashtable<String, TipoRegistroFactel5> tipoRegistrosFactel5 = new Hashtable<String, TipoRegistroFactel5>();

	/*
	 * private int[][] pos_000000 = { {6,8,8,12,12,12,20,20,20,4},
	 * {40,18,70,30,20,5,8,70}, {2,65,40,40,7,40}, {12,5,7,8,5},
	 * {56,30,8,8,1,10} };
	 */
	private String fechaFactura = "";
	private String nombreFicheroOriginal = "";
	private String cifActual = "";
	private long idAcuerdo = -1;
	private long idFichero = -1;
	private String[] ficheros = {};

	//private List<String> sqlQueries = new ArrayList<String>();
	private LinkedHashSet<String> sqlQueries = new LinkedHashSet<String>();
	private List<String> resultados = new ArrayList<String>();

	private Split977Config config = null; // new Split977Config("", false, true,
											// false, false, null);
	private long tiempoEmpleado = 0;

	@Autowired
	private AcuerdoService acuerdoService;

	@Autowired
	private FicheroService ficheroService;

	@Autowired
	private TipoRegistroFactel5Service tipoRegistroService;

	public Capture977rProcessor() {
		super();
	}

	/**
	 * 
	 */
	public String execute() {
		
		resultados = new ArrayList<String>();
		numRegistros = new TreeMap<String, Long>();

		StringBuilder sb = new StringBuilder();

		Calendar ini = Calendar.getInstance();
		long lIni = ini.getTimeInMillis();
		sb.append(lIni);
		
		List<String> listaFicheros = new ArrayList<String>();

		try {

			if (!init(sb)) {
				throw new ErrorEnLaInicializacionException(
						"No se ha podido crear el directorio de trabajo!");
			}


			sb.append("Acuerdo: ").append(getConfig().getAcuerdo())
					.append(Split977Config.CRLF);
			logger.info(sb.toString());

			// Crea el acuerdo en la tabla, si no existe y retorna el Id del acuerdo.
			creaAcuerdo();

			// Descomprime los ficheros que hay en el directorio
			listaFicheros = descomprimirFicheros();

			logger.info(String.format("Número de ficheros a tratar: [%d]",
					listaFicheros.size()));

			// Se localiza si los ficheros están o no cargados actualmente
			listaFicheros = obtieneInformacionFicheros(listaFicheros);
			
			if(listaFicheros.size() > 0){
				obtieneEstructuraRegistros(listaFicheros);

				creaSQLCreateTables();
				
				for (String fichero : listaFicheros) {

					FileInputStream fis = new FileInputStream(new File(fichero));
					InputStreamReader isr = new InputStreamReader(fis,
							Split977Config.CODIFICACION_FICHERO_ORIGEN);
					BufferedReader in = new BufferedReader(isr);

					try {
						procesaFichero(in);
					} catch (ElFicheroYaExisteException e) {
						sb.append("El fichero ya existe:" + e.getMensaje());
					} catch (NoSeHaPodidoGuardarElFichero e) {
						sb.append("No se ha podido guardar el fichero:" + e.getMensaje());
						e.printStackTrace();
					}
				}

				creaSQLLoadDataInfile();
				
				for(String sql : sqlQueries){
					logger.info(sql);
				}

				
				cerrarFicherosSalida();

				ejecutarSentenciasSQL();

				getInfoTablas(sb);
				
				
			}

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			sb.append("FileNotFoundException:" + e.getMessage());

		} catch (IOException e) {
			sb.append("Error de Entrada/Salida: " + e.getMessage());
			e.printStackTrace();
		} catch (ErrorEnLaInicializacionException e) {
			sb.append("Error En La Inicialización: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			sb.append("Exception:" + e.getMessage());
			e.printStackTrace();
		} finally {
			//HelperFile.cleandirs(getConfig().getDirectorioOut());

			Calendar fin = Calendar.getInstance();
			long lFin = fin.getTimeInMillis();
			sb.append((lFin - lIni) + Split977Config.CRLF);

			setTiempoEmpleado(lFin - lIni);
			
			logger.info("Se han tratado " + listaFicheros.size() + " ficheros.");
			resultados.add("Se han tratado " + listaFicheros.size() + " ficheros.");
			for(String fichero : listaFicheros){
				logger.info(fichero);
				resultados.add(fichero);
			}
			for(String tipoRegistro : numRegistros.keySet()){
				logger.info(numRegistros.get(tipoRegistro) + " registros : " + tipoRegistro + ":" + tipoRegistrosFactel5.get(tipoRegistro).getDescripcion());
				resultados.add(numRegistros.get(tipoRegistro) + " registros : " + tipoRegistro + ":" + tipoRegistrosFactel5.get(tipoRegistro).getDescripcion());
			}

			resultados.add(String.format("Tiempo total de proceso:[%d] segundos.", (lFin-lIni)/1000));
			

		}

		return sb.toString();

	}

	private void creaAcuerdo() {
		Acuerdo elAcuerdo = new Acuerdo();
		elAcuerdo.setAcuerdo(getConfig().getAcuerdo());
		Acuerdo elAcuerdoGuardado = acuerdoService.findByNombre(getConfig()
				.getAcuerdo());
		if (elAcuerdoGuardado == null) {
			elAcuerdo.setId(0L);
			elAcuerdoGuardado = acuerdoService.create(elAcuerdo);
			logger.info(String.format("Se ha creado el acuerdo : %s",
					elAcuerdoGuardado.toString()));
		}

		idAcuerdo = elAcuerdoGuardado.getId();
	}

	/**
	 * 
	 * Lee los ficheros que se quieren tratar, localiza el nombre, y 
	 * en caso de que el fichero no esté tratado, guarda el registro para 
	 * procesarlo.
	 * 
	 * @param listaFicheros
	 * @return lista de ficheros 
	 */
	private List<String> obtieneInformacionFicheros(List<String> listaFicheros){
		
		List<String> ficherosDefinitivos = new ArrayList<String>();
		
		for (String fichero : listaFicheros) {

			logger.info(String.format("Fichero: [%s]", fichero));

			try {

				FileInputStream fis = new FileInputStream(new File(fichero));
				InputStreamReader isr = new InputStreamReader(fis,
						Split977Config.CODIFICACION_FICHERO_ORIGEN);
				BufferedReader in = new BufferedReader(isr);

				String line = in.readLine();
				while (true) {
					if (line == null) {
						break;
					}

					if (line.startsWith("000000")) {
						//tratarRegistro000000(line);
						obtieneInfoRegistro000000(line);
						ficherosDefinitivos.add(fichero);
						break;
					}
				}
				
				in.close();
			} catch(ElFicheroYaExisteException e){
				logger.error(String.format(Mensajes.FILE_ALREADY_PROCESSED, fichero));
			} catch (NoSeHaPodidoGuardarElFichero e) {
				logger.error(String.format(Mensajes.FILE_NOT_SAVED, fichero));
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				logger.error(String.format(Mensajes.FILE_NOT_EXISTS, fichero));
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				logger.error(String.format(Mensajes.FILE_UNSUPORTED_ENCODING, fichero));
				e.printStackTrace();
			} catch (IOException e) {
				logger.error(String.format(Mensajes.FILE_IO_ERROR, fichero));
				e.printStackTrace();
			}


		}
		
		return ficherosDefinitivos;
	}

	private void obtieneEstructuraRegistros(List<String> listaFicheros)
			throws FileNotFoundException, UnsupportedEncodingException {
		for (String fichero : listaFicheros) {

			logger.info(String.format("Fichero: [%s]", fichero));

			FileInputStream fis = new FileInputStream(new File(fichero));
			InputStreamReader isr = new InputStreamReader(fis,
					Split977Config.CODIFICACION_FICHERO_ORIGEN);
			BufferedReader in = new BufferedReader(isr);

			getEstructuraRegistros(in);

		}
	}

	/**
	 * 
	 * Se descomprimen los ficheros .zip del directorio, y los descomprimidos se
	 * guardan en un lista.
	 * 
	 * @return
	 */
	private List<String> descomprimirFicheros() {
		List<String> listaFicheros = new ArrayList<String>();

		for (String fichero : ficheros) {
			fichero = FilenameUtils.normalize(fichero);
			String extension = FilenameUtils.getExtension(fichero);

			if (extension.toLowerCase().equals("zip")) {
				Unzip977RFile uz = new Unzip977RFile();
				String ficheroDescomprimido = uz.unzip(fichero, getConfig()
						.getDirectorioOut());
				ficheroDescomprimido = FilenameUtils
						.normalize(ficheroDescomprimido);
				logger.info(String.format("Fichero descomprimido:[%s]",
						ficheroDescomprimido));
				listaFicheros.add(ficheroDescomprimido);
			} else {
				logger.error(String.format(
						"El fichero [%s] NO es un fichero .ZIP!!", fichero));
			}

		}
		return listaFicheros;
	}

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	private void ejecutarSentenciasSQL() throws FileNotFoundException,
			IOException, SQLException {
		
		
		for(String sql : sqlQueries){
			acuerdoService.executeSQLQuery(sql);
		}

	}

	/**
	 * @param sb
	 */
	private void getInfoTablas(StringBuilder sb) {
		sb.append("Número de tablas:" + camposPorTabla.size()
				+ Split977Config.CRLF);

		Vector<String> v = new Vector<String>(camposPorTabla.keySet());
		Collections.sort(v);
		for (String nombreTabla : v) {
			sb.append("Nombre tabla:" + nombreTabla + Split977Config.CRLF);
			Vector<String> nomCampos = camposPorTabla.get(nombreTabla);
			for (String nomCampo : nomCampos) {
				sb.append(Split977Config.TAB + nomCampo + Split977Config.CRLF);
			}

		}
	}


	/**
	 * @param sb
	 */
	private boolean init(StringBuilder sb) {
		boolean ret = true;
		String[] aProps = { 
				"java.class.path"
				, "java.ext.dirs"
				, "java.library.path"
				, "path.separator" };

		for (String aProp : aProps) {
			sb
			.append(aProp)
			.append(":  [").append(System.getProperty(aProp)).append("]")
			.append(Split977Config.CRLF);
		}
		sb
			.append("directorio:       [").append(getConfig().getDirectorioOut()).append("]")
			.append(Split977Config.CRLF);
		
		getConfig().setDirectorioOut(
				FilenameUtils.normalize(getConfig().getDirectorioOut() + File.separator
						+ UtilNombre.nombreAleatorio())
				);

		boolean existeDir = (new File(getConfig().getDirectorioOut())).exists();
		if (existeDir) {
			logger.info("El directorio:" + getConfig().getDirectorioOut()
					+ " ya existe!");
		} else {
			File outDirName = new File(getConfig().getDirectorioOut());
			if (outDirName.mkdirs()) {
				logger.info("El directorio:" + getConfig().getDirectorioOut()
						+ " se ha creado con éxito!");
			} else {
				System.err.println("Error al crear el directorio:"
						+ getConfig().getDirectorioOut());
				ret = false;
			}
		}

		if (config == null) {
			System.err
					.println("No se han especificado las condiciones iniciales: ACUERDO, etc...");
			ret = false;
		}
		
		for(TipoRegistroFactel5 tr : tipoRegistroService.findAll()){
			tipoRegistrosFactel5.put(tr.getTipoRegistro(), tr);
		}
		
		return ret;
	}

	/**
	 * Se incluyen en una lista los comandos que generarán las tablas, en el
	 * caso de que éstas NO existan.
	 * 
	 * Hay un atributo de la clase que indica si se ha de incluir o no la
	 * eliminación de tablas (DROP TABLE). Por defecto es FALSE.
	 * 
	 */
	private void creaSQLCreateTables() {

		if (getConfig().isBorrarTablas()) {
			StringBuilder SQLDropTableDefT000000 = new StringBuilder(
					"DROP TABLE IF EXISTS 977r.t000000;");

			sqlQueries.add(SQLDropTableDefT000000.toString());
		}

		StringBuilder SQLCreateTableDefT000000 = new StringBuilder();
		SQLCreateTableDefT000000
				.append("CREATE TABLE IF NOT EXISTS 977r.t000000 (")
				.append("	id int(11) NOT NULL AUTO_INCREMENT, ")
				.append("	fichero VARCHAR(12) NOT NULL, ")
				.append("	FECHA_FACTURA VARCHAR(8) NOT NULL, ")
				.append("	CIF_CLIENTE_Clave VARCHAR(18) NOT NULL, ")
				.append("	ACUERDO VARCHAR(18) NOT NULL, ")
				.append("	IDACUERDO int(11) NOT NULL ")
				.append(",	PRIMARY KEY (id)")
				.append(",	KEY (fichero, FECHA_FACTURA)")
				.append(")	ENGINE=MyISAM;");

		sqlQueries.add(SQLCreateTableDefT000000.toString());

		Set<String> sKeys = registros.keySet();
		Vector<String> vKeys = new Vector<String>(sKeys);
		Collections.sort(vKeys);

		for (Iterator<String> i = vKeys.iterator(); i.hasNext();) {
			String codigoRegistro = (String) i.next();

			// El registro 702010 es el detalle de llamadas...
			if (codigoRegistro.equals("702010")
					&& !getConfig().isDetalleLlamadas()) {
				// no hacemos nada... sin detalle

				// El registro 702010 es el detalle de llamadas de RED
				// INTELIGENTE...
			} else if (codigoRegistro.equals("702020")
					&& !getConfig().isDetalleLlamadasRI()) {
				// no hacemos nada... sin detalle

			} else {

				TipoRegistro tr = registros.get(codigoRegistro);
				// recorremos cada Bloque

				// Se utiliza una tabla para
				Vector<String> nombresCampos = new Vector<String>();

				for (int iBloque = 0; iBloque < tr.getNumBloques(); iBloque++) {
					Bloque bloque = tr.getBloques()[iBloque];
					EstructuraCampo[] estructura = bloque.getEstructuras();

					StringBuilder SQLDropTableDef = null;
					if (getConfig().isBorrarTablas()) {
						SQLDropTableDef = new StringBuilder(
								"DROP TABLE IF EXISTS 977r.t" + codigoRegistro + "_"
										+ (iBloque + 1) + ";");
					}

					String nombreTabla = codigoRegistro + "_" + (iBloque + 1);

					StringBuilder SQLCreateTableDef = new StringBuilder();
					SQLCreateTableDef.append("CREATE TABLE IF NOT EXISTS 977r.t"
							+ codigoRegistro + "_" + (iBloque + 1) + " (");

					StringBuilder SQLCreateTableCamposComunes = new StringBuilder();
					SQLCreateTableCamposComunes
							.append(" fichero VARCHAR(12) NOT NULL")
							.append(" , CampoClave INT(11) NOT NULL ")
							.append(" , FECHA_FACTURA VARCHAR(8) NOT NULL ")
							.append(" , CIF_CLIENTE_Clave VARCHAR(18) NOT NULL ")
							.append(" , ACUERDO VARCHAR(18) NOT NULL ")
							.append(" , IDACUERDO INT(11) NOT NULL ")
							.append("");

					/*
					 * Utilizamos un vector para almacenar los nombres de los
					 * campos de las tablas
					 */
					Vector<String> camposEnTabla = new Vector<String>();
					camposEnTabla.clear();
					// campos comunes
					camposEnTabla.add("fichero");
					camposEnTabla.add("CampoClave");
					camposEnTabla.add("FECHA_FACTURA");
					camposEnTabla.add("CIF_CLIENTE_Clave");
					camposEnTabla.add("ACUERDO");
					camposEnTabla.add("IDACUERDO");

					StringBuilder SQLCreateTableDefCampos = new StringBuilder();

//					int ocurrencias = 0;
					for (int k = 0; k < bloque.getNumEstructuras(); k++) {

						/**
						 * Cambiamos los caracteres que pueden resultar
						 * complicados como nombres de campo en una tabla MySQL
						 */
						String nombreCampo = estructura[k].getNombreCampo()
								.trim()
								.replace(" ", "_")
								.replace("/_", "")
								.replace(".", "");

						int longCampo = (new Integer(
								estructura[k].getLongitudCampo())).intValue();

						if ("CODIGO_REGISTRO".equals(nombreCampo)) {
						} else if ("LONGITUD_REGISTRO".equals(nombreCampo)) {
						} else if ("SECUENCIAL".equals(nombreCampo)) {
						} else if (nombreCampo.startsWith("OCURR")) {

//							ocurrencias++;
						} else {

							// Guardamos el campo en caso de que no esté en
							// la lista
							String _nombreCampo = "t" + (iBloque + 1) + "."
									+ nombreCampo;
							if (!nombresCampos.contains(_nombreCampo)) {
								nombresCampos.add(_nombreCampo);

								camposEnTabla.add(nombreCampo);
							}

							SQLCreateTableDefCampos
								.append(", ").append(nombreCampo);

							/**
							 * Añadimos la longitud y características en función
							 * del Tipo de Campo
							 */
							if (estructura[k].getTipoCampo().equals("N")) {
								SQLCreateTableDefCampos
										.append(" INT(11) DEFAULT 0 ");

							} else if (estructura[k].getTipoCampo().equals("A")) {
								SQLCreateTableDefCampos.append(" VARCHAR("
										+ longCampo + ") DEFAULT NULL ");

							} else if (estructura[k].getTipoCampo().equals("I")) {
								// el contenido es numérico, ha de
								// considerarse como tal
								String formatoCampo = estructura[k]
										.getFormatoCampo().trim();
								StringTokenizer st = new StringTokenizer(
										formatoCampo, ",");
								String m = st.nextToken();
								int im = (new Integer(m)).intValue();
								String d = st.nextToken();
								int id = (new Integer(d)).intValue();
								im += id;
								formatoCampo = im + "," + id;
								SQLCreateTableDefCampos
										.append(" DOUBLE("+ formatoCampo + ") DEFAULT 0 ");

							} else if (estructura[k].getTipoCampo().equals("F")) {
								SQLCreateTableDefCampos
										.append(" DATE DEFAULT NULL ");

							} else if (estructura[k].getTipoCampo().equals("D")) {
								SQLCreateTableDefCampos
										.append(" VARCHAR(" + longCampo + ") DEFAULT NULL ");

							} else if (estructura[k].getTipoCampo().equals("H")) {
								SQLCreateTableDefCampos
										.append(" TIME DEFAULT NULL ");

							} else {
								logger.info(estructura[k].getTipoCampo()
										+ " No contemplado!");
							}// if

							/**
							 * Casos especiales en la tabla de detalle de
							 * llamadas Simulcom
							 */
							if (codigoRegistro.equals("702017")
									&& nombreCampo.equals("IMPORTE_VALOR_ANADIDO")
									&& iBloque == 0) {
								SQLCreateTableDefCampos
										.append(",  NUMERO_PARTICIPANTES_2 INT(11) DEFAULT 0 ");

								camposEnTabla.add("NUMERO_PARTICIPANTES_2");

							}

						}// if(nombreCampo.startsWith("OCURR"))

					}// for(int k = 0; k < bloque.getNumEstructuras(); k++)

					if (codigoRegistro.equals("901000") && iBloque == 1) {
						SQLCreateTableDefCampos
								.append(",  TEXTO VARCHAR(180) ");

						camposEnTabla.add("TEXTO");
					}
					SQLCreateTableDef
							.append(SQLCreateTableCamposComunes.toString())
							.append(SQLCreateTableDefCampos.toString())
							.append(", KEY (CampoClave)")
							.append(")  ENGINE=MyISAM;");

					// out.write(SQLDropTableTemp.toString() + CRLF);
					if (getConfig().isBorrarTablas()) {
						sqlQueries.add(SQLDropTableDef.toString());
					}
					// out.write(SQLCreateTableTemp + CRLF);
					sqlQueries.add(SQLCreateTableDef.toString());

					camposPorTabla.put(nombreTabla, camposEnTabla);

				}// for(int iBloque = 0; iBloque < tr.getNumBloques();
					// iBloque++)

			}// if(codigoRegistro.equals("702010") && detalleLlamadas)

		}// for(Iterator<String> i = vKeys.iterator(); i.hasNext(); )
	}

	/**
	 * 
	 */
	private void creaSQLLoadDataInfile() {

		String path = getConfig().getDirectorioOut();
		String loadDataInfile = "";

			StringBuilder sbQueryDeleteT000000 = new StringBuilder();

			sbQueryDeleteT000000.append("DELETE FROM 977r.t000000 WHERE ");
			sbQueryDeleteT000000.append(" fichero = '" + nombreFicheroOriginal + "' ");
			sbQueryDeleteT000000.append(" and  acuerdo = '" + getConfig().getAcuerdo() + "';");
			
			sqlQueries.add(sbQueryDeleteT000000.toString());
			
			StringBuilder sbQueryLDIT000000 = new StringBuilder();

			sbQueryLDIT000000.append("load data LOCAL infile '")
					.append(path)
					.append("/000000.txt' into table 977r.t000000 ")
					.append("fields terminated by ';' enclosed by '\"' ")
					.append("(fichero, FECHA_FACTURA, CIF_CLIENTE_Clave, ACUERDO, IDACUERDO); ");
			
			sqlQueries.add(sbQueryLDIT000000.toString());

			Set<String> sKeys = codigoRegistroExistente.keySet();
			Vector<String> vKeys = new Vector<String>(sKeys);
			Collections.sort(vKeys);

			for (Iterator<String> i = vKeys.iterator(); i.hasNext();) {
				String codigoRegistro = (String) i.next();

				if (codigoRegistro.equals("000000")) {
				} else if (codigoRegistro.equals("903000")) {
				} else if (codigoRegistro.equals("702010")
						&& !getConfig().isDetalleLlamadas()) {
				} else if (codigoRegistro.equals("702020")
						&& !getConfig().isDetalleLlamadasRI()) {
				} else {
					TipoRegistro tr = registros.get(codigoRegistro);
					// recorremos cada Bloque

					// Se utiliza una tabla para saber si se han utilizado o no
					// el nombre de campo
					Vector<String> nombresCampos = new Vector<String>();

					for (int iBloque = 0; iBloque < tr.getNumBloques(); iBloque++) {
						Bloque bloque = tr.getBloques()[iBloque];
						EstructuraCampo[] estructura = bloque.getEstructuras();

//						int ocurrencias = 0;
						for (int k = 0; k < bloque.getNumEstructuras(); k++) {

							String nombreCampo = estructura[k].getNombreCampo()
									.trim().replace(" ", "_").replace("/_", "")
									.replace(".", "");

							if (nombreCampo.startsWith("OCURR")) {
//								ocurrencias++;
							} else {

								// Guardamos el campo en caso de que no esté en
								// la lista
								String _nombreCampo = "t" + (iBloque + 1) + "."
										+ nombreCampo;
								if (!nombresCampos.contains(_nombreCampo))
									nombresCampos.add(_nombreCampo);

							}// if(nombreCampo.startsWith("OCURR"))

						}// for(int k = 0; k < bloque.getNumEstructuras(); k++)


						String strBloque = codigoRegistro + "_" + (iBloque + 1);

						if (codigoRegistro.equals("601010")
								|| codigoRegistro.equals("701010")) {

							String strBloque2 = "6701010" + "_" + (iBloque + 1);
							String nombreTabla = "701010" + "_" + (iBloque + 1);

							String strListaCampos = getListaCampos(nombreTabla);

							loadDataInfile = "load data LOCAL infile '"
									+ path
									+ "/"
									+ strBloque
									+ ".txt' into table 977r.t"
									+ strBloque2
									+ " fields terminated by  ';' enclosed by '\"' "
									+ strListaCampos + ";";

						} else if (!(codigoRegistro.equals("901000") && iBloque == 2)) {

							String strListaCampos = getListaCampos(strBloque);

							loadDataInfile = "load data LOCAL infile '"
									+ path
									+ "/"
									+ strBloque
									+ ".txt' into table 977r.t"
									+ strBloque
									+ " fields terminated by  ';' enclosed by '\"' "
									+ strListaCampos + ";";
						}
						sqlQueries.add(loadDataInfile);

					}

				}// if(codigoRegistro.equals("702010") && detalleLlamadas)

			}// for(Iterator<String> i = vKeys.iterator(); i.hasNext(); )
			
			sqlQueries.add("CALL 977r.Actualizar_TodasTablasAux(@param);");
			sqlQueries.add("CALL 977r.Actualizar_t901000_CuotasAperiodicas_v20120420('" + getConfig().getAcuerdo() + "');");
			sqlQueries.add("CALL 977r.977R_ADDINCLUDE_ALL('" + getConfig().getAcuerdo() + "');");

			/**
			 * Procesamos las consultas que hay en el fichero
			 * Capture977R.SQLQueries.xml
			 */
			// String[] comandos = {
			// "ActualizarTablas"
			// , "ActualizarCuotasAperiodicas"
			// , "IncluirDatos"
			// };
			//
			// for (String comm : comandos) {
			// sb.append("SQL:" + comm);
			// String s2 = sqlStatements.getProperty(comm);
			// sb.append(s2);
			// String s2_res = Pattern.compile("--ACUERDO--").matcher(s2)
			// .replaceAll(getConfig().getAcuerdo());
			// out2.write(s2_res);
			// out2.write("COMMIT;" + Split977Config.CRLF);
			// out2.flush();
			// }

	}

	/**
	 * @param nombreTabla
	 * @return
	 */
	private String getListaCampos(String nombreTabla) {

		ArrayList<String> camposEvitar = new ArrayList<String>();
		camposEvitar.add("CODIGO REGISTRO");
		camposEvitar.add("CODIGO_REGISTRO");
		camposEvitar.add("CODIGO REGISTRO EXT");
		camposEvitar.add("CODIGO_REGISTRO_EXT");
		camposEvitar.add("SECUENCIAL");
		camposEvitar.add("LONGITUD REGISTRO");
		camposEvitar.add("LONGITUD_REGISTRO");

		Vector<String> listaCampos = camposPorTabla.get(nombreTabla);
		String strListaCampos = " (";
		for (String _nombreCampo : listaCampos) {
			// No está entre los campos a evitar
			// logger.info(nombreTabla + ":" + _nombreCampo);
			if (camposEvitar.indexOf(_nombreCampo) == -1) {
				strListaCampos += _nombreCampo + ", ";
			}
		}
		strListaCampos = strListaCampos.substring(0,
				strListaCampos.length() - 2);
		strListaCampos += ") ";

		// logger.info(nombreTabla + ":" + strListaCampos);
		return strListaCampos;
	}

	/**
	 * 
	 * @param fileName
	 * @throws NoSeHaPodidoGuardarElFichero
	 * @throws ElFicheroYaExisteException
	 */
	private void procesaFichero(BufferedReader in)
			throws ElFicheroYaExisteException, NoSeHaPodidoGuardarElFichero {
		try {

			String line;
			line = in.readLine();
			while (true) {
				if (line == null) {
					break;
				}

				if (line.length() < 6) {
					break;
				}

				String codigoRegistro = line.substring(0, 6);

				if (codigoRegistroExistente.get(codigoRegistro) == null)
					codigoRegistroExistente.put(codigoRegistro, codigoRegistro);

				int secuencial = Integer.parseInt(line.substring(6, 6 + 8));

				if (codigoRegistro.equals("100000")) {
					// Extraemos el CIF
					cifActual = line.substring(129, 129 + 18);
					logger.info("cifActual:" + cifActual);
				}
				// Recuperamos la estructura de la línea

				TipoRegistro tr = registros.get(codigoRegistro);
				if (tr == null) {
					// la primera linea es el registro 000000 y no esta
					// contemplado en los registros 903000
					if (codigoRegistro.equals("000000")) {
						tratarRegistro000000(line);
					} else {
						// logger.info("procesaFichero:codigoRegistro:" +
						// codigoRegistro+" y TipoRegistro tr == null");
					}
				} else if (codigoRegistro.equals("901000")) {
					tratarRegistro901000(line);

				} else if (codigoRegistro.equals("702010")
						&& !getConfig().isDetalleLlamadas()) {
					// no hacemos nada... sin detalle
				} else if (codigoRegistro.equals("702020")
						&& !getConfig().isDetalleLlamadasRI()) {
					// no hacemos nada... sin detalle
				} else if (codigoRegistro.equals("000000")) {
					//tratarRegistro000000(line);
				} else {
					// Los bloques se recorren 1 vez, a menos que el campo
					// OCURRENCIAS con longitud <>0 diga lo contrario
					int[] vecesBloque = { 1, 1, 1 };
					int posVecesBloque = 1;
					int pos = 0;
					// Recorremos los bloques del registro
					for (int iBloque = 0; iBloque < tr.getNumBloques(); iBloque++) {
						Bloque bloque = tr.getBloques()[iBloque];
						EstructuraCampo[] estructuras = bloque.getEstructuras();

						if (vecesBloque[iBloque] > 1) {
							for (int j = 0; j < vecesBloque[iBloque]; j++) {
								StringBuilder resultLine = new StringBuilder();
								resultLine
										.append(Split977Config.COMILLAS_DOBLES)
										.append(nombreFicheroOriginal)
										.append(Split977Config.COMILLAS_DOBLES)
										.append(";").append("")
										.append(secuencial).append(";")
										.append(Split977Config.COMILLAS_DOBLES)
										.append(fechaFactura)
										.append(Split977Config.COMILLAS_DOBLES)
										.append(";")
										.append(Split977Config.COMILLAS_DOBLES)
										.append(cifActual)
										.append(Split977Config.COMILLAS_DOBLES)
										.append(";")
										.append(Split977Config.COMILLAS_DOBLES)
										.append(getConfig().getAcuerdo())
										.append(Split977Config.COMILLAS_DOBLES)
										.append(";").append("")
										.append(idAcuerdo).append(";")
										.append("");

								String fOutName = getConfig()
										.getDirectorioOut()
										+ File.separator
										+ codigoRegistro + "_" + (iBloque + 1);
								fOutName = FilenameUtils.normalize(fOutName);

								BufferedWriter bwOut = getBROut(fOutName);
								
								for (int k = 0; k < bloque.getNumEstructuras(); k++) {
									int offset = (new Integer(
											estructuras[k].getLongitudCampo()))
											.intValue();

									if ("CODIGO REGISTRO".equals(estructuras[k]
											.getNombreCampo().trim())) {
										if (offset > 0)
											pos += offset;
									} else if ("SECUENCIAL"
											.equals(estructuras[k]
													.getNombreCampo().trim())) {
										if (offset > 0)
											pos += offset;
									} else if ("LONGITUD REGISTRO"
											.equals(estructuras[k]
													.getNombreCampo().trim())) {
										if (offset > 0)
											pos += offset;
									} else if (offset > 0) {
										String tipoCampo = estructuras[k]
												.getTipoCampo();
										String formatoCampo = estructuras[k]
												.getFormatoCampo();
										String campo = "";
										/**
										 * Falla por longitud en algunos
										 * registros, por ejemplo 400000
										 */
										if (line.length() > (pos + offset)) {
											campo = line.substring(pos,
													pos += offset);
										} else {
											campo = line.substring(pos);
										}
										campo = correccionCamposNumericos(
												tipoCampo, campo);
										if (tipoCampo.equals("I")) {
											Double d = extraeDouble(
													formatoCampo, campo);
											resultLine.append("" + d + ";");
										} else if (tipoCampo.equals("N")) {
											long l = extraeLong(campo);
											if (l == -999999) {
												logger.info("ERROR: " + line);
												l = 0;
											}
											resultLine.append("" + l + ";");
										} else if (tipoCampo.equals("F")) {
											if (campo.equals("00000000")) {
												campo = "25001231";
											}
											resultLine.append("").append(campo)
													.append(";");
										} else {
											resultLine
													.append(Split977Config.COMILLAS_DOBLES)
													.append(ltrim(campo.trim()))
													.append(Split977Config.COMILLAS_DOBLES)
													.append(";");
										}
									} else {
										logger.info("OFFSET Negativo[" + offset
												+ "]");
									}

								}
								bwOut.write("" + resultLine.toString()
										+ Split977Config.CRLF);
								bwOut.flush();
							}

						} else {
							StringBuilder resultLine = new StringBuilder();
							resultLine
									.append(Split977Config.COMILLAS_DOBLES)
									.append(nombreFicheroOriginal)
									.append(Split977Config.COMILLAS_DOBLES)
									.append(";")
									.append("").append(secuencial).append(";")
									.append(Split977Config.COMILLAS_DOBLES)
									.append(fechaFactura)
									.append(Split977Config.COMILLAS_DOBLES)
									.append(";")
									.append(Split977Config.COMILLAS_DOBLES)
									.append(cifActual)
									.append(Split977Config.COMILLAS_DOBLES)
									.append(";")
									.append(Split977Config.COMILLAS_DOBLES)
									.append(getConfig().getAcuerdo())
									.append(Split977Config.COMILLAS_DOBLES)
									.append(";").append("").append(idAcuerdo)
									.append(";")
									.append("");
							String fOutName = getConfig().getDirectorioOut()
									+ File.separator + codigoRegistro + "_"
									+ (iBloque + 1);

							fOutName = FilenameUtils.normalize(fOutName);

							BufferedWriter bwOut = getBROut(fOutName);
							for (int k = 0; k < bloque.getNumEstructuras(); k++) {

								int offset = (new Integer(
										estructuras[k].getLongitudCampo()))
										.intValue();

								if ("CODIGO REGISTRO".equals(estructuras[k]
										.getNombreCampo().trim())) {
									if (offset > 0)
										pos += offset;
								} else if ("SECUENCIAL".equals(estructuras[k]
										.getNombreCampo().trim())) {
									if (offset > 0)
										pos += offset;
								} else if ("LONGITUD REGISTRO"
										.equals(estructuras[k].getNombreCampo()
												.trim())) {
									if (offset > 0)
										pos += offset;
								} else if (offset > 0) {
									String tipoCampo = estructuras[k]
											.getTipoCampo();
									String campo = line.substring(pos,
											pos += offset);
									String formatoCampo = estructuras[k]
											.getFormatoCampo();
									campo = correccionCamposNumericos(
											tipoCampo, campo);

									if (tipoCampo.equals("I")) {
										Double d = extraeDouble(formatoCampo,
												campo);
										resultLine.append("").append(d)
												.append(";");
									} else if (tipoCampo.equals("N")) {
										long l = extraeLong(campo);
										if (l == -999999) {
											System.err
													.println("ERROR: " + line);
											l = 0;
										}
										resultLine.append("" + l + ";");
									} else if (tipoCampo.equals("F")) {
										if (campo.equals("00000000")) {
											campo = "25001231";
										}
										resultLine.append("" + campo + ";");
									} else {
										resultLine
												.append(Split977Config.COMILLAS_DOBLES)
												.append(ltrim(campo.trim()))
												.append(Split977Config.COMILLAS_DOBLES)
												.append(";");
									}
									if (estructuras[k].getNombreCampo()
											.substring(0, 5).equals("OCURR")
											&& offset > 0 && iBloque == 0) {
										vecesBloque[posVecesBloque] = (new Integer(
												campo)).intValue();
										posVecesBloque++;
									}
								}
							}
							// logger.info(resultLine);
							bwOut.write("" + resultLine.toString()
									+ Split977Config.CRLF);
							bwOut.flush();

						}
						
						if(iBloque == 1){
							//Bloque de detalle
							if(!numRegistros.containsKey(codigoRegistro)){
								numRegistros.put(codigoRegistro, 0L);
							}
							numRegistros.put(codigoRegistro, numRegistros.get(codigoRegistro)+1);
						}

					}// for(int iBloque = 0; iBloque < tr.getNumBloques(); // iBloque++){
					
				}// if(tr == null){

				line = in.readLine();

			}// while(true){

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @throws IOException
	 */
	private void cerrarFicherosSalida() {
		try {
			Set<String> sKeys = bwOut.keySet();
			Vector<String> vKeys = new Vector<String>(sKeys);
			Collections.sort(vKeys);
			for (Iterator<String> i = vKeys.iterator(); i.hasNext();) {
				bwOut.get(i.next()).close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void tratarRegistro000000(String line)
			throws ElFicheroYaExisteException, NoSeHaPodidoGuardarElFichero {

		int posicionFechaFactura = 305;
		int posicionFicheroOriginal = 577;
		int posicionCIFActual = 162;

		// Extraemos los datos necesarios de 000000
		// La fecha
		fechaFactura = line.substring(posicionFechaFactura,
				posicionFechaFactura + 8);
		logger.info("Fecha Factura:[" + fechaFactura + "]");
		// nombre del fichero
		nombreFicheroOriginal = line.substring(posicionFicheroOriginal,
				posicionFicheroOriginal + 12);
		logger.info("Nombre del fichero:[" + nombreFicheroOriginal + "]");
		// cif actual
		String cifActual000000 = line.substring(posicionCIFActual,
				posicionCIFActual + 18);
		logger.info("CIF Supracliente:[" + cifActual000000 + "]");

	}

	private void obtieneInfoRegistro000000(String line)
			throws ElFicheroYaExisteException, NoSeHaPodidoGuardarElFichero {

		int posicionFechaFactura = 305;
		int posicionFicheroOriginal = 577;
		int posicionCIFActual = 162;

		// Extraemos los datos necesarios de 000000
		// La fecha
		fechaFactura = line.substring(posicionFechaFactura,
				posicionFechaFactura + 8);
		logger.info("Fecha Factura:[" + fechaFactura + "]");
		// nombre del fichero
		nombreFicheroOriginal = line.substring(posicionFicheroOriginal,
				posicionFicheroOriginal + 12);
		logger.info("Nombre del fichero:[" + nombreFicheroOriginal + "]");
		// cif actual
		String cifActual000000 = line.substring(posicionCIFActual,
				posicionCIFActual + 18);
		logger.info("CIF Supracliente:[" + cifActual000000 + "]");

		try {
			guardarFichero(cifActual000000);

			StringBuilder resultLine = new StringBuilder();
			resultLine.append(Split977Config.COMILLAS_DOBLES)
					.append(nombreFicheroOriginal)
					.append(Split977Config.COMILLAS_DOBLES).append(";")
					.append(Split977Config.COMILLAS_DOBLES)
					.append(fechaFactura)
					.append(Split977Config.COMILLAS_DOBLES).append(";")
					.append(Split977Config.COMILLAS_DOBLES)
					.append(cifActual000000)
					.append(Split977Config.COMILLAS_DOBLES).append(";")
					.append(Split977Config.COMILLAS_DOBLES)
					.append(getConfig().getAcuerdo())
					.append(Split977Config.COMILLAS_DOBLES).append(";")
					.append("").append(idAcuerdo).append(";").append("");

			String fOutName = getConfig().getDirectorioOut() + File.separator
					+ "000000";

			fOutName = FilenameUtils.normalize(fOutName);
			BufferedWriter bwOut = getBROut(fOutName);
			bwOut.write("" + resultLine.toString() + Split977Config.CRLF);
			bwOut.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void guardarFichero(String cifActual000000)
			throws ElFicheroYaExisteException, NoSeHaPodidoGuardarElFichero {
		
		logger.info("Guardando fichero:" + nombreFicheroOriginal + ":" + fechaFactura + ":" + cifActual000000  );
		Fichero elFichero = ficheroService.findByNombreFechaFacturaCif(
				nombreFicheroOriginal, fechaFactura, cifActual000000);

		if (elFichero != null) {
			// el fichero YA existe en la tabla
			logger.info("Existe el fichero "+ elFichero.getId() +":" + nombreFicheroOriginal + ":" + fechaFactura + ":" + cifActual000000  );
			
			if(!getConfig().isRecargaFicheros()){
				throw new ElFicheroYaExisteException(nombreFicheroOriginal,
						fechaFactura, cifActual000000);
			}else{
				logger.info("Borramos el fichero "+ elFichero.getId() +":" + nombreFicheroOriginal + ":" + fechaFactura + ":" + cifActual000000  );
				ficheroService.deleteByFichero(nombreFicheroOriginal, fechaFactura, cifActual000000);
			}
		} else {
			Fichero nuevoFichero = new Fichero();
			nuevoFichero.setFichero(nombreFicheroOriginal);
			nuevoFichero.setFechaFactura(fechaFactura);
			nuevoFichero.setCifSupraCliente(cifActual000000);
			Fichero ret = ficheroService.create(nuevoFichero);
			if (ret == null) {
				throw new NoSeHaPodidoGuardarElFichero(nombreFicheroOriginal,
						fechaFactura, cifActual000000);
			} else {
				Fichero retFichero = new Fichero();
				retFichero = ficheroService.findByNombreFechaFacturaCif(
						nombreFicheroOriginal, fechaFactura, cifActual000000);
				idFichero = retFichero.getId();
				logger.info("El Id del fichero es:" + idFichero);
			}
		}
	}

	/**
	 * 
	 * @param line
	 */
	private void tratarRegistro901000(String line) {

		/**
		 * El registro es ligeramente diferente del resto. Los Bloques 2 y 3 se
		 * tienen que tratar juntos
		 */
		BufferedWriter bwOut = null;
		BufferedWriter bwOut2 = null;
		try {
			int repeticionesBloque2 = 0;
			int longitudTextoBloque3 = 0;

			String codigoRegistro = line.substring(0, 6);
			// String secuencial = line.substring(6, 6 + 8);
			int secuencial = Integer.parseInt(line.substring(6, 6 + 8));

			TipoRegistro tr = registros.get(codigoRegistro);
			Bloque bloque = tr.getBloques()[0];
			EstructuraCampo[] estructuras = bloque.getEstructuras();
			StringBuilder resultLine = new StringBuilder();
			resultLine
					.append(Split977Config.COMILLAS_DOBLES)
					.append(nombreFicheroOriginal)
					.append(Split977Config.COMILLAS_DOBLES)
					.append(";")
					// .append("\"" + secuencial + COMILLAS_DOBLES + ";")
					.append("").append(secuencial).append(";")
					.append(Split977Config.COMILLAS_DOBLES)
					.append(fechaFactura)
					.append(Split977Config.COMILLAS_DOBLES).append(";")
					.append(Split977Config.COMILLAS_DOBLES).append(cifActual)
					.append(Split977Config.COMILLAS_DOBLES).append(";")
					.append(Split977Config.COMILLAS_DOBLES)
					.append(getConfig().getAcuerdo())
					.append(Split977Config.COMILLAS_DOBLES).append(";")
					.append("").append(idAcuerdo).append(";")
					// .append(COMILLAS_DOBLES).append(codigoRegistro).append("_").append((1)).append(COMILLAS_DOBLES).append(";")
					.append("");

			String fOutName = getConfig().getDirectorioOut() + File.separator
					+ codigoRegistro + "_" + (1);
			fOutName = FilenameUtils.normalize(fOutName);
			// logger.info("Fichero normalizado:" + fOutName);
			bwOut = getBROut(fOutName);
			int pos = 0;
			for (int k = 0; k < bloque.getNumEstructuras(); k++) {
				int offset = (new Integer(estructuras[k].getLongitudCampo()))
						.intValue();

				if ("CODIGO REGISTRO".equals(estructuras[k].getNombreCampo()
						.trim())) {
					logger.info("**" + estructuras[k].getNombreCampo() + "**");
					if (offset > 0)
						pos += offset;
				} else if ("SECUENCIAL".equals(estructuras[k].getNombreCampo()
						.trim())) {
					logger.info("**" + estructuras[k].getNombreCampo() + "**");
					if (offset > 0)
						pos += offset;
				} else if ("LONGITUD REGISTRO".equals(estructuras[k]
						.getNombreCampo().trim())) {
					logger.info("**" + estructuras[k].getNombreCampo() + "**");
					if (offset > 0)
						pos += offset;
				} else if (offset > 0) {
					String nomCampo = estructuras[k].getNombreCampo();
					String tipoCampo = estructuras[k].getTipoCampo();
					String formatoCampo = estructuras[k].getFormatoCampo();
					String campo = "";
					if (line.length() > (pos + offset)) {
						campo = line.substring(pos, pos += offset);

					} else {
						campo = line.substring(pos);

					}
					if (nomCampo.trim().equals("OCURRENCIAS")) {
						repeticionesBloque2 = (new Integer(campo)).intValue();

					} else if (nomCampo.trim().equals("LONGITUD TEXTOS")) {
						longitudTextoBloque3 = (new Integer(campo)).intValue();

					}

					campo = correccionCamposNumericos(tipoCampo, campo);
					if (tipoCampo.equals("I")) {
						Double d = extraeDouble(formatoCampo, campo);
						resultLine.append("" + d + ";");
					} else {
						resultLine.append(Split977Config.COMILLAS_DOBLES)
								.append(ltrim(campo.trim()))
								.append(Split977Config.COMILLAS_DOBLES)
								.append(";");
					}
				} else {
					logger.info("OFFSET:[" + offset + "]");
				}
			}
			bwOut.write("" + resultLine.toString() + Split977Config.CRLF);
			bwOut.flush();

			fOutName = getConfig().getDirectorioOut() + "/" + codigoRegistro
					+ "_" + (2);
			fOutName = FilenameUtils.normalize(fOutName);
			logger.info("Fichero normalizado:" + fOutName);
			bwOut2 = getBROut(fOutName);
			for (int i = 0; i < repeticionesBloque2; i++) {
				resultLine = new StringBuilder();
				resultLine
						.append(Split977Config.COMILLAS_DOBLES)
						.append(nombreFicheroOriginal)
						.append(Split977Config.COMILLAS_DOBLES)
						.append(";")
						// .append("\"" + secuencial + COMILLAS_DOBLES + ";")
						.append("").append(secuencial).append(";")
						.append(Split977Config.COMILLAS_DOBLES)
						.append(fechaFactura)
						.append(Split977Config.COMILLAS_DOBLES).append(";")
						.append(Split977Config.COMILLAS_DOBLES)
						.append(cifActual)
						.append(Split977Config.COMILLAS_DOBLES).append(";")
						.append(Split977Config.COMILLAS_DOBLES)
						.append(getConfig().getAcuerdo())
						.append(Split977Config.COMILLAS_DOBLES).append(";")
						.append("").append(idAcuerdo).append(";")
						// .append(COMILLAS_DOBLES).append(codigoRegistro).append("_").append((2)).append(COMILLAS_DOBLES).append(";")
						.append("");
				String campo = "";
				Bloque bloque2 = tr.getBloques()[1];
				estructuras = bloque2.getEstructuras();
				for (int k = 0; k < bloque2.getNumEstructuras(); k++) {
					int offset = (new Integer(estructuras[k].getLongitudCampo()))
							.intValue();

					if ("CODIGO REGISTRO".equals(estructuras[k]
							.getNombreCampo().trim())) {
						logger.info("**" + estructuras[k].getNombreCampo()
								+ "**");
						if (offset > 0)
							pos += offset;
					} else if ("SECUENCIAL".equals(estructuras[k]
							.getNombreCampo().trim())) {
						logger.info("**" + estructuras[k].getNombreCampo()
								+ "**");
						if (offset > 0)
							pos += offset;
					} else if ("LONGITUD REGISTRO".equals(estructuras[k]
							.getNombreCampo().trim())) {
						logger.info("**" + estructuras[k].getNombreCampo()
								+ "**");
						if (offset > 0)
							pos += offset;
					} else if (offset > 0) {

						String tipoCampo = estructuras[k].getTipoCampo();
						String formatoCampo = estructuras[k].getFormatoCampo();

						if (line.length() > (pos + offset)) {
							campo = line.substring(pos, pos += offset);
						} else {
							campo = line.substring(pos);
						}

						campo = correccionCamposNumericos(tipoCampo, campo);
						if (tipoCampo.equals("I")) {
							Double d = extraeDouble(formatoCampo, campo);
							resultLine.append("").append(d).append(";");
						} else {
							resultLine.append(Split977Config.COMILLAS_DOBLES)
									.append(ltrim(campo.trim()))
									.append(Split977Config.COMILLAS_DOBLES)
									.append(";");
						}
					} else {
						logger.info("OFFSET:[" + offset + "]");
					}
				}
				if (line.length() > (pos + longitudTextoBloque3)) {
					campo = line.substring(pos, pos += longitudTextoBloque3);
				} else {
					campo = line.substring(pos);
				}
				resultLine.append(Split977Config.COMILLAS_DOBLES)
						.append(ltrim(campo.trim()))
						.append(Split977Config.COMILLAS_DOBLES).append(";");
				bwOut2.write("" + resultLine.toString() + Split977Config.CRLF);
				bwOut2.flush();
			}
		} catch (Exception e) {
		} finally {
		}

	}

	/**
	 * 
	 * Retorna un Buffer de escritura según el nombre pasado por parámetro
	 * 
	 * 
	 * @param fOutName
	 * @return
	 */
	private BufferedWriter getBROut(String fOutName) {

		/**
		 * 
		 * El método busca el fichero en la HashTable, si lo encuentra devuelve
		 * el Objeto sino crea uno nuevo
		 */
		// logger.info("Fichero clave:" + fOutName);
		File fileOut = null;
		BufferedWriter out = null;
		try {
			if (filesOut.get(fOutName) == null) {
				// creamos un nuevo fOut
				fileOut = new File(fOutName + ".txt");
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(fileOut),
						Split977Config.CODIFICACION_FICHERO_ORIGEN));
				filesOut.put(fOutName, fileOut);
				bwOut.put(fOutName, out);
				// logger.info("Fichero:" + fOutName);
			} else {
				out = bwOut.get(fOutName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return out;
	}

	/**
	 * @param fileName
	 */
	@SuppressWarnings("unused")
	private void getEstructuraRegistros(String fileName) {
		try {
			// File file = new File(fileName);
			// BufferedReader in = new BufferedReader(new FileReader(file));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(fileName)),
					Split977Config.CODIFICACION_FICHERO_ORIGEN));

			int numLinea = 0;

			String line;
			line = in.readLine();
			while (true) {
				if (line == null) {
					break;
				}

				if (line.startsWith("903000")) {

					TipoRegistro tr = new TipoRegistro();
					Bloque[] bloques = new Bloque[3];
					int pos = 0;
					int offset = 0;
					String line2 = line.substring(122);
					// logger.info(numLinea+";"+line2);
					offset = 6;
					String codigoRegistro = line2.substring(pos, pos += offset);
					tr.setCodigoRegistro(codigoRegistro);
					// logger.info("Registro:"+codigoRegistro);
					offset = 3;
					String numBloques = line2.substring(pos, pos += offset);
					int iNumBloques = Integer.parseInt(numBloques);
					tr.setNumBloques(iNumBloques);
					// logger.info("NumBloques:"+ iNumBloques);
					offset = 3;
					String numCampos = line2.substring(pos, pos += offset);
					int iNumCampos = Integer.parseInt(numCampos);
					tr.setNumCampos(iNumCampos);
					// logger.info("NumCampos:"+ iNumCampos );
					int[] arr = { 1 };
					for (int j = 0; j < iNumBloques; j++) {
						// Tenemos que pasar el valor de la variable pos por
						// referencia,
						// pues se modificar� en el m�todo extraeBloque
						// El truco est� en pasarlo en un array
						arr[0] = pos;
						Bloque bloque = extraeBloque(arr, line2, iNumCampos);
						pos = arr[0];
						bloques[j] = bloque;
					}// for(int j = 0; j < iNumBloques; j++)
					tr.setBloques(bloques);
					// registros.add(codigoRegistro, tr);
					registros.put(codigoRegistro, tr);

				}// if(line.startsWith("903000"))

				numLinea++;
				line = in.readLine();

			}

			in.close();
			logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			// logger.info(registros);
			Enumeration<String> e = (Enumeration<String>) registros.keys();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				// logger.info(key + ":" + registros.get(key));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * En los registros 903000 está la estructura de los registros de un fichero
	 * 977R.
	 * 
	 * Este método lee el fichero línea a línea y localiza las líneas que
	 * comienzan por 903000. En ellas está la información de cómo leer el resto
	 * de registros.
	 * 
	 * @param in
	 */
	private void getEstructuraRegistros(BufferedReader in) {
		try {

			String line = in.readLine();
			while (true) {
				if (line == null) {
					break;
				}

				if (line.startsWith("903000")) {

					TipoRegistro tipoRegistro = new TipoRegistro();
					Bloque[] bloques = new Bloque[3];
					int pos = 0;
					int offset = 0;
					String line2 = line.substring(122);

					offset = 6;
					String codigoRegistro = line2.substring(pos, pos += offset);
					tipoRegistro.setCodigoRegistro(codigoRegistro);

					offset = 3;
					String numBloques = line2.substring(pos, pos += offset);
					int iNumBloques = Integer.parseInt(numBloques);
					tipoRegistro.setNumBloques(iNumBloques);

					offset = 3;
					String numCampos = line2.substring(pos, pos += offset);
					int iNumCampos = Integer.parseInt(numCampos);
					tipoRegistro.setNumCampos(iNumCampos);

					int[] arr = { 1 };
					for (int j = 0; j < iNumBloques; j++) {

						/**
						 * Tenemos que pasar el valor de la variable "pos" por
						 * referencia, pues se modificará en el método
						 * "extraeBloque"
						 * 
						 * El truco está en pasarlo en un array ya que es un
						 * objeto...
						 * 
						 */
						arr[0] = pos;
						Bloque bloque = extraeBloque(arr, line2, iNumCampos);
						pos = arr[0];
						bloques[j] = bloque;
					}// for(int j = 0; j < iNumBloques; j++)

					tipoRegistro.setBloques(bloques);
					registros.put(codigoRegistro, tipoRegistro);

				}// if(line.startsWith("903000"))

				line = in.readLine();

			}

			in.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param tipoCampo
	 * @param campo
	 * @return
	 */
	private String correccionCamposNumericos(String tipoCampo, String campo) {
		if ((tipoCampo.equals("I") || tipoCampo.equals("N"))
				&& campo.length() > 0) {
			String positivos = "{ABCDEFGHI";
			String negativos = "}JKLMNOPQR";

			String derecha = campo.substring(campo.length() - 1);
			// logger.info("campo:" + campo + "\tderecha:" + derecha);
			if (positivos.indexOf(derecha) > -1) { // es positivo
				campo = campo.replace(derecha,
						(new Integer(positivos.indexOf(derecha))).toString());
			} else if (negativos.indexOf(derecha) > -1) { // es negativo
				campo = campo.replace(derecha,
						(new Integer(negativos.indexOf(derecha))).toString());
				campo = "-" + campo;
			}
			// logger.info("campo:" + campo);
		}
		return campo;
	}

	/**
	 * @param pos
	 * @param line2
	 * @param iNumCampos
	 * @return
	 */
	private Bloque extraeBloque(int[] posiciones, String line2, int iNumCampos) {
		int offset;
		offset = 1;
		int pos = posiciones[0];
		String numBloque = line2.substring(pos, pos += offset);
		int iNumBloque = Integer.parseInt(numBloque);
		// logger.info("NumBloque:"+ iNumBloque );
		offset = 1;
		String numBloquePadre = line2.substring(pos, pos += offset);
		int iNumBloquePadre = Integer.parseInt(numBloquePadre);
		// logger.info("NumBloquePadre:"+ iNumBloquePadre );
		Bloque bloque = new Bloque();
		bloque.setNumBloque(iNumBloque);
		bloque.setNumBloquePadre(iNumBloquePadre);
		EstructuraCampo[] estructuras = new EstructuraCampo[50];
		for (int k = 0; k < iNumCampos; k++) {
			offset = 52;
			String strEstructuraCampo = line2.substring(pos, pos += offset);
			if (!strEstructuraCampo.substring(0, 6).equals("      ")) {
				// logger.info("estructuraCampo:"+
				// splitRegistro(strEstructuraCampo) );
				EstructuraCampo ec = extraeEstructuraCampo(strEstructuraCampo);
				estructuras[k] = ec;
				// logger.info(ec);
				bloque.setNumEstructuras(bloque.getNumEstructuras() + 1);
			}
		}
		bloque.setEstructuras(estructuras);
		// logger.info("" + bloque);
		posiciones[0] = pos;
		return bloque;
	}

	/**
	 * @param strEstructuraCampo
	 * @return
	 */
	private EstructuraCampo extraeEstructuraCampo(String strEstructuraCampo) {
		EstructuraCampo ec = new EstructuraCampo();
		int pos2 = 0;
		ec.setNombreCampo(strEstructuraCampo.substring(pos2,
				pos2 += posiciones[0]));
		ec.setTipoCampo(strEstructuraCampo.substring(pos2,
				pos2 += posiciones[1]));
		ec.setFormatoCampo(strEstructuraCampo.substring(pos2,
				pos2 += posiciones[2]));
		ec.setPosicionCampo(strEstructuraCampo.substring(pos2,
				pos2 += posiciones[3]));
		ec.setLongitudCampo(strEstructuraCampo.substring(pos2,
				pos2 += posiciones[4]));
		ec.setRepetidoCampo(strEstructuraCampo.substring(pos2,
				pos2 += posiciones[5]));
		ec.setAuxCampo(strEstructuraCampo
				.substring(pos2, pos2 += posiciones[6]));

		logger.info(ec.toString());
		return ec;
	}

	/**
	 * @param formatoCampo
	 * @param campo
	 * @return double
	 */
	private double extraeDouble(String formatoCampo, String campo) {

		double d = 0.0;
		boolean negativo = false;
		if (formatoCampo.indexOf(",") > -1) { // Existe formato decimal

			// Es negativo??
			String neg = campo.substring(0, 1);
			if (neg.equals("-")) {
				negativo = true;
			}
			String entero = formatoCampo
					.substring(0, formatoCampo.indexOf(","));

			if (negativo) {
				// El tema está en los decimales NEGATIVOS, si es negativo,
				// entonces la longitud de campo era mayor

				campo = "-"
						+ campo.substring(1,
								(new Integer(entero)).intValue() + 1) + "."
						+ campo.substring((new Integer(entero)).intValue() + 1);
			} else {
				campo = campo.substring(0, (new Integer(entero)).intValue())
						+ "."
						+ campo.substring((new Integer(entero)).intValue());
			}

			try {

				d = new Double(campo);

			} catch (NumberFormatException e) {
				// e.printStackTrace();
				logger.error("Error en la conversión de [" + campo
						+ "] a tipo DOUBLE.");
				return 0.0;
			}
		}
		return d;
	}

	/**
	 * 
	 * @param campo
	 * 
	 * @return long
	 */
	private long extraeLong(final String campo) {
		long l = 0;
		try {
			l = new Long(campo.trim());
		} catch (NumberFormatException e) {
			logger.info("Error en la conversión de [" + campo
					+ "] a tipo LONG.");
			l = -999999; // error
		}
		return l;
	}

	private String ltrim(final String source) {
		return source.replaceAll("^\\s+", "");
	}

	/**
	 * @return the ficheros
	 */
	public String[] getFicheros() {
		return ficheros;
	}

	/**
	 * @param ficheros
	 *            the ficheros to set
	 */
	public void setFicheros(String[] f) {
		ficheros = f;
	}

	/**
	 * @param nombreFicheroOriginal
	 *            the nombreFicheroOriginal to set
	 */
	public void setNombreFicheroOriginal(String nombreFicheroOriginal) {
		this.nombreFicheroOriginal = nombreFicheroOriginal;
	}

	/**
	 * @return the nombreFicheroOriginal
	 */
	public String getNombreFicheroOriginal() {
		return nombreFicheroOriginal;
	}

	/**
	 * @param borrarAcuerdo
	 *            the borrarAcuerdo to set
	 */
	public void setBorrarAcuerdo(boolean borrarAcuerdo) {
		this.getConfig().setBorrarAcuerdo(borrarAcuerdo);
	}

	/**
	 * @return the borrarAcuerdo
	 */
	public boolean isBorrarAcuerdo() {
		return getConfig().isBorrarAcuerdo();
	}

	public long getTiempoEmpleado() {
		return tiempoEmpleado;
	}

	public void setTiempoEmpleado(long tiempoEmpleado) {
		this.tiempoEmpleado = tiempoEmpleado;
	}

	public void setFicherosZipPath(String path) {

		List<String> aListFiles = new ArrayList<String>();
		File dir = new File(path);
		if (dir.isDirectory()) {
			String[] files = dir.list();
			for (String file : files) {
				logger.info("setFicherosZipPath:[" + file + "]");
				// int dot = file.lastIndexOf(".");
				// String extension = file.substring(dot + 1);
				if (FilenameUtils.getExtension(file).equalsIgnoreCase("zip")) {
					String unFichero = FilenameUtils.normalize(path
							+ File.separator + file);
					aListFiles.add(unFichero);
					logger.info("Fichero ZIP:[" + unFichero + "]");
				} else {
					logger.info("Extensión:["
							+ FilenameUtils.getExtension(file) + "]");
				}
			}
			String[] f = aListFiles.toArray(new String[aListFiles.size()]);
			setFicheros(f);
		}

	}

	public void setFicherosPath(String path) {

		String[] extValidas = { "ene", "feb", "mar", "abr", "may", "jun",
				"jul", "ago", "sep", "oct", "nov", "dic", "ENE", "FEB", "MAR",
				"ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC" };

		List<String> aListFiles = new ArrayList<String>();
		File dir = new File(path);
		if (dir.isDirectory()) {
			String[] files = dir.list();
			for (String file : files) {
				int dot = file.lastIndexOf(".");
				String extension = file.substring(dot + 1);
				int indexOfExtension = ArrayUtils
						.indexOf(extValidas, extension);
				if (indexOfExtension > 0) {
					aListFiles.add(path + File.separator + file);
					logger.info("setFicherosZipPath:[" + path + File.separator
							+ file + "]");
				}
			}
			String[] f = aListFiles.toArray(new String[aListFiles.size()]);
			setFicheros(f);
		}

	}

	public Split977Config getConfig() {
		return config;
	}

	public void setConfig(Split977Config config) {
		this.config = config;

		// Indicamos dónde está el directorio con los ficheros .zip
		this.setFicherosZipPath(this.config.getDirectorioZipFiles());
	}

	public List<String> getResultados() {
		return resultados;
	}

	public void setResultados(List<String> resultados) {
		this.resultados = resultados;
	}

}
