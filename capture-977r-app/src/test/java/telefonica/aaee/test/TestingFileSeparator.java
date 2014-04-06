package telefonica.aaee.test;

import java.io.File;

import org.junit.Test;

public class TestingFileSeparator {

	@Test
	public void test() {
		System.out.println(File.separator);
		
		String outPathName = "V:/Clientes/a/AJUNTAMENT_DE_LHOSPITALET/REGs/2012/REG_2012_3T/";
		System.out.println(outPathName);

		if ((outPathName.indexOf('/') !=-1) && (outPathName.indexOf('\\') !=-1)) {
			outPathName = outPathName.replace('\\', File.separator.charAt(0));
			outPathName = outPathName.replace('/', File.separator.charAt(0));
        }
		System.out.println(outPathName);
		
		outPathName = outPathName.replaceAll("//", File.separator);
		System.out.println(outPathName);
		
		if(!outPathName.isEmpty()){
            if(!outPathName.endsWith(File.separator)){
            	outPathName = outPathName.concat(File.separator);
            }
        }
		System.out.println(outPathName);
	}

}
