package telefonica.aaee.util.file;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class HelperFile {


	protected final static Log logger = LogFactory.getLog(HelperFile.class);
	
	
	public static boolean cleandirs(String dirName) {
		
		File dir = new File(dirName);
		
		return deleteDir(dir);
	}
	
	public static boolean deleteDir(File dir){
		if(dir.isDirectory()){
			// Localizamos los ficheros
			String[] children = dir.list();
			for(String file : children){
				boolean success = deleteDir(new File(dir, file));
				if(!success){
					logger.fatal("No se puede borrar el fichero " + dir + File.separator + file);
					return false;
				}else{
					logger.fatal("Borrado el fichero: " + dir + File.separator + file);
				}
			}
		}
		return dir.delete();
	}

}
