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
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.exceptions.CeldasIncorrectasException;
import telefonica.aaee.informes.model.condiciones.ConceptoFacturable;
import telefonica.aaee.informes.model.condiciones.ReasignacionCargo;
import telefonica.aaee.informes.model.condiciones.Trafico;
import telefonica.aaee.informes.model.condiciones.TraficoInternacional;
import telefonica.aaee.informes.model.condiciones.TraficoInternacionalPorNivel;

/**
 * @author t130796
 * 
 */
public class GestorEscenarios {

	private static final String BUSCA_CONDICION_TRF_POR_AMBITO = "Buscamos la condición de tráfico por acuerdo y ámbito: [%s] - [%s]";
	private static final String BUSCA_CONDICION_TRF_INT_POR_NIVEL = "Buscamos la condición de tráfico internacional por Nivel:[%s]-[%s]";
	private static final String BUSCA_CONDICION_TRF_INT_POR_ACUERDO_Y_DESTINO = "Buscamos la condición de tráfico internacional por acuerdo y destino: [%s] [%s]";
	
	private static final String CABECERAS_TRF_INT_POR_NIVEL = "cabecerasTRFInt_PorNivel";
	private static final String CABECERAS_CCF = "cabecerasCCF";
	private static final String CABECERAS_TRF = "cabecerasTRF";
	private static final String CABECERAS_TRF_INT = "cabecerasTRFInt";
	private static final String CABECERAS_REAS_CARGO = "cabecerasReasignacionCargos";

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

	private static final Logger LOGGER = Logger.getLogger(GestorEscenarios.class.getCanonicalName());

	private Map<String, List<String>> cabObligatorias = new HashMap<String, List<String>>();
	private Map<String, List<String>> cabExistentes = new HashMap<String, List<String>>();

	private File fileIn = null;

	//private String result = null;
	private long numModificaciones = 0;
	private List<String> errores = new ArrayList<String>();

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
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

		Calendar calendar = Calendar.getInstance();
		long lIni = 0;

		try {

			lIni = calendar.getTimeInMillis();

			inputStream = new FileInputStream(getFileIn());

			Workbook workBook = new XSSFWorkbook(inputStream);

			int pos = -1;
			Sheet sheet = null;
			String pestanya = "";

			pestanya = "Cond.CCF";
			LOGGER.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				LOGGER.info("Procesando pestaña " + pestanya);
				updateCCF(sheet);
			} else {
				LOGGER.warning("No existe la pestaña " + pestanya);
			}

			pestanya = "Cond.TRF";
			LOGGER.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				LOGGER.info("Procesando pestaña " + pestanya);
				updateTRF(sheet);
			} else {
				LOGGER.warning("No existe la pestaña " + pestanya);
			}

			pestanya = "Cond.TRFInternacional";
			LOGGER.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				LOGGER.info("Procesando pestaña " + pestanya);
				updateTRFInt(sheet);
			} else {
				LOGGER.warning("No existe la pestaña " + pestanya);
			}

			//
			pestanya = "Cond.TRFInternacional_PorNivel";
			LOGGER.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				LOGGER.info("Procesando pestaña " + pestanya);
				updateTRFInt_PorNivel(sheet);
			} else {
				LOGGER.warning("No existe la pestaña " + pestanya);
			}

			pestanya = "ReasignacionCargos";
			LOGGER.info("Localizando pestaña..." + pestanya);
			pos = workBook.getSheetIndex(pestanya);
			if (pos > -1) {
				sheet = workBook.getSheetAt(pos);
				LOGGER.info("Procesando pestaña " + pestanya);
				updateReasignacionCargos(sheet);
			} else {
				LOGGER.warning("No existe la pestaña " + pestanya);
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
				LOGGER.warning("Lista de errores.");
				for(String error : errores){
					LOGGER.warning("ERROR:[" + error + "]");
				}
			}

			LOGGER.info("Total modificaciones:" + this.getNumModificaciones());
			LOGGER.info("Total tiempo:" + lFin + "-" + lIni + "="
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
			if (!obligatorias.contains(c)) {
				LOGGER.info("*" + c + "* OJO NO está!");
				celdasOK = false;
			} else {
				LOGGER.info("*" + c + "* está!");
				existentes.add(c);
			}
		}// while (cells.hasNext ())
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
			LOGGER.info("Type not supported." + cell.getCellType());
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
	 */
	private void updateCCF(Sheet sheet) throws CeldasIncorrectasException,
			SQLException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet, cabObligatorias.get(CABECERAS_CCF), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_CCF, lista);

		LOGGER.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> filas = (Iterator<Row>) sheet.rowIterator();
		while (filas.hasNext()) {

			Row row = filas.next();
			
			if (row.getRowNum() == 0) { // resto filas

			} else {

				LOGGER.info("Fila:" + row.getRowNum());

				// Recogemos los datos del Excel...
				ConceptoFacturable condicion = fila2CF(row);

				// Localizamos esa entidad en la tabla por el Id...
				ConceptoFacturable cf = entityManager.find(ConceptoFacturable.class,
						condicion.getId());

				// no está...
				if (cf == null) {

					// miramos que esté el acuerdo y el concepto...
					LOGGER.info("CF no localizado!");
					Query query = entityManager.createNamedQuery("FindByAcuerdoCifTipoDeServicio");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("cf", condicion.getConceptoFacturable());
					query.setParameter("sv", condicion.getTipoDeServicio());
					try {
						cf = (ConceptoFacturable) query.getSingleResult();
						LOGGER.info("Localizado CF:" + cf.toString());

						// Solo modificamos si hay un precio especial...
						if (condicion.getPrecioEspecial().equalsIgnoreCase("SI")) {
							
							cf.setPrecioEspecial(condicion.getPrecioEspecial());
							cf.setTipoPrecioEspecial(condicion.getTipoPrecioEspecial());
							cf.setImporteAcuerdo(condicion.getImporteAcuerdo());
							
							update(cf);
							
							incNumModificaciones();
							LOGGER.info("Modificado CF:" + cf.toString());
						} else {
							LOGGER.info("Localizado CF " + cf.getId()
									+ ", sin precio especial!");
						}
					} catch (javax.persistence.NoResultException e) {
						LOGGER.warning("No existe el par acuerdo-conceptofacturable:"
								+ condicion.getAcuerdo()
								+ ":"
								+ condicion.getConceptoFacturable());
						
						save(condicion);
						
						LOGGER.warning("Guardado el CF :"
								+ condicion.toString());
						incNumModificaciones();
					} catch (javax.persistence.NonUniqueResultException e) {
						LOGGER.warning("Hay más de un resultado:"
								+ condicion.toString());
						errores.add(String.format("Hay más de un resultado para la tupla Acuerdo-Concepto Facturable-Tipo de Servicio:"
								, condicion.getAcuerdo()
								, condicion.getConceptoFacturable()
								, condicion.getTipoDeServicio()));
						cf = null;
					}
				} else {
					LOGGER.info("Localizado CF:" + cf.toString());
					cf.setPrecioEspecial(condicion.getPrecioEspecial());
					cf.setTipoPrecioEspecial(condicion
							.getTipoPrecioEspecial());
					cf.setImporteAcuerdo(condicion.getImporteAcuerdo());
					
					update(cf);
					
					LOGGER.info("Guardado CF:" + cf.toString());
					LOGGER.info("Localizando CF2... con id " + cf.getId());
					ConceptoFacturable cf2 = entityManager.find(
							ConceptoFacturable.class, cf.getId());
					LOGGER.info("Localizado CF2:" + cf2.toString());
					if (cf.getImporteAcuerdo() == cf2.getImporteAcuerdo()) {
						incNumModificaciones();
					} else {
						LOGGER.info("Error... en " + cf.toString());
					}
				}
			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}

	/**
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 */
	private void updateTRF(Sheet sheet) throws CeldasIncorrectasException,
			SQLException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet, cabObligatorias.get(CABECERAS_TRF), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_TRF, lista);

		LOGGER.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				LOGGER.info("Fila:" + row.getRowNum());

				Trafico condicion = fila2TRF(row);

				Trafico trf = entityManager.find(Trafico.class, condicion.getId());

				if (trf == null) {
					// Buscamos por acuerdo y ámbito de tráfico
					LOGGER.info("TRF no localizado!");
					LOGGER.info(String.format(BUSCA_CONDICION_TRF_POR_AMBITO
							, condicion.getAcuerdo()
							, condicion.getAmbitoDeTrafico()));
					
					Query query = entityManager.createNamedQuery("FindByAcuerdoAmbitoDeTrafico");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("at", condicion.getAmbitoDeTrafico());
					try {
						trf = (Trafico) query.getSingleResult();
						LOGGER.info("Localizado CF:" + trf.toString());
						trf.setPrecioEspecial(condicion.getPrecioEspecial());
						trf.setPrecioEspecial(condicion.getPrecioEspecial());
						trf.setPrecioPorMinuto(condicion.getPrecioPorMinuto());

						update(trf);
						
						LOGGER.info("Modificado TRF:" + trf.toString());
						incNumModificaciones();
					} catch (javax.persistence.NoResultException e) {
						LOGGER.warning("No existe el par acuerdo-ambito:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getAmbitoDeTrafico());

						save(trf);
						
						LOGGER.info("Guardado TRF:" + condicion.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						LOGGER.warning("Hay más de un resultado:"
								+ condicion.toString());
						errores.add(String.format("Hay más de un resultado para la tupla Acuerdo-Ambito de Tráfico:"
								, condicion.getAcuerdo()
								, condicion.getAmbitoDeTrafico()));
						trf = null;
					}
				} else {
					LOGGER.info("Localizado TRF:" + trf.toString());

					trf.setPrecioEspecial(condicion.getPrecioEspecial());
					trf.setTipoDescuento(condicion.getTipoDescuento());
					trf.setPrecioPorMinuto(condicion.getPrecioPorMinuto());
					trf.setPorcentajeDescuento(condicion
							.getPorcentajeDescuento());
					trf.setEstLlamada(condicion.getEstLlamada());

					update(trf);
					
					LOGGER.info("Modificado TRF:" + trf.toString());
					incNumModificaciones();

				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}

	/**
	 * 
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 */
	private void updateTRFInt(Sheet sheet) throws CeldasIncorrectasException,
			SQLException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet, cabObligatorias.get(CABECERAS_TRF_INT),
				lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_TRF_INT, lista);

		LOGGER.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				LOGGER.info("Fila:" + row.getRowNum());

				TraficoInternacional condicion = fila2TRFInt(row);

				TraficoInternacional tint = entityManager.find(TraficoInternacional.class, condicion.getId());
				if (tint == null) {
					// Buscamos por acuerdo y ámbito de tráfico
					LOGGER.info("TRFInt no localizado!");
					LOGGER.info(String.format(BUSCA_CONDICION_TRF_INT_POR_ACUERDO_Y_DESTINO
							, condicion.getAcuerdo()
							, condicion.getDestino()));
					Query query = entityManager.createNamedQuery("FindByAcuerdoDestino");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("pd", condicion.getDestino());
					try {
						tint = (TraficoInternacional) query.getSingleResult();
						LOGGER.info("Localizado TRFInt:" + tint.toString());
						tint.setPrecioEspecial(condicion.getPrecioEspecial());
						tint.setPrecioPorMinuto(condicion.getPrecioPorMinuto());

						update(tint);
						
						LOGGER.info("Modificado TRF:" + tint.toString());
						incNumModificaciones();
					} catch (javax.persistence.NoResultException e) {
						LOGGER.warning("No existe el par acuerdo-destino:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getDestino());

						save(condicion);
						
						LOGGER.info("Guardado TRFInt:" + condicion.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						LOGGER.warning("Hay más de un resultado para el par:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getDestino());
						errores.add(String.format("Hay más de un resultado para el par acuerdo-destino: [%s]-[%s]"
								, condicion.getAcuerdo()
								, condicion.getDestino()));
						tint = null;
					}

				} else {
					LOGGER.info("Localizado TRFInt:" + tint.toString());

					tint.setPrecioEspecial(condicion.getPrecioEspecial());
					tint.setTipoDescuento(condicion.getTipoDescuento());
					tint.setPrecioPorMinuto(condicion.getPrecioPorMinuto());
					tint.setPorcentajeDescuento(condicion.getPorcentajeDescuento());
					tint.setEstLlamada(condicion.getEstLlamada());

					update(tint);

					LOGGER.info("Modificado TRFInt:" + tint.toString());

					incNumModificaciones();

				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}

	/**
	 * 
	 * @param sheet
	 * @throws CeldasIncorrectasException
	 * @throws SQLException
	 */
	private void updateTRFInt_PorNivel(Sheet sheet)
			throws CeldasIncorrectasException, SQLException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet,
				cabObligatorias.get(CABECERAS_TRF_INT_POR_NIVEL), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_TRF_INT_POR_NIVEL, lista);

		LOGGER.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				LOGGER.info("Fila:" + row.getRowNum());

				TraficoInternacionalPorNivel condicion = fila2TRFInt_PorNivel(row);

				TraficoInternacionalPorNivel tint = entityManager.find(
						TraficoInternacionalPorNivel.class, condicion.getId());
				if (tint == null) {
					// Buscamos por acuerdo y ámbito de tráfico
					LOGGER.info("TRF no localizado!");
					LOGGER.info(String.format(BUSCA_CONDICION_TRF_INT_POR_NIVEL
							, condicion.getAcuerdo()
							, condicion.getNivel()));
					Query query = entityManager.createNamedQuery("FindByAcuerdoNivel");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("pd", condicion.getNivel());
					try {
						tint = (TraficoInternacionalPorNivel) query
								.getSingleResult();
						LOGGER.info("Localizado TRFInt:" + tint.toString());
						tint.setPrecioEspecial(condicion.getPrecioEspecial());
						tint.setPrecioPorMinuto(condicion.getPrecioPorMinuto());

						update(tint);
						
						LOGGER.info("Modificado TRF:" + tint.toString());
						incNumModificaciones();
					} catch (javax.persistence.NoResultException e) {
						LOGGER.warning("No existe el par acuerdo-nivel:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getNivel());

						save(tint);
						
						LOGGER.info("Guardado TRFInt:" + condicion.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						LOGGER.warning("Hay más de un resultado para el par:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getNivel());
						errores.add(String.format("Hay más de un resultado para el par acuerdo-nivel: [%s]-[%s]"
								, condicion.getAcuerdo()
								, condicion.getNivel()));

						tint = null;
					}

				} else {
					LOGGER.info("Localizado TRFInt:" + tint.toString());

					tint.setPrecioEspecial(condicion.getPrecioEspecial());
					tint.setTipoDescuento(condicion.getTipoDescuento());
					tint.setPrecioPorMinuto(condicion.getPrecioPorMinuto());
					tint.setPorcentajeDescuento(condicion
							.getPorcentajeDescuento());
					tint.setEstLlamada(condicion.getEstLlamada());

					update(tint);

					LOGGER.info("Modificado TRFInt:" + tint.toString());

					incNumModificaciones();

				}

			}// if (row.getRowNum() == 1)

		}// while (rows.hasNext ())
	}
	
	private void updateReasignacionCargos(Sheet sheet)
			throws CeldasIncorrectasException, SQLException {
		/**
		 * Comprobamos que las cabeceras de la primera fila sean correctas
		 */
		List<String> lista = new ArrayList<String>();
		if (!celdasCabeceraOK(sheet,
				cabObligatorias.get(CABECERAS_REAS_CARGO), lista))
			throw new CeldasIncorrectasException("Error en las Celdas");

		cabExistentes.put(CABECERAS_REAS_CARGO, lista);

		LOGGER.info(cabExistentes.toString());

		// Recorremos las filas
		Iterator<Row> rows2 = (Iterator<Row>) sheet.rowIterator();
		while (rows2.hasNext()) {

			Row row = rows2.next();
			if (row.getRowNum() == 0) { // resto filas
				// No se hace nada con la primera fila
			} else {

				LOGGER.info("Fila:" + row.getRowNum());

				// Recuperamos la condición del Excel
				ReasignacionCargo condicion = fila2ReasignacionCargo(row);

				// Localizamos el elemento de la tabla
				ReasignacionCargo reasignacion = entityManager.find(ReasignacionCargo.class, condicion.getId());
				
				if (reasignacion == null) {
					// Buscamos por acuerdo, cif, grupo de gasto y agrupación facturable
					LOGGER.info("ReasignacionCargo no localizado!");
					LOGGER.info("Buscamos la condición de Reasignacion por Cargo:");
					
					Query query = entityManager.createNamedQuery("FindByAcuerdoCifGrupoDeGastoAgrupacionFacturable");
					query.setParameter("ac", condicion.getAcuerdo());
					query.setParameter("c", condicion.getCif());
					query.setParameter("g", condicion.getGrupoDeGasto());
					query.setParameter("af", condicion.getAgrupacionFacturable());
					try {
						reasignacion = (ReasignacionCargo) query
								.getSingleResult();
						LOGGER.info("Localizado ReasignacionCargo:" + reasignacion.toString());
						reasignacion.setTipoDocReasignado(condicion.getTipoDocReasignado());
						reasignacion.setCifReasignado(condicion.getCifReasignado());
						reasignacion.setNombreClienteReasignado(condicion.getNombreClienteReasignado());
						reasignacion.setGrupoDeGastoReasignado(condicion.getGrupoDeGastoReasignado());
						reasignacion.setAgrupacionFacturableReasignado(condicion.getAgrupacionFacturableReasignado());

						update(reasignacion);

						LOGGER.info("Modificado ReasignacionCargo:" + reasignacion.toString());
						incNumModificaciones();
					} catch (javax.persistence.NoResultException e) {
						LOGGER.warning("No existe la tupla Acuerdo-Cif-GrupoDeGasto-AgrupacionFacturable:"
								+ condicion.getAcuerdo() + ":"
								+ condicion.getCif() + ":"
								+ condicion.getGrupoDeGasto() + ":"
								+ condicion.getAgrupacionFacturable()
								);

						save(reasignacion);
						
						LOGGER.info("Guardado ReasignacionCargo:" + condicion.toString());
					} catch (javax.persistence.NonUniqueResultException e) {
						LOGGER.warning("Hay más de un resultado para la tupla Acuerdo-Cif-GrupoDeGasto-AgrupacionFacturable:"
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
					LOGGER.info("Localizado ReasignacionCargo:" + reasignacion.toString());

					reasignacion.setTipoDocReasignado(condicion.getTipoDocReasignado());
					reasignacion.setCifReasignado(condicion.getCifReasignado());
					reasignacion.setNombreClienteReasignado(condicion.getNombreClienteReasignado());
					reasignacion.setGrupoDeGastoReasignado(condicion.getGrupoDeGastoReasignado());
					reasignacion.setAgrupacionFacturableReasignado(condicion.getAgrupacionFacturableReasignado());

					update(reasignacion);

					LOGGER.info("Modificado ReasignacionCargo:" + reasignacion.toString());

					incNumModificaciones();

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

				// LOGGER.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					double lTmp = Double.parseDouble(strCelda);
					long id = Math.round(lTmp);
					// LOGGER.info("id:\t" + id);
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

				// LOGGER.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// LOGGER.info(String.format("ID: [%d]", id));
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

				// LOGGER.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// LOGGER.info("id:\t" + id);
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

				// LOGGER.info(String.format("[%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// LOGGER.info("id:\t" + id);
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

				// LOGGER.info(String.format("Columna [%d] campo [%s]", colNum, campo));
				if (campo.equals(ID)) {
					long id = Math.round(cell.getNumericCellValue());
					// LOGGER.info("id:\t" + id);
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
}
