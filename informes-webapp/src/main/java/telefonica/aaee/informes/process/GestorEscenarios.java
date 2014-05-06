package telefonica.aaee.informes.process;

/**
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.exceptions.CeldasIncorrectasException;
import telefonica.aaee.informes.exceptions.ConceptoFacturableNotFoundException;
import telefonica.aaee.informes.exceptions.PlantaPrecioEspecialNotFoundException;
import telefonica.aaee.informes.exceptions.ReasignacionCargoNotFoundException;
import telefonica.aaee.informes.exceptions.ReasignacionNotFoundException;
import telefonica.aaee.informes.exceptions.TraficoInternacionalNotFoundException;
import telefonica.aaee.informes.exceptions.TraficoInternacionalPorNivelNotFoundException;
import telefonica.aaee.informes.exceptions.TraficoNotFoundException;
import telefonica.aaee.informes.exceptions.TraficoRINotFoundException;
import telefonica.aaee.informes.model.condiciones.ConceptoFacturable;
import telefonica.aaee.informes.model.condiciones.PlantaPrecioEspecial;
import telefonica.aaee.informes.model.condiciones.Reasignacion;
import telefonica.aaee.informes.model.condiciones.ReasignacionCargo;
import telefonica.aaee.informes.model.condiciones.Trafico;
import telefonica.aaee.informes.model.condiciones.TraficoInternacional;
import telefonica.aaee.informes.model.condiciones.TraficoInternacionalPorNivel;
import telefonica.aaee.informes.model.condiciones.TraficoRI;
import telefonica.aaee.informes.services.condiciones.ConceptoFacturableService;
import telefonica.aaee.informes.services.condiciones.PlantaPrecioEspecialService;
import telefonica.aaee.informes.services.condiciones.ReasignacionCargoService;
import telefonica.aaee.informes.services.condiciones.ReasignacionService;
import telefonica.aaee.informes.services.condiciones.TraficoInternacionalPorNivelService;
import telefonica.aaee.informes.services.condiciones.TraficoInternacionalService;
import telefonica.aaee.informes.services.condiciones.TraficoRIService;
import telefonica.aaee.informes.services.condiciones.TraficoService;

/**
 * @author t130796
 * 
 */
@Service
public class GestorEscenarios {

	private static final String BUSCA_CONDICION_TRF_POR_AMBITO = "Buscamos la condición de tráfico por acuerdo y ámbito: [%s] - [%s]";
	private static final String BUSCA_CONDICION_TRF_INT_POR_NIVEL = "Buscamos la condición de tráfico internacional por Nivel:[%s]-[%s]";
	private static final String BUSCA_CONDICION_TRF_INT_POR_ACUERDO_Y_DESTINO = "Buscamos la condición de tráfico internacional por acuerdo y destino: [%s] [%s]";
	
	private static final String CABECERAS_TRF_INT_POR_NIVEL = "cabecerasTRFInt_PorNivel";
	private static final String CABECERAS_CCF = "cabecerasCCF";
	private static final String CABECERAS_TRF = "cabecerasTRF";
	private static final String CABECERAS_TRF_RI = "cabecerasTRFRI";
	private static final String CABECERAS_TRF_INT = "cabecerasTRFInt";
	private static final String CABECERAS_REAS_CARGO = "cabecerasReasignacionCargos";
	private static final String CABECERAS_REAS = "cabecerasReasignacion";
	private static final String CABECERAS_PPE = "cabecerasPlantaPrecioEspecial";

	private static final String ID = "id";
	private static final String ACUERDO = "acuerdo";

	private static final String PORCENTAJE_DESCUENTO = "porcentaje descuento";
	private static final String PRECIO_POR_MINUTO = "precio por minuto";
	private static final String EST_LLAMADA = "est llamada";
	private static final String TIPO_DESCUENTO = "tipo descuento";
	private static final String IMPORTE_ACUERDO = "importe acuerdo";
	private static final String IMPORTE_ORIGINAL = "importe original";
	private static final String TIPO_PRECIO_ESPECIAL = "tipo precio especial";
	private static final String PRECIO_ESPECIAL = "precio especial";
	private static final String DESC_CONCEPTO_FACTURABLE = "desc concepto facturable";
	private static final String CONCEPTO_FACTURABLE = "concepto facturable";
	private static final String TIPO_DE_SERVICIO = "tipo de servicio";
	private static final String DESC_TIPO_DE_SERVICIO = "desc tipo de servicio";
	private static final String AMBITO_DE_TRAFICO = "ambito de trafico";
	private static final String DESTINO = "destino";
	private static final String PAIS_DESTINO = "pais destino";
	private static final String DESC_AMBITO_DE_TRAFICO = "desc ambito de trafico";
	
	// Reasignación cargos
	private static final String TIPO_DOC = "tipo doc";
	private static final String CIF = "cif";
	private static final String NOMBRE_CLIENTE = "nombre cliente";
	private static final String GRUPO_DE_GASTO = "grupo de gasto";
	private static final String AGRUPACION_FACTURABLE = "agrupacion facturable";
	private static final String TIPO_DOC_REASIGNADO = "tipo doc reasignado";
	private static final String CIF_REASIGNADO = "cif reasignado";
	private static final String NOMBRE_CLIENTE_REASIGNADO = "nombre cliente reasignado";
	private static final String GRUPO_DE_GASTO_REASIGNADO = "grupo de gasto reasignado";
	private static final String AGRUPACION_FACTURABLE_REASIGNADO = "agrupacion facturable reasignado";
	private static final String COMENTARIOS = "comentarios";

	// Reasignación 
	private static final String CIF_NUEVO = "cif nuevo";
	private static final String NOMBRE_NUEVO = "nombre nuevo";
	private static final String MULTICONEXION = "multiconexion";
	private static final String NUMERO_COMERCIAL = "numero comercial";
	private static final String NUMERO_COMERCIAL_ASOCIADO = "numero comercial asociado";
	private static final String CIF_ORIGINAL = "cif original";
	private static final String NOMBRE_ORIGINAL = "nombre original";
	private static final String CENTRO_COSTE = "centrocoste";
	private static final String NUMERO_CUENTA = "numerocuenta";
	private static final String IDACUERDO = "idacuerdo";
	
	//PlantaPreciosEspeciales
	private static final String MAX_IMPORTE_ESTANDAR_PRODUCTO = "max importe estandar producto";
	private static final String MIN_IMPORTE_ESTANDAR_PRODUCTO = "min importe estandar producto";
	private static final String MAX_IMPORTE_UNITARIO = "max importe unitario";
	private static final String MIN_IMPORTE_UNITARIO = "min importe unitario";

	protected final Log logger = LogFactory.getLog(getClass());

	private Map<String, List<String>> cabObligatorias = new HashMap<String, List<String>>();
	private Map<String, List<String>> cabExistentes = new HashMap<String, List<String>>();

	private File fileIn = null;

	//private String result = null;
	private long numModificaciones = 0;
	private List<String> errores = new ArrayList<String>();

	private EntityManager entityManager;
	
	@Autowired
	private ConceptoFacturableService cfService;

	@Autowired
	private TraficoService traficoService;

	@Autowired
	private TraficoRIService traficoRIService;

	@Autowired
	private TraficoInternacionalService traficoIntService;

	@Autowired
	private TraficoInternacionalPorNivelService traficoIntNivelService;

	@Autowired
	private ReasignacionCargoService reasCargoService;

	@Autowired
	private ReasignacionService reasService;

	@Autowired
	private PlantaPrecioEspecialService ppeService;

	@PersistenceContext(unitName = "JPAInformesWebApp")
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}
	
	@PostConstruct
    public void init() {
		logger.info("Inicializando ConceptoFacturableService..." + cfService.findAll().size());
		logger.info("Inicializando TraficoService..." + traficoService.findAll().size());
	}

	
	
	public GestorEscenarios() {
		super();

		Properties properties = new Properties();

		try {
			InputStream inStream = this.getClass().getResourceAsStream("/config.properties");
			properties.load(inStream);

			Enumeration<?> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);

				List<String> lista = Arrays.asList(value.split(","));
				cabObligatorias.put(key, lista);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param in
	 * @param out
	 */
	public boolean execute() {
		
		boolean bResult = true;
		InputStream inputStream = null;
		numModificaciones = 0;

		Calendar calendar = Calendar.getInstance();
		long lIni = 0;

		try {

			lIni = calendar.getTimeInMillis();

			inputStream = new FileInputStream(getFileIn());
			
			Workbook workBook = null;
			String ext = FilenameUtils.getExtension(getFileIn().getAbsolutePath());
			
			if("xlsx".equalsIgnoreCase(ext)){
				workBook = new XSSFWorkbook(inputStream);
			}else if("xls".equalsIgnoreCase(ext)){
				workBook = new HSSFWorkbook(inputStream);
			}

			int pos = -1;
			Sheet sheet = null;
			String pestanya = "";

			pestanya = "Cond.CCF";
			logger.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				logger.info("Procesando pestaña " + pestanya);
				updateCCF(sheet);
			} else {
				logger.warn("No existe la pestaña " + pestanya);
			}

			pestanya = "Cond.TRF";
			logger.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				logger.info("Procesando pestaña " + pestanya);
				updateTRF(sheet);
			} else {
				logger.warn("No existe la pestaña " + pestanya);
			}

			pestanya = "Cond.TRF.RI";
			logger.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				logger.info("Procesando pestaña " + pestanya);
				updateTRFRI(sheet);
			} else {
				logger.warn("No existe la pestaña " + pestanya);
			}

			pestanya = "Cond.TRFInternacional";
			logger.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				logger.info("Procesando pestaña " + pestanya);
				updateTRFInt(sheet);
			} else {
				logger.warn("No existe la pestaña " + pestanya);
			}

			//
			pestanya = "Cond.TRFInternacional_PorNivel";
			logger.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				logger.info("Procesando pestaña " + pestanya);
				updateTRFInt_PorNivel(sheet);
			} else {
				logger.warn("No existe la pestaña " + pestanya);
			}

			pestanya = "ReasignacionCargos";
			logger.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				logger.info("Procesando pestaña " + pestanya);
				updateReasignacionCargos(sheet);
			} else {
				logger.warn("No existe la pestaña " + pestanya);
			}

			pestanya = "Reasignacion";
			logger.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				logger.info("Procesando pestaña " + pestanya);
				updateReasignacion(sheet);
			} else {
				logger.warn("No existe la pestaña " + pestanya);
			}

			pestanya = "Cond.PPE";
			logger.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				logger.info("Procesando pestaña " + pestanya);
				updatePlantaPreciosEspeciales(sheet);
			} else {
				logger.warn("No existe la pestaña " + pestanya);
			}

		} catch (IOException e) {
			e.printStackTrace();
			errores.add(e.getMessage());
			bResult = false;
		} catch (CeldasIncorrectasException e) {
			System.err.println(e.getMessage());
			errores.add(e.getMessage());
			e.printStackTrace();
			bResult = false;
		} catch (SQLException e) {
			e.printStackTrace();
			errores.add(e.getMessage());
			bResult = false;
		} catch (Exception e) {
			e.printStackTrace();
			errores.add(e.getMessage());
			bResult = false;
		} finally {

			Calendar calendar2 = Calendar.getInstance();
			long lFin = calendar2.getTimeInMillis();
			
			if(errores.size() >0){
				logger.warn("Lista de errores.");
				for(String error : errores){
					logger.warn("ERROR:[" + error + "]");
				}
			}

			logger.info("Total modificaciones:" + this.getNumModificaciones());
			logger.info("Total tiempo:" + lFin + "-" + lIni + "="
					+ ((lFin - lIni) / 1000) + " segundos!");
		}

		return bResult;
	}

	private String quitarComas(String cell2String) {
		String c = cell2String;
		c = c.replaceAll(",", ".");
		return c;
	}

	/**
	 * @param sheet
	 */
	private boolean celdasCabeceraOK(final Sheet sheet,
			final List<String> obligatorias, List<String> existentes) {
		String c;
		boolean celdasOK = true;
		Iterator<Cell> cells = sheet.getRow(0).cellIterator();
		while (cells.hasNext()) {
			Cell cell = cells.next();
			c = cell2String(cell).toLowerCase();
			c = c.replaceAll("_", " ");
			logger.info("*" + c + "* está!");
			existentes.add(c);
			
//			if (!obligatorias.contains(c)) {
//				logger.info("*" + c + "* OJO NO está!");
//				celdasOK = false;
//			} else {
//				logger.info("*" + c + "* está!");
//				existentes.add(c);
//			}
		}// while (cells.hasNext ())
		
		if(!existentes.containsAll(obligatorias)){
			for(String o : obligatorias){
				logger.info("obligatorias: *" + o + "*");
				if(!existentes.contains(o)){
					logger.info("obligatorias: *" + o + "* OJO NO está!");
				}
			}
			celdasOK = false;
		}
		return celdasOK;
	}

	/**
	 * @param cell
	 * @return
	 */
	private String cell2String(Cell cell) {
		String c;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: {
			c = String.valueOf(cell.getNumericCellValue());
			break;
		}

		case Cell.CELL_TYPE_STRING: {
			RichTextString richTextString = cell.getRichStringCellValue();
			c = richTextString.getString();
			break;
		}

		case Cell.CELL_TYPE_BLANK: {
			c = String.valueOf(cell.getStringCellValue()) + ":"
					+ Cell.CELL_TYPE_BLANK;
			break;
		}

		case Cell.CELL_TYPE_FORMULA: {
			c = String.valueOf(cell.getNumericCellValue()) + ":"
					+ Cell.CELL_TYPE_FORMULA;
			break;
		}

		default: {
			// types other than String and Numeric.
			logger.info("Type not supported." + cell.getCellType());
			c = cell.toString();
			break;
		}
		}// switch (cell.getCellType ())
		return c.trim();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setFileIn(File fileIn) {
		this.fileIn = fileIn;
	}

	public File getFileIn() {
		return fileIn;
	}

	public void setNumModificaciones(long numModificaciones) {
		this.numModificaciones = numModificaciones;
	}

	/**
	 * @return
	 */
	public long getNumModificaciones() {
		return numModificaciones;
	}

	/**
	 * @return
	 */
	private long incNumModificaciones() {
		return numModificaciones++;
	}

	@Transactional
	private void update(Object obj) {
		entityManager.merge(obj);
	}


	@Transactional
	private void save(Object obj) {
		entityManager.persist(obj);
	}


	/**
	 * @param conn
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 * @throws ConceptoFacturableNotFoundException 
	 */
	private void updateCCF(Sheet sheet) throws CeldasIncorrectasException,
			SQLException, ConceptoFacturableNotFoundException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet, cabObligatorias.get(CABECERAS_CCF), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_CCF, lista);

		logger.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> filas = (Iterator<Row>) sheet.rowIterator();
		while (filas.hasNext()) {

			Row row = filas.next();
			
			if (row.getRowNum() == 0) { // resto filas

			} else {

				logger.info("Fila:" + row.getRowNum());

				// Recogemos los datos del Excel...
				ConceptoFacturable condicion = fila2CF(row);
				
				logger.info("Localizada condicion:" + condicion.toString());

				// Localizamos esa entidad en la tabla por el Id...
				ConceptoFacturable cf = entityManager.find(ConceptoFacturable.class,
						condicion.getId());

				// no está...
				if (cf == null) {

					// miramos que está el acuerdo y el concepto...
					logger.info("CF no localizado!");
					Query query = entityManager.createNamedQuery("FindByAcuerdoCifTipoDeServicio");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("cf", condicion.getConceptoFacturable());
					query.setParameter("sv", condicion.getTipoDeServicio());
					try {
						cf = (ConceptoFacturable) query.getSingleResult();
						logger.info("Localizado CF:" + cf.toString());

						// Solo modificamos si hay un precio especial...
						if (condicion.getPrecioEspecial().equalsIgnoreCase("SI")) {
							
							cf.setPrecioEspecial(condicion.getPrecioEspecial());
							cf.setTipoPrecioEspecial(condicion.getTipoPrecioEspecial());
							cf.setImporteAcuerdo(condicion.getImporteAcuerdo());
							
							//update(cf);
							ConceptoFacturable mod = cfService.update(cf);
							
							if (!mod.equals(cf)) {
								logger.info("Modificado CF:" + mod.toString());
								incNumModificaciones();
							}
						} else {
							logger.info("Localizado CF " + cf.getId()
									+ ", sin precio especial!");
						}
					} catch (javax.persistence.NoResultException e) {
						logger.warn("No existe el par acuerdo-conceptofacturable:"
								+ condicion.getAcuerdo()
								+ ":"
								+ condicion.getConceptoFacturable());
						
						//save(condicion);
						condicion = cfService.create(condicion);
						incNumModificaciones();
						logger.warn("Guardado el CF :"
								+ condicion.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						logger.warn("Hay más de un resultado:"
								+ condicion.toString());
						errores.add(String.format("Hay más de un resultado para la tupla Acuerdo-Concepto Facturable-Tipo de Servicio:"
								, condicion.getAcuerdo()
								, condicion.getConceptoFacturable()
								, condicion.getTipoDeServicio()));
						cf = null;
					}
				} else {
					logger.info("Localizado CF:" + cf.toString());
					cf.setPrecioEspecial(condicion.getPrecioEspecial());
					cf.setTipoPrecioEspecial(condicion.getTipoPrecioEspecial());
					cf.setImporteAcuerdo(condicion.getImporteAcuerdo());
					
					ConceptoFacturable mod = cfService.update(cf);
					
					if (!mod.equals(cf)) {
						logger.info("Modificado CF:" + mod.toString());
						incNumModificaciones();
					}
				}
			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}

	/**
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 * @throws TraficoNotFoundException 
	 */
	private void updateTRF(Sheet sheet) throws CeldasIncorrectasException,
			SQLException, TraficoNotFoundException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet, cabObligatorias.get(CABECERAS_TRF), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_TRF, lista);

		logger.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				logger.info("Fila:" + row.getRowNum());

				Trafico condicion = fila2TRF(row);

				Trafico trf = entityManager.find(Trafico.class, condicion.getId());

				if (trf == null) {
					// Buscamos por acuerdo y ��mbito de tr��fico
					logger.info("TRF no localizado!");
					logger.info(String.format(BUSCA_CONDICION_TRF_POR_AMBITO
							, condicion.getAcuerdo()
							, condicion.getAmbitoDeTrafico()));
					
					Query query = entityManager.createNamedQuery("FindByAcuerdoAmbitoDeTrafico");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("at", condicion.getAmbitoDeTrafico());
					try {
						trf = (Trafico) query.getSingleResult();
						logger.info("Localizado CF:" + trf.toString());
						trf.setPrecioEspecial(condicion.getPrecioEspecial());
						trf.setPrecioEspecial(condicion.getPrecioEspecial());
						trf.setPrecioPorMinuto(condicion.getPrecioPorMinuto());

						Trafico mod = traficoService.update(trf);
						
						if (!mod.equals(trf)) {
							logger.info("Modificado TRF:" + mod.toString());
							incNumModificaciones();
						}
					} catch (javax.persistence.NoResultException e) {
						logger.warn("No existe el par acuerdo-ambito:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getAmbitoDeTrafico());

						condicion = traficoService.create(condicion);
						incNumModificaciones();
						logger.info("Guardado TRF:" + condicion.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						logger.warn("Hay más de un resultado:"
								+ condicion.toString());
						errores.add(String.format("Hay más de un resultado para la tupla Acuerdo-Ambito de Tráfico:"
								, condicion.getAcuerdo()
								, condicion.getAmbitoDeTrafico()));
						trf = null;
					}
				} else {
					logger.info("Localizado TRF:" + trf.toString());

					trf.setPrecioEspecial(condicion.getPrecioEspecial());
					trf.setTipoDescuento(condicion.getTipoDescuento());
					trf.setPrecioPorMinuto(condicion.getPrecioPorMinuto());
					trf.setPorcentajeDescuento(condicion.getPorcentajeDescuento());
					trf.setEstLlamada(condicion.getEstLlamada());

					Trafico mod = traficoService.update(trf);
					
					if (!mod.equals(trf)) {
						logger.info("Modificado TRF:" + mod.toString());
						incNumModificaciones();
					}


				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}

	/**
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 * @throws TraficoNotFoundException 
	 */
	private void updateTRFRI(Sheet sheet) throws CeldasIncorrectasException,
			SQLException, TraficoRINotFoundException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet, cabObligatorias.get(CABECERAS_TRF_RI), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_TRF_RI, lista);

		logger.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				logger.info("Fila:" + row.getRowNum());

				TraficoRI condicion = fila2TRFRI(row);

				TraficoRI trf = entityManager.find(TraficoRI.class, condicion.getId());

				if (trf == null) {
					// Buscamos por acuerdo y ��mbito de tr��fico
					logger.info("TRF no localizado!");
					logger.info(String.format(BUSCA_CONDICION_TRF_POR_AMBITO
							, condicion.getAcuerdo()
							, condicion.getAmbitoDeTrafico()));
					
					Query query = entityManager.createNamedQuery("TRAFICORI.FindByAcuerdoAmbitoDeTrafico");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("at", condicion.getAmbitoDeTrafico());
					try {
						trf = (TraficoRI) query.getSingleResult();
						logger.info("Localizado TraficoRI:" + trf.toString());
						trf.setPrecioEspecial(condicion.getPrecioEspecial());
						trf.setPrecioEspecial(condicion.getPrecioEspecial());
						trf.setPrecioPorMinuto(condicion.getPrecioPorMinuto());

						TraficoRI mod = traficoRIService.update(trf);
						
						if (!mod.equals(trf)) {
							logger.info("Modificado TraficoRI:" + mod.toString());
							incNumModificaciones();
						}
					} catch (javax.persistence.NoResultException e) {
						logger.warn("No existe el par acuerdo-ambito:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getAmbitoDeTrafico());

						condicion = traficoRIService.create(condicion);
						incNumModificaciones();
						logger.info("Guardado TraficoRI:" + condicion.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						logger.warn("Hay más de un resultado:"
								+ condicion.toString());
						errores.add(String.format("Hay más de un resultado para la tupla Acuerdo-Ambito de Tráfico:"
								, condicion.getAcuerdo()
								, condicion.getAmbitoDeTrafico()));
						trf = null;
					}
				} else {
					logger.info("Localizado TraficoRI:" + trf.toString());

					trf.setPrecioEspecial(condicion.getPrecioEspecial());
					trf.setTipoDescuento(condicion.getTipoDescuento());
					trf.setPrecioPorMinuto(condicion.getPrecioPorMinuto());
					trf.setPorcentajeDescuento(condicion.getPorcentajeDescuento());
					trf.setEstLlamada(condicion.getEstLlamada());

					TraficoRI mod = traficoRIService.update(trf);
					
					if (!mod.equals(trf)) {
						logger.info("Modificado TRF:" + mod.toString());
						incNumModificaciones();
					}


				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}

	/**
	 * 
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 * @throws TraficoInternacionalNotFoundException 
	 */
	private void updateTRFInt(Sheet sheet) throws CeldasIncorrectasException,
			SQLException, TraficoInternacionalNotFoundException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet, cabObligatorias.get(CABECERAS_TRF_INT),
				lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_TRF_INT, lista);

		logger.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				logger.info("Fila:" + row.getRowNum());

				TraficoInternacional condicion = fila2TRFInt(row);

				TraficoInternacional tint = entityManager.find(TraficoInternacional.class, condicion.getId());
				if (tint == null) {
					// Buscamos por acuerdo y ��mbito de tr��fico
					logger.info("TRFInt no localizado!");
					logger.info(String.format(BUSCA_CONDICION_TRF_INT_POR_ACUERDO_Y_DESTINO
							, condicion.getAcuerdo()
							, condicion.getDestino()));
					Query query = entityManager.createNamedQuery("FindByAcuerdoDestino");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("pd", condicion.getDestino());
					try {
						tint = (TraficoInternacional) query.getSingleResult();
						logger.info("Localizado TRFInt:" + tint.toString());
						tint.setPrecioEspecial(condicion.getPrecioEspecial());
						tint.setPrecioPorMinuto(condicion.getPrecioPorMinuto());

						TraficoInternacional mod = traficoIntService.update(tint);
						
						if (!mod.equals(tint)) {
							logger.info("Modificado TRFINT:" + mod.toString());
							incNumModificaciones();
						}

					} catch (javax.persistence.NoResultException e) {
						logger.warn("No existe el par acuerdo-destino:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getDestino());

						TraficoInternacional nuevo = traficoIntService.create(tint);
						incNumModificaciones();
						logger.info("Guardado TRFInt:" + nuevo.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						logger.warn("Hay más de un resultado para el par:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getDestino());
						errores.add(String.format("Hay más de un resultado para el par acuerdo-destino: [%s]-[%s]"
								, condicion.getAcuerdo()
								, condicion.getDestino()));
						tint = null;
					}

				} else {
					logger.info("Localizado TRFInt:" + tint.toString());

					tint.setPrecioEspecial(condicion.getPrecioEspecial());
					tint.setTipoDescuento(condicion.getTipoDescuento());
					tint.setPrecioPorMinuto(condicion.getPrecioPorMinuto());
					tint.setPorcentajeDescuento(condicion.getPorcentajeDescuento());
					tint.setEstLlamada(condicion.getEstLlamada());

					TraficoInternacional mod = traficoIntService.update(tint);


					if (!mod.equals(tint)) {
						logger.info("Modificado TRFInt:" + mod.toString());
						incNumModificaciones();
					}
					

				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}

	/**
	 * 
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 * @throws TraficoInternacionalPorNivelNotFoundException 
	 */
	private void updateTRFInt_PorNivel(Sheet sheet)
			throws CeldasIncorrectasException, SQLException, TraficoInternacionalPorNivelNotFoundException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet,
				cabObligatorias.get(CABECERAS_TRF_INT_POR_NIVEL), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_TRF_INT_POR_NIVEL, lista);

		logger.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				logger.info("Fila:" + row.getRowNum());

				TraficoInternacionalPorNivel condicion = fila2TRFInt_PorNivel(row);

				TraficoInternacionalPorNivel tint = entityManager.find(
						TraficoInternacionalPorNivel.class, condicion.getId());
				if (tint == null) {
					// Buscamos por acuerdo y ��mbito de tr��fico
					logger.info("TRF no localizado!");
					logger.info(String.format(BUSCA_CONDICION_TRF_INT_POR_NIVEL
							, condicion.getAcuerdo()
							, condicion.getNivel()));
					Query query = entityManager.createNamedQuery("FindByAcuerdoNivel");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("pd", condicion.getNivel());
					try {
						tint = (TraficoInternacionalPorNivel) query
								.getSingleResult();
						logger.info("Localizado TRFInt:" + tint.toString());
						tint.setPrecioEspecial(condicion.getPrecioEspecial());
						tint.setPrecioPorMinuto(condicion.getPrecioPorMinuto());

						TraficoInternacionalPorNivel mod = traficoIntNivelService.update(tint);
						
						if (!mod.equals(tint)) {
							logger.info("Modificado TRFINT:" + mod.toString());
							incNumModificaciones();
						}
						
					} catch (javax.persistence.NoResultException e) {
						logger.warn("No existe el par acuerdo-nivel:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getNivel());

						TraficoInternacionalPorNivel nuevo = traficoIntNivelService.create(tint);
						incNumModificaciones();
						logger.info("Guardado TRFInt:" + nuevo.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						logger.warn("Hay más de un resultado para el par:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getNivel());
						errores.add(String.format("Hay más de un resultado para el par acuerdo-nivel: [%s]-[%s]"
								, condicion.getAcuerdo()
								, condicion.getNivel()));

						tint = null;
					}

				} else {
					logger.info("Localizado TRFInt:" + tint.toString());

					tint.setPrecioEspecial(condicion.getPrecioEspecial());
					tint.setTipoDescuento(condicion.getTipoDescuento());
					tint.setPrecioPorMinuto(condicion.getPrecioPorMinuto());
					tint.setPorcentajeDescuento(condicion
							.getPorcentajeDescuento());
					tint.setEstLlamada(condicion.getEstLlamada());

					TraficoInternacionalPorNivel mod = traficoIntNivelService.update(tint);
					
					if (!mod.equals(tint)) {
						logger.info("Modificado TRFINTNIV:" + mod.toString());
						incNumModificaciones();
					}



				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}
	
	private void updateReasignacionCargos(Sheet sheet)
			throws CeldasIncorrectasException, SQLException, ReasignacionCargoNotFoundException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet,
				cabObligatorias.get(CABECERAS_REAS_CARGO), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_REAS_CARGO, lista);

		logger.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				logger.info("Fila:" + row.getRowNum());

				// Recuperamos la condición del Excel
				ReasignacionCargo condicion = fila2ReasignacionCargo(row);

				// Localizamos el elemento de la tabla
				ReasignacionCargo reasignacion = entityManager.find(ReasignacionCargo.class, condicion.getId());
				
				if (reasignacion == null) {
					// Buscamos por acuerdo, cif, grupo de gasto y agrupación facturable
					logger.info("ReasignacionCargo no localizado!");
					logger.info("Buscamos la condición de Reasignacion por Cargo:");
					
					Query query = entityManager.createNamedQuery("FindByAcuerdoCifGrupoDeGastoAgrupacionFacturable");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("c", condicion.getCif());
					query.setParameter("g", condicion.getGrupoDeGasto());
					query.setParameter("af", condicion.getAgrupacionFacturable());
					try {
						reasignacion = (ReasignacionCargo) query
								.getSingleResult();
						logger.info("Localizado ReasignacionCargo:" + reasignacion.toString());
						reasignacion.setTipoDocReasignado(condicion.getTipoDocReasignado());
						reasignacion.setCifReasignado(condicion.getCifReasignado());
						reasignacion.setNombreClienteReasignado(condicion.getNombreClienteReasignado());
						reasignacion.setGrupoDeGastoReasignado(condicion.getGrupoDeGastoReasignado());
						reasignacion.setAgrupacionFacturableReasignado(condicion.getAgrupacionFacturableReasignado());

						ReasignacionCargo mod = reasCargoService.update(reasignacion);
						
						if (!mod.equals(reasignacion)) {
							logger.info("Modificado REASCARGO:" + mod.toString());
							incNumModificaciones();
						}
					} catch (javax.persistence.NoResultException e) {
						logger.warn("No existe la tupla Acuerdo-Cif-GrupoDeGasto-AgrupacionFacturable:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getCif() + ":"
								+ condicion.getGrupoDeGasto() + ":"
								+ condicion.getAgrupacionFacturable()
								);

						ReasignacionCargo nuevo = reasCargoService.create(condicion);
						incNumModificaciones();
						logger.info("Guardado REASCARGO:" + nuevo.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						logger.warn("Hay más de un resultado para la tupla Acuerdo-Cif-GrupoDeGasto-AgrupacionFacturable:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getCif() + ":"
								+ condicion.getGrupoDeGasto() + ":"
								+ condicion.getAgrupacionFacturable()
								);
						errores.add(String.format("Hay más de un resultado para la tupla Acuerdo-Cif-GrupoDeGasto-AgrupacionFacturable:"
								, condicion.getAcuerdo()
								, condicion.getCif()
								, condicion.getGrupoDeGasto()
								, condicion.getAgrupacionFacturable()));
						reasignacion = null;
					}

				} else {
					logger.info("Localizado ReasignacionCargo:" + reasignacion.toString());

					reasignacion.setTipoDocReasignado(condicion.getTipoDocReasignado());
					reasignacion.setCifReasignado(condicion.getCifReasignado());
					reasignacion.setNombreClienteReasignado(condicion.getNombreClienteReasignado());
					reasignacion.setGrupoDeGastoReasignado(condicion.getGrupoDeGastoReasignado());
					reasignacion.setAgrupacionFacturableReasignado(condicion.getAgrupacionFacturableReasignado());

					ReasignacionCargo mod = reasCargoService.update(reasignacion);
					
					if (!mod.equals(reasignacion)) {
						logger.info("Modificado REASCARGO:" + mod.toString());
						incNumModificaciones();
					}

				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}


	private void updateReasignacion(Sheet sheet)
			throws CeldasIncorrectasException, SQLException, ReasignacionNotFoundException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet,
				cabObligatorias.get(CABECERAS_REAS), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_REAS, lista);

		logger.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				logger.info("Fila:" + row.getRowNum());

				// Recuperamos la condición del Excel
				Reasignacion condicion = fila2Reasignacion(row);

				// Localizamos el elemento de la tabla
				Reasignacion reasignacion = entityManager.find(Reasignacion.class, condicion.getId());
				
				if (reasignacion == null) {
					// Buscamos por acuerdo, cif, grupo de gasto y agrupación facturable
					logger.info("Reasignacion no localizado!");
					logger.info("Buscamos la condición de Reasignacion:");
					
					Query query = entityManager.createNamedQuery("FindByAcuerdoNumeroComercialAsociado");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("m", condicion.getMulticonexion());
					query.setParameter("nc", condicion.getNumeroComercial());
					query.setParameter("nca", condicion.getNumeroComercialAsociado());
					try {
						reasignacion = (Reasignacion) query
								.getSingleResult();
						logger.info("Localizado Reasignacion:" + reasignacion.toString());
						
						reasignacion.setCifNuevo(condicion.getCifNuevo());
						reasignacion.setNombreNuevo(condicion.getNombreNuevo());
						
						reasignacion.setNumeroCuenta(condicion.getNumeroCuenta());
						reasignacion.setCentroCoste(condicion.getCentroCoste());

						Reasignacion mod = reasService.update(reasignacion);
						
						if (!mod.equals(reasignacion)) {
							logger.info("Modificado Reasignacion:" + mod.toString());
							incNumModificaciones();
						}
					} catch (javax.persistence.NoResultException e) {
						logger.warn("No existe la tupla Acuerdo-Multiconexion-NC-NCA:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getMulticonexion() + ":"
								+ condicion.getNumeroComercial() + ":"
								+ condicion.getNumeroComercialAsociado()
								);

						Reasignacion nuevo = reasService.create(condicion);
						incNumModificaciones();
						logger.info("Guardado Reasignacion:" + nuevo.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						logger.warn("Hay más de un resultado para la tupla Acuerdo-Cif-GrupoDeGasto-AgrupacionFacturable:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getMulticonexion() + ":"
								+ condicion.getNumeroComercial() + ":"
								+ condicion.getNumeroComercialAsociado()
								);
						errores.add(String.format("Hay más de un resultado para la tupla Acuerdo-Cif-GrupoDeGasto-AgrupacionFacturable:"
								, condicion.getAcuerdo()
								+ condicion.getMulticonexion() + ":"
								+ condicion.getNumeroComercial() + ":"
								+ condicion.getNumeroComercialAsociado()
								));
						reasignacion = null;
					}

				} else {
					logger.info("Localizado ReasignacionCargo:" + reasignacion.toString());

					reasignacion.setCifNuevo(condicion.getCifNuevo());
					reasignacion.setNombreNuevo(condicion.getNombreNuevo());
					
					reasignacion.setNumeroCuenta(condicion.getNumeroCuenta());
					reasignacion.setCentroCoste(condicion.getCentroCoste());

					Reasignacion mod = reasService.update(reasignacion);
					
					if (!mod.equals(reasignacion)) {
						logger.info("Modificado REASCARGO:" + mod.toString());
						incNumModificaciones();
					}

				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}



	/**
	 * @param row
	 * @return
	 */
	private ConceptoFacturable fila2CF(Row row) {
		// De cada fila recorremos las celdas
		Iterator<Cell> cells1 = row.cellIterator();

		ConceptoFacturable condicion = new ConceptoFacturable();

		while (cells1.hasNext()) {

			Cell cell = cells1.next();
			short colNum = (short) cell.getColumnIndex();
			String strCelda = cell2String(cell);

			List<String> lista = cabExistentes.get(CABECERAS_CCF);
			if (colNum < lista.size()) {
				String campo = lista.get(colNum);

				// logger.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					double lTmp = Double.parseDouble(strCelda);
					long id = Math.round(lTmp);
					// logger.info("id:\t" + id);
					condicion.setId(id);

				} else if (campo.equals(PRECIO_ESPECIAL)) {
					condicion.setPrecioEspecial(strCelda);

				} else if (campo.equals(TIPO_DE_SERVICIO)) {
					condicion.setTipoDeServicio(strCelda);

				} else if (campo.equals(DESC_TIPO_DE_SERVICIO)) {
					condicion.setDescTipoDeServicio(strCelda);

				} else if (campo.equals(TIPO_PRECIO_ESPECIAL)) {
					condicion.setTipoPrecioEspecial(strCelda);

				} else if (campo.equals(IMPORTE_ACUERDO)) {
					double importe_acuerdo = new Double(
							quitarComas(strCelda));
					condicion.setImporteAcuerdo(importe_acuerdo);

				} else if (campo.equals(IMPORTE_ORIGINAL)) {
					double importe_original = new Double(
							quitarComas(strCelda));
					condicion.setImporteOriginal(importe_original);

				} else if (campo.equals(CONCEPTO_FACTURABLE)) {
					condicion.setConceptoFacturable(strCelda);

				} else if (campo.equals(DESC_CONCEPTO_FACTURABLE)) {
					condicion.setDescConceptoFacturable(strCelda);

				} else if (campo.equals(ACUERDO)) {
					condicion.setAcuerdo(strCelda);
				}
			}

		}// while (cells.hasNext())
		condicion.setIniPeriodo("20110128");
		condicion.setFinPeriodo("25001228");
		return condicion;
	}

	/**
	 * @param row
	 * @return
	 */
	private Trafico fila2TRF(Row row) {
		Trafico condicion = new Trafico();

		// De cada fila recorremos las celdas
		Iterator<Cell> celdas = row.cellIterator();

		while (celdas.hasNext()) {
			Cell cell = celdas.next();
			int colNum = cell.getColumnIndex();
			String strCelda = cell2String(cell);

			List<String> lista = cabExistentes.get(CABECERAS_TRF);
			if (colNum < lista.size()) {
				String campo = lista.get(colNum);

				// logger.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// logger.info(String.format("ID: [%d]", id));
					condicion.setId(id);

				} else if (campo.equals(TIPO_DESCUENTO)) {
					condicion.setTipoDescuento(strCelda);

				} else if (campo.equals(EST_LLAMADA)) {
					double est_llamada = new Double(strCelda);
					condicion.setEstLlamada(est_llamada);

				} else if (campo.equals(PRECIO_POR_MINUTO)) {
					double precio_por_minuto = new Double(strCelda);
					condicion.setPrecioPorMinuto(precio_por_minuto);

				} else if (campo.equals(PORCENTAJE_DESCUENTO)) {
					double porcentaje_descuento = new Double(strCelda);
					condicion.setPorcentajeDescuento(porcentaje_descuento);

				} else if (campo.equals(PRECIO_ESPECIAL)) {
					condicion.setPrecioEspecial(strCelda);

				} else if (campo.equals(AMBITO_DE_TRAFICO)) {
					condicion.setAmbitoDeTrafico(strCelda);

				} else if (campo.equals(ACUERDO)) {
					condicion.setAcuerdo(strCelda);
				}
			}

		}// while (cells.hasNext())

		condicion.setIniPeriodo("20110128");
		condicion.setFinPeriodo("25001228");
		return condicion;
	}

	/**
	 * @param row
	 */
	private TraficoInternacional fila2TRFInt(Row row) {
		// De cada fila recorremos las celdas
		TraficoInternacional condicion = new TraficoInternacional();

		Iterator<Cell> celdas = row.cellIterator();
		while (celdas.hasNext()) {
			Cell cell = celdas.next();
			int colNum = cell.getColumnIndex();
			String strCelda = cell2String(cell);

			List<String> lista = cabExistentes.get(CABECERAS_TRF_INT);
			if (colNum < lista.size()) {
				String campo = lista.get(colNum);

				// logger.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// logger.info("id:\t" + id);
					condicion.setId(id);
				} else if (campo.equals(TIPO_DESCUENTO)) {
					condicion.setTipoDescuento(strCelda);
				} else if (campo.equals(EST_LLAMADA)) {
					double est_llamada = new Double(strCelda);
					condicion.setEstLlamada(est_llamada);
				} else if (campo.equals(PRECIO_POR_MINUTO)) {
					double precio_por_minuto = new Double(strCelda);
					condicion.setPrecioPorMinuto(precio_por_minuto);
				} else if (campo.equals(PORCENTAJE_DESCUENTO)) {
					double porcentaje_descuento = new Double(strCelda);
					condicion.setPorcentajeDescuento(porcentaje_descuento);
				} else if (campo.equals(PRECIO_ESPECIAL)) {
					condicion.setPrecioEspecial(strCelda);
				} else if (campo.equals(ACUERDO)) {
					condicion.setAcuerdo(strCelda);
				} else if (campo.equals(DESTINO)) {
					condicion.setDestino(strCelda);
				} else if (campo.equals(PAIS_DESTINO)) {
					condicion.setPaisDestino(strCelda);
				} else if (campo.equals(AMBITO_DE_TRAFICO)) {
					condicion.setAmbitoDeTrafico(strCelda);
				} else if (campo.equals(DESC_AMBITO_DE_TRAFICO)) {
					condicion.setDescAmbitoDeTrafico(strCelda);
				}
			}

		}// while (cells.hasNext())

		condicion.setIniPeriodo("20110128");
		condicion.setFinPeriodo("25001228");
		return condicion;
	}

	/**
	 * @param row
	 */
	private TraficoInternacionalPorNivel fila2TRFInt_PorNivel(Row row) {
		// De cada fila recorremos las celdas
		TraficoInternacionalPorNivel condicion = new TraficoInternacionalPorNivel();

		Iterator<Cell> celdas = row.cellIterator();
		while (celdas.hasNext()) {
			Cell cell = celdas.next();
			int colNum = cell.getColumnIndex();
			String strCelda = cell2String(cell);

			List<String> lista = cabExistentes.get(CABECERAS_TRF_INT_POR_NIVEL);
			if (colNum < lista.size()) {
				String campo = lista.get(colNum);

				// logger.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// logger.info("id:\t" + id);
					condicion.setId(id);
				} else if (campo.equals(TIPO_DESCUENTO)) {
					condicion.setTipoDescuento(strCelda);
				} else if (campo.equals(EST_LLAMADA)) {
					double est_llamada = new Double(strCelda);
					condicion.setEstLlamada(est_llamada);
				} else if (campo.equals(PRECIO_POR_MINUTO)) {
					double precio_por_minuto = new Double(strCelda);
					condicion.setPrecioPorMinuto(precio_por_minuto);
				} else if (campo.equals(PORCENTAJE_DESCUENTO)) {
					double porcentaje_descuento = new Double(strCelda);
					condicion.setPorcentajeDescuento(porcentaje_descuento);
				} else if (campo.equals(PRECIO_ESPECIAL)) {
					condicion.setPrecioEspecial(strCelda);
				} else if (campo.equals(ACUERDO)) {
					condicion.setAcuerdo(strCelda);
				} else if (campo.equals("nivel")) {
					condicion.setNivel(strCelda);
				} else if (campo.equals(AMBITO_DE_TRAFICO)) {
					condicion.setAmbitoDeTrafico(strCelda);
				} else if (campo.equals(DESC_AMBITO_DE_TRAFICO)) {
					condicion.setDescAmbitoDeTrafico(strCelda);
				}
			}

		}// while (cells.hasNext())

		condicion.setIniPeriodo("20110128");
		condicion.setFinPeriodo("25001228");
		return condicion;
	}
	/**
	 * @param row
	 */
	private ReasignacionCargo fila2ReasignacionCargo(Row row) {
		// De cada fila recorremos las celdas
		ReasignacionCargo condicion = new ReasignacionCargo();

		Iterator<Cell> celdas = row.cellIterator();
		while (celdas.hasNext()) {
			Cell cell = celdas.next();
			int colNum = cell.getColumnIndex();
			String strCelda = cell2String(cell);

			List<String> lista = cabExistentes.get(CABECERAS_REAS_CARGO);
			if (colNum < lista.size()) {
				String campo = lista.get(colNum);

				// logger.info(String.format("Columna [%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// logger.info("id:\t" + id);
					condicion.setId(id);
				} else if (campo.equals(TIPO_DOC_REASIGNADO)) {
					condicion.setTipoDocReasignado(strCelda);
				} else if (campo.equals(CIF_REASIGNADO)) {
					condicion.setCifReasignado(strCelda);
				} else if (campo.equals(NOMBRE_CLIENTE_REASIGNADO)) {
					condicion.setNombreClienteReasignado(strCelda);
				} else if (campo.equals(GRUPO_DE_GASTO_REASIGNADO)) {
					condicion.setGrupoDeGastoReasignado(strCelda);
				} else if (campo.equals(AGRUPACION_FACTURABLE_REASIGNADO)) {
					condicion.setAgrupacionFacturableReasignado(strCelda);
				} else if (campo.equals(ACUERDO)) {
					condicion.setAcuerdo(strCelda);
				} else if (campo.equals(TIPO_DOC)) {
					condicion.setTipoDoc(strCelda);
				} else if (campo.equals(CIF)) {
					condicion.setCif(strCelda);
				} else if (campo.equals(NOMBRE_CLIENTE)) {
					condicion.setNombreCliente(strCelda);
				} else if (campo.equals(GRUPO_DE_GASTO)) {
					condicion.setGrupoDeGasto(strCelda);
				} else if (campo.equals(AGRUPACION_FACTURABLE)) {
					condicion.setAgrupacionFacturable(strCelda);
				} else if (campo.equals(COMENTARIOS)) {
					condicion.setComentarios(strCelda);
				}
			}

		}// while (cells.hasNext())

//		condicion.setIniPeriodo("20110128");
//		condicion.setFinPeriodo("25001228");
		return condicion;
	}
	
	/**
	 * @param row
	 */
	private Reasignacion fila2Reasignacion(Row row) {
		// De cada fila recorremos las celdas
		Reasignacion condicion = new Reasignacion();

		Iterator<Cell> celdas = row.cellIterator();
		while (celdas.hasNext()) {
			Cell cell = celdas.next();
			int colNum = cell.getColumnIndex();
			String strCelda = cell2String(cell);

			List<String> lista = cabExistentes.get(CABECERAS_REAS);
			if (colNum < lista.size()) {
				String campo = lista.get(colNum);

				// logger.info(String.format("Columna [%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// logger.info("id:\t" + id);
					condicion.setId(id);
				} else if (campo.equals(CIF_ORIGINAL)) {
					condicion.setCifOriginal(strCelda);
				} else if (campo.equals(NOMBRE_ORIGINAL)) {
					condicion.setNombreOriginal(strCelda);
					
				} else if (campo.equals(CIF_NUEVO)) {
					condicion.setCifNuevo(strCelda);
				} else if (campo.equals(NOMBRE_NUEVO)) {
					condicion.setNombreNuevo(strCelda);
					
				} else if (campo.equals(ACUERDO)) {
					condicion.setAcuerdo(strCelda);
				} else if (campo.equals(IDACUERDO)) {
					condicion.setIdAcuerdo(Math.round(cell.getNumericCellValue()));
					
				} else if (campo.equals(MULTICONEXION)) {
					condicion.setMulticonexion(strCelda);
				} else if (campo.equals(NUMERO_COMERCIAL)) {
					condicion.setNumeroComercial(strCelda);
				} else if (campo.equals(NUMERO_COMERCIAL_ASOCIADO)) {
					condicion.setNumeroComercialAsociado(strCelda);
					
				} else if (campo.equals(NUMERO_CUENTA)) {
					condicion.setNumeroCuenta(strCelda);
				} else if (campo.equals(CENTRO_COSTE)) {
					condicion.setCentroCoste(strCelda);
				}
			}

		}// while (cells.hasNext())

//		condicion.setIniPeriodo("20110128");
//		condicion.setFinPeriodo("25001228");
		return condicion;
	}

	/**
	 * @param row
	 * @return
	 */
	private TraficoRI fila2TRFRI(Row row) {
		TraficoRI condicion = new TraficoRI();
	
		// De cada fila recorremos las celdas
		Iterator<Cell> celdas = row.cellIterator();
	
		while (celdas.hasNext()) {
			Cell cell = celdas.next();
			int colNum = cell.getColumnIndex();
			String strCelda = cell2String(cell);
	
			List<String> lista = cabExistentes.get(CABECERAS_TRF_RI);
			if (colNum < lista.size()) {
				String campo = lista.get(colNum);
	
				// logger.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// logger.info(String.format("ID: [%d]", id));
					condicion.setId(id);
	
				} else if (campo.equals(TIPO_DESCUENTO)) {
					condicion.setTipoDescuento(strCelda);
	
				} else if (campo.equals(EST_LLAMADA)) {
					double est_llamada = new Double(strCelda);
					condicion.setEstLlamada(est_llamada);
	
				} else if (campo.equals(PRECIO_POR_MINUTO)) {
					double precio_por_minuto = new Double(strCelda);
					condicion.setPrecioPorMinuto(precio_por_minuto);
	
				} else if (campo.equals(PORCENTAJE_DESCUENTO)) {
					double porcentaje_descuento = new Double(strCelda);
					condicion.setPorcentajeDescuento(porcentaje_descuento);
	
				} else if (campo.equals(PRECIO_ESPECIAL)) {
					condicion.setPrecioEspecial(strCelda);
	
				} else if (campo.equals(AMBITO_DE_TRAFICO)) {
					condicion.setAmbitoDeTrafico(strCelda);
	
				} else if (campo.equals(ACUERDO)) {
					condicion.setAcuerdo(strCelda);
				}
			}
	
		}// while (cells.hasNext())
	
		condicion.setIniPeriodo("20110128");
		condicion.setFinPeriodo("25001228");
		return condicion;
	}

	/**
	 * @param conn
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 * @throws ConceptoFacturableNotFoundException 
	 */
	private void updatePlantaPreciosEspeciales(Sheet sheet) throws CeldasIncorrectasException,
			SQLException, PlantaPrecioEspecialNotFoundException{
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet, cabObligatorias.get(CABECERAS_PPE), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");
	
		cabExistentes.put(CABECERAS_PPE, lista);
	
		logger.info(cabExistentes.toString());
	
		// Recorremos las filas
		Iterator<Row> filas = (Iterator<Row>) sheet.rowIterator();
		while (filas.hasNext()) {
	
			Row row = filas.next();
			
			if (row.getRowNum() == 0) { // resto filas
	
			} else {
	
				logger.info("Fila:" + row.getRowNum());
	
				// Recogemos los datos del Excel...
				PlantaPrecioEspecial condicion = fila2PPE(row);
	
				// Localizamos esa entidad en la tabla por el Id...
				PlantaPrecioEspecial cf = entityManager.find(PlantaPrecioEspecial.class,
						condicion.getId());
	
				// no está...
				if (cf == null) {
	
					// miramos que está el acuerdo y el concepto...
					logger.info("PlantaPrecioEspecial no localizado!");
					Query query = entityManager.createNamedQuery("FindByAcuerdoTipoDeServicioMulticonexionNCNCACF");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("ts", condicion.getTipoDeServicio());
					query.setParameter("m", condicion.getMulticonexion());
					query.setParameter("nc", condicion.getNumeroComercial());
					query.setParameter("nca", condicion.getNumeroComercialAsociado());
					query.setParameter("cf", condicion.getConceptoFacturable());
					try {
						cf = (PlantaPrecioEspecial) query.getSingleResult();
						logger.info("Localizado CF:" + cf.toString());
	
						// Solo modificamos si hay un precio especial...
						if (condicion.getPrecioEspecial().equalsIgnoreCase("SI")) {
							
							cf.setPrecioEspecial(condicion.getPrecioEspecial());
							cf.setTipoPrecioEspecial(condicion.getTipoPrecioEspecial());
							cf.setImporteAcuerdo(condicion.getImporteAcuerdo());
							
							//update(cf);
							PlantaPrecioEspecial mod = ppeService.update(cf);
							
							if (!mod.equals(cf)) {
								logger.info("Modificado CF:" + mod.toString());
								incNumModificaciones();
							}
						} else {
							logger.info("Localizado PlantaPrecioEspecial " + cf.getId()
									+ ", sin precio especial!");
						}
					} catch (javax.persistence.NoResultException e) {
						logger.warn("No existe el par acuerdo-conceptofacturable:"
								+ condicion.getAcuerdo()
								+ ":"
								+ condicion.getTipoDeServicio()
								+ ":"
								+ condicion.getMulticonexion()
								+ ":"
								+ condicion.getNumeroComercial()
								+ ":"
								+ condicion.getNumeroComercialAsociado()
								+ ":"
								+ condicion.getConceptoFacturable());
						
						//save(condicion);
						condicion = ppeService.create(condicion);
						incNumModificaciones();
						logger.warn("Guardado el PlantaPrecioEspecial :"
								+ condicion.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						logger.warn("Hay más de un resultado:"
								+ condicion.toString());
						errores.add(String.format("Hay más de un resultado para la tupla Acuerdo-Concepto Facturable-Tipo de Servicio:"
								, condicion.getAcuerdo()
								, condicion.getTipoDeServicio()
								, condicion.getMulticonexion()
								, condicion.getNumeroComercial()
								, condicion.getNumeroComercialAsociado()
								, condicion.getConceptoFacturable()
								));
						cf = null;
					}
				} else {
					logger.info("Localizado PlantaPrecioEspecial:" + cf.toString());
					cf.setPrecioEspecial(condicion.getPrecioEspecial());
					cf.setTipoPrecioEspecial(condicion.getTipoPrecioEspecial());
					cf.setImporteAcuerdo(condicion.getImporteAcuerdo());
					
					PlantaPrecioEspecial mod = ppeService.update(cf);
					
					if (!mod.equals(cf)) {
						logger.info("Modificado PlantaPrecioEspecial:" + mod.toString());
						incNumModificaciones();
					}
				}
			}// if (row.getRowNum() == 1)
	
		}// while (rows.hasNext ())
	}

	/**
	 * @param row
	 * @return
	 */
	private PlantaPrecioEspecial fila2PPE(Row row) {
		// De cada fila recorremos las celdas
		Iterator<Cell> cells1 = row.cellIterator();
	
		PlantaPrecioEspecial condicion = new PlantaPrecioEspecial();
	
		while (cells1.hasNext()) {
	
			Cell cell = cells1.next();
			short colNum = (short) cell.getColumnIndex();
			String strCelda = cell2String(cell);
	
			List<String> lista = cabExistentes.get(CABECERAS_PPE);
			if (colNum < lista.size()) {
				String campo = lista.get(colNum);
	
				// logger.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					double lTmp = Double.parseDouble(strCelda);
					long id = Math.round(lTmp);
					// logger.info("id:\t" + id);
					condicion.setId(id);
				
				} else if (campo.equals(IDACUERDO)) {
					double lTmp = Double.parseDouble(strCelda);
					long id = Math.round(lTmp);
					// logger.info("id:\t" + id);
					condicion.setIdAcuerdo(id);
	
				} else if (campo.equals(TIPO_DE_SERVICIO)) {
					condicion.setTipoDeServicio(strCelda);
				} else if (campo.equals(DESC_TIPO_DE_SERVICIO)) {
					condicion.setDescTipoDeServicio(strCelda);
	
				} else if (campo.equals(PRECIO_ESPECIAL)) {
					condicion.setPrecioEspecial(strCelda);
				} else if (campo.equals(TIPO_PRECIO_ESPECIAL)) {
					condicion.setTipoPrecioEspecial(strCelda);
				} else if (campo.equals(IMPORTE_ACUERDO)) {
					double importe_acuerdo = new Double(
							quitarComas(strCelda));
					condicion.setImporteAcuerdo(importe_acuerdo);
	
				} else if (campo.equals(MAX_IMPORTE_ESTANDAR_PRODUCTO)) {
					double importe = new Double(
							quitarComas(strCelda));
					condicion.setMaxImporteEstandarProducto(importe);
	
				} else if (campo.equals(MIN_IMPORTE_ESTANDAR_PRODUCTO)) {
					double importe = new Double(
							quitarComas(strCelda));
					condicion.setMinImporteEstandarProducto(importe);
	
				} else if (campo.equals(MAX_IMPORTE_UNITARIO)) {
					double importe = new Double(
							quitarComas(strCelda));
					condicion.setMaxImporteUnitario(importe);
	
				} else if (campo.equals(MIN_IMPORTE_UNITARIO)) {
					double importe = new Double(
							quitarComas(strCelda));
					condicion.setMinImporteUnitario(importe);
	
				} else if (campo.equals(CONCEPTO_FACTURABLE)) {
					condicion.setConceptoFacturable(strCelda);
				} else if (campo.equals(DESC_CONCEPTO_FACTURABLE)) {
					condicion.setDescConceptoFacturable(strCelda);
	
				} else if (campo.equals(MULTICONEXION)) {
					condicion.setMulticonexion(strCelda);
				} else if (campo.equals(NUMERO_COMERCIAL)) {
					condicion.setNumeroComercial(strCelda);
				} else if (campo.equals(NUMERO_COMERCIAL_ASOCIADO)) {
					condicion.setNumeroComercialAsociado(strCelda);
					
				} else if (campo.equals(ACUERDO)) {
					condicion.setAcuerdo(strCelda);
				}
			}
	
		}// while (cells.hasNext())
		condicion.setIniPeriodo("20110128");
		condicion.setFinPeriodo("25001228");
		return condicion;
	}
}
