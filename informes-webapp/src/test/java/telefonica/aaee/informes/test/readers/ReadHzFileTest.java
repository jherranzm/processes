package telefonica.aaee.informes.test.readers;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import telefonica.aaee.informes.process.ReadHzFile;

public class ReadHzFileTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testExecute() {
		ReadHzFile procesor = new ReadHzFile();
		procesor.setHZFile(new File("/Users/jherranzm/dev/testFiles/HZ_2012-2S+2013.htm"));
		procesor.setRealPath("/Users/jherranzm/dev/testFiles/");
		assertTrue(procesor.execute());
	}

}
