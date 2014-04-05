package telefonica.aaee.informes.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import telefonica.aaee.informes.model.FileInfoDTO;

public class ReadIIFile {

	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * Constantes
	 */
	private static final String II_SERVLET = "https://edomus.tesa/ii/priv/seg00/servlet/";
	private static final String TIPO_CONCERTADA = "IIBB00JSGestorLOFXBBnas977";
	private static final String TIPO_ADSL = "IIBB00JSGestorLOAPCBB977";

	private File iiFile = null;
	private String fileName = null;
	private String result = null;
	private ArrayList<FileInfoDTO> vEnlaces = new ArrayList<FileInfoDTO>();



	/**
	 * @return true or false
	 */
	public boolean execute() {

		boolean ret = false;
		if (readIIFile(iiFile)) {
			logger.info("OK");
			ret = true;
		} else {
			logger.error("error!");
		}

		return ret;
	}
	
	
	/**
	 * 
	 * @param f
	 * @return
	 */
	private boolean readIIFile(File f) {

		boolean ret = false;
		logger.info("[INFO]ReadIIFile.readIIFile().HZFile:" + iiFile);

		BufferedReader buffReader;

		final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		final String s = formatter.format(new Date());

		if (f.exists()) {
				logger.info("Existeix el fitxer:" + iiFile);

			try {
				buffReader = new BufferedReader(new FileReader(iiFile));

				String line;
				int fila = 0;

				line = buffReader.readLine();

				/**
				 * Es llegeix el fitxer línia per línia
				 */

				while (true) {

					if (line == null) {
						break;
					}

					final Pattern pattern1 = Pattern.compile("(.*)" + TIPO_CONCERTADA + "?(.*)\"");
					final Pattern pattern2 = Pattern.compile("(.*)" + TIPO_ADSL + "?(.*)\"");
					final Matcher matcher1 = pattern1.matcher(line);
					final Matcher matcher2 = pattern2.matcher(line);
					final boolean matchFound1 = matcher1.matches();
					final boolean matchFound2 = matcher2.matches();

					if (matchFound1) {

						final String strValue = new String(matcher1.group(2));
						logger.info("value:" + strValue);
						
						FileInfoDTO enlaceII = getEnlaceIIFromConcertada(strValue);
						if (enlaceII != null) {
							vEnlaces.add(enlaceII);
						}
						logger.info("totalFilas = fila;" + fila);
						fila++;
						
					} else if (matchFound2) {
						final String strValue = new String(matcher2.group(2));
						logger.info("value:[" + strValue + "]");
						
						FileInfoDTO enlaceII = getEnlaceIIFromADSL(strValue);
						if (enlaceII != null){
							vEnlaces.add(enlaceII);
						}

						logger.info("totalFilas = fila;" + fila);
						fila++;

					}// if(matchFound1)
					line = buffReader.readLine();
				}

				ret = true;

				buffReader.close();

				setResult(s + ".html");

			} catch (IOException ioe) {
				logger.fatal("IOException: " + ioe);
			} catch (Exception e) {
				logger.fatal("Exception: " + e);
			}
		} else {
			logger.fatal("No existeix... " + iiFile + " !!!");

		}// if

		return ret;

	}

	private FileInfoDTO getEnlaceIIFromConcertada(String strValue) {

		/**
		 * 
		 * 2010-02-02
		 * 
		 * ./IIBB00JSGestorLOFXBBnas977? 0. PAGE=130& 1. FIGR=N& 2.
		 * FECHA=28-12-2009& 3.
		 * FILE=CO.20091228.LG58286303--012009.LG58286303.000
		 * -------.EUROS.05+00.REN.COC04985+DIC.TE& 4. NIFCIF=LG58286303& 5.
		 * TAMA=802.658& 6. PAFINI=20090701& 7. PAFFIN=20091231
		 * 
		 * ***
		 * 
		 * 0. PAGE=130& 1. FECHA=28-06-2007& 2.
		 * FILE=VA.20070628.LA08000820--012003.
		 * LB82933706.000-------.EUROS.05+00.REN.COV00072+JUN.TE& 3.
		 * NIFCIF=LA08000820& 4. TAMA=2.515& 5. PAFINI=20070101& 6.
		 * PAFFIN=20070630
		 * 
		 * Se necesitan los campos FILE TAMA NIFCIF
		 * 
		 * ENVIO
		 * 
		 * Podemos dejar los campos como están, modificando el campo FILE
		 * añadiendo por delante YYY/NIFNIFNIF donde YYY son las 3 primeras
		 * letras de NIFCIF y añadir el campo ENVIO de manera que sea el nombre
		 * del fichero que descargaremos.
		 * 
		 */

		FileInfoDTO fileInfo = new FileInfoDTO();
		//
		String[] result = strValue.split("&");
		Map<String, String> parametros = new HashMap<String, String>();
		for (String param : result) {
			String[] keyValue = param.split("=");
			parametros.put(keyValue[0], keyValue[1]);
		}
		logger.info("FECHA=#" + parametros.get("FECHA") + "#");
		logger.info("FILE=#" + parametros.get("FILE") + "#");
		logger.info("NIFCIF=#" + parametros.get("NIFCIF") + "#"); // LA08000820

		String bytes = parametros.get("TAMA");// result[5];

		logger.info("YYY=#"
				+ parametros.get("NIFCIF").substring(1).substring(0, 3) + "#");// A08
		String format = parametros.get("FILE").split(".EUROS.")[1].substring(0,
				2);
		//
		if (format.equals("05")) {
			format = "FACTEL5_977R";
			String ENVIO = parametros.get("FILE").split(".EUROS.")[0] + "_"
					+ format + ".zip";
			String newFILE = "FILE="
					+ parametros.get("NIFCIF").substring(1).substring(1, 3)
					+ "/" + parametros.get("NIFCIF").substring(1) + "/"
					+ parametros.get("FILE") + "&ENVIO=" + ENVIO + "&"
					+ "NIFCIF=" + parametros.get("NIFCIF") + "&" + "TAMA="
					+ bytes;
			
			fileInfo.setUrl(II_SERVLET
							+ TIPO_CONCERTADA + "?" + newFILE + "#");
			fileInfo.setFileSize(Long.valueOf(bytes.replaceAll("[.,]", "")));
			fileInfo.setFileName(ENVIO);
//			numeroLinea++;

//			logger.info("numeroLinea:" + numeroLinea);
		} else {
			format = "FACTEL4_977";
			return null;
		}

		return fileInfo;

	}

	private FileInfoDTO getEnlaceIIFromADSL(String strValue) {

		/**
		 * 
		 * 2010-06-11
		 * 
		 * 0. PAGE=437& 1. FECHA=19-06-2010& 2.
		 * FILE=APC.20100619.B08000135.08088725875
		 * .IVA.EUROS.05+00.REN.LMR00487+JUN.TE& 3. TAMA=13.957& 4.
		 * NIFCIF=LB08000135& 5. AGF=08088725875& 6. PAFINI=NO& 7. PAFFIN=NO
		 * 
		 * ***
		 * https://edomus.tesa/ii/priv/seg00/servlet/IIBB00JSGestorLOAPCBB977?
		 * 
		 * 0. PAGE=437& 1. FECHA=19-06-2010& 2.
		 * FILE=APC.20100619.B08000135.08088725875
		 * .IVA.EUROS.05+00.REN.LMR00487+JUN.TE& 3. TAMA=13.957& 4.
		 * NIFCIF=LB08000135& 5. AGF=08088725875& 6. PAFINI=NO& 7. PAFFIN=NO#
		 * 
		 * Se necesitan los campos FILE TAMA NIFCIF
		 * 
		 * ENVIO
		 * 
		 * Podemos dejar los campos como están, modificando el campo FILE
		 * añadiendo por delante YYY/NIFNIFNIF donde YYY son las 3 primeras
		 * letras de NIFCIF y añadir el campo ENVIO de manera que sea el nombre
		 * del fichero que descargaremos.
		 * 
		 */

		logger.info("procesaLineaADSL:" + "[" + strValue + "]");


		FileInfoDTO fileInfo = new FileInfoDTO();
		String[] result = strValue.split("&");

		String FECHA = result[1];
		logger.info("FECHA:" + FECHA);
		String[] aFECHA = FECHA.split("=");
		FECHA = aFECHA[1];
		logger.info("FECHA:" + FECHA);

		String FILE = result[2];
		String[] aFILE = FILE.split("=");
		FILE = aFILE[1];
		logger.info("FILE:" + FILE);
		String NIFCIF = result[4];
		logger.info("NIFCIF:" + NIFCIF);
		String TAMA = result[3];
		String[] aTAMA = TAMA.split("=");
		String bytes = aTAMA[1];
		logger.info("bytes:" + bytes);
		String[] aNIFCIF = NIFCIF.split("=");
		String valueNIFCIF = aNIFCIF[1];
		logger.info("valueNIFCIF:" + valueNIFCIF);
		valueNIFCIF = valueNIFCIF.substring(1);
		String YYY = valueNIFCIF.substring(1, 3);
		logger.info("YYY=#" + YYY + "#");// A08
		String[] aENVIO = FILE.split(".EUROS.");
		logger.info("aENVIO:" + aENVIO.length);
		if (aENVIO.length > 1) {
			String format = aENVIO[1].substring(0, 2);
			logger.info("format:" + format);

			if (format.equals("05")) {
				format = "FACTEL5_977R";
				String ENVIO = aENVIO[0] + "_" + format + ".zip";
				// String newFILE = strValue.substring(1) + "&ENVIO=" + ENVIO;
				String newFILE = "FILE=" + YYY + "/" + valueNIFCIF + "/" + FILE
						+ "&ENVIO=" + ENVIO + "&" + NIFCIF + "&" + TAMA;
				fileInfo.setUrl(II_SERVLET
						+ TIPO_ADSL + "?" + newFILE + "#");
				fileInfo.setFileSize(Long.valueOf(bytes));
				fileInfo.setFileName(ENVIO);
//				numeroLinea++;
			} else {
				format = "FACTEL4_977";
				return null;
			}
		} else {
			logger.info(" linea is NULL.");
			return null;
		}

		return fileInfo;

	}

	/**
	 * @return Returns the IIFile.
	 */
	public File getIIFile() {
		return iiFile;
	}

	/**
	 * @param IIFile
	 *            The IIFile to set.
	 */
	public void setIIFile(File IIFile) {
		this.iiFile = IIFile;
	}

	/**
	 * @return Returns the result.
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            The result to set.
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @param vEnlaces
	 *            the vEnlaces to set
	 */
	public void setvEnlaces(ArrayList<FileInfoDTO> vEnlaces) {
		this.vEnlaces = vEnlaces;
	}

	/**
	 * @return the vEnlaces
	 */
	public ArrayList<FileInfoDTO> getvEnlaces() {
		return vEnlaces;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		this.iiFile = new File(this.fileName);
	}

}