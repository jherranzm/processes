/**
 * 
 */
package telefonica.aaee.app;

import telefonica.aaee.capture977.Split977;
import telefonica.aaee.capture977.Split977Config;

/**
 * @author Usuario
 *
 */
public class Capture977App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean bDetalleLlamadas = false;
		boolean bDetalleLlamadasRI = false;
		
		if(args.length < 4){
			System.err.println("Faltan argumentos...");
			System.exit(1);
		}else if(args[0] == null || args[0].length() == 0){
			System.err.println("Falta el acuerdo");
			System.exit(1);
		}else if(args[1] == null || args[1].length() == 0){
			System.err.println("Falta la ruta");
			System.exit(1);
		}else if(args[2] == null || args[2].length() == 0){
			System.err.println("Falta el detalle de tráfico");
			System.exit(1);
		}else if(args[3] == null || args[3].length() == 0){
			System.err.println("Falta el detalle de tráfico ri");
			System.exit(1);
		} 
		
		System.out.println("0:" + args[0]);
		System.out.println("1:" + args[1]);
		System.out.println("2:" + args[2]);
		System.out.println("3:" + args[3]);
		
		String acuerdo = args[0];
		String path = args[1];
		if(args[2].equals("1")) bDetalleLlamadas = true;
		if(args[3].equals("1")) bDetalleLlamadasRI = true;
		
		
		try {
			Split977 sp = new Split977();
			
			Split977Config config = new Split977Config.Builder()
			.acuerdo(acuerdo)
			.detalleLlamadas(bDetalleLlamadas)
			.detalleLlamadasRI(bDetalleLlamadasRI)
			.directorioZipFiles(path)
			.directorioOut(path)
			.build();
		
		sp.setConfig(config);
			

			if(sp.getFicheros().length > 0 ){
				sp.execute();
				System.out.println("Se ha tardado:" + sp.getTiempoEmpleado()/1000 + " segundos!");
			}else{
				System.err.println("Sin ficheros a tratar...");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
