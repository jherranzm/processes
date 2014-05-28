package telefonica.aaee.excel.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class RangoTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RangoTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( RangoTest.class );
    }

	public void testGetRango() {
		int _f = 33;
		int _c = 26;
		String res = getRango(_f, _c);
		assertTrue(res.equals("Z2:Z33"));
	}

	public void testGetRangoA2A33() {
		int _f = 33;
		int _c = 1;
		String res = getRango(_f, _c);
		assertTrue(res.equals("A2:A33"));
	}

	public void testGetRangoCol2() {
		int _f = 33;
		int _c = 2;
		String res = getRango(_f, _c);
		assertTrue(res.equals("B2:B33"));
	}

	public void testGetRangoCol27() {
		int _f = 33;
		int _c = 27;
		String res = getRango(_f, _c);
		assertTrue(res.equals("AA2:AA33"));
	}

	private String getRango(int _f, int _c) {
		System.out.println("");
		System.out.println("Fila:"+_f);
		System.out.println("Columna:"+_c);
		final String nomColumnes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final StringBuffer nomColumna = new StringBuffer();
		long maxFila = _f;
		int indexCol = _c;
		if(indexCol > nomColumnes.length()){
			int pos1 = indexCol % nomColumnes.length();
			System.out.println("Posición:"+pos1);
			int fl = (int) (indexCol / nomColumnes.length());
			System.out.println("fl:"+fl);
			if (fl > 0) nomColumna.append(nomColumnes.charAt(fl -1));
			System.out.println(nomColumna);
			if (pos1 > 0) nomColumna.append(nomColumnes.charAt(pos1 - 1));
			System.out.println(nomColumna);
		}else if (indexCol > 0) {
			nomColumna.append(nomColumnes.charAt(_c -1));
			System.out.println(nomColumna);
		} else {
			nomColumna.append("A");
		}
	
	
		String res = "" + nomColumna + "2:"  +  nomColumna  + "" +  maxFila;
		System.out.println(res);
		return res;
	}

}
