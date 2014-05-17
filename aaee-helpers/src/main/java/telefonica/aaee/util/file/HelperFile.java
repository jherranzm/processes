package telefonica.aaee.util.file;

import java.io.File;
import java.util.logging.Logger;


public class HelperFile {


	private static final Logger LOGGER = Logger.getLogger(HelperFile.class.getCanonicalName());
	
	
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
					LOGGER.warning("No se puede borrar el fichero " + dir + File.separator + file);
					return false;
				}else{
					LOGGER.info("Borrado el fichero: " + dir + File.separator + file);
				}
			}
		}
		return dir.delete();
	}

}
