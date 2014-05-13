package telefonica.aaee.segmentacion.util.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import telefonica.aaee.segmentacion.model.Exportable;
import telefonica.aaee.segmentacion.util.Constantes;

public class ToCSVFileWriter {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private Hashtable<String, BufferedWriter> bwOut = new Hashtable<String, BufferedWriter>();
	private Hashtable<String, File> filesOut = new Hashtable<String, File>();
	private Hashtable<String, String> fileNames = new Hashtable<String, String>();
	
	private String dir = "";
	
	public int printToCSVFile(List<Exportable> lista, String entidad){
		int numRows = 0;
		
		String fOutName = this.getDir() + File.separator + entidad;
		fOutName = FilenameUtils.normalize(fOutName + ".txt");
		logger.info("Fichero CSV normalizado:" + fOutName);
		fileNames.put(entidad, fOutName);
		try {
			BufferedWriter bwOut = getBROut(fOutName);
			
			for(Exportable element : lista){
//				logger.info(element.toCSV());
				bwOut.write("" + element.toCSV() + Constantes.CRLF);
			}
			
			bwOut.flush();
			bwOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return numRows;
	}

	public int printToTXTFile(List<String> lista, String entidad){
		int numRows = 0;
		
		String fOutName = this.getDir() + File.separator + entidad;
		fOutName = FilenameUtils.normalize(fOutName + ".txt");
		logger.info("Fichero TXT normalizado:" + fOutName);
		try {
			BufferedWriter bwOut = getBROut(fOutName);
			
			for(String element : lista){
				bwOut.write("" + element + Constantes.CRLF);
			}
			
			bwOut.flush();
			bwOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return numRows;
	}

	private BufferedWriter getBROut(String fOutName) {

		/**
		 * 
		 * El método busca el fichero en la HashTable, si lo encuentra devuelve
		 * el Objeto sino crea uno nuevo
		 */
		logger.info("Fichero clave:" + fOutName);
		File fileOut = null;
		BufferedWriter out = null;
		try {
			if (filesOut.get(fOutName) == null) {
				// creamos un nuevo fOut
				fileOut = new File(fOutName);
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(fileOut),
						Constantes.CODIFICACION_FICHERO_ORIGEN));
				filesOut.put(fOutName, fileOut);
				bwOut.put(fOutName, out);
				logger.info("Fichero:" + fOutName);
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

	
	
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public Hashtable<String, String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(Hashtable<String, String> fileNames) {
		this.fileNames = fileNames;
	}
}
