/**
 * 
 */
package telefonica.aaee.excel.model;

import javax.sql.rowset.CachedRowSet;

/**
 * @author t130796
 *
 */
public class ContenidoHojaExcel 
	extends PestanyaExcel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CachedRowSet crs;
	private long numRegs; 
	private long numFilaInicial; 

	/**
	 * @param consulta, nombre de la consulta SQL
	 * @param crs (CachedRowSet)
	 * @param numRegs, long número de registros
	 * @param nombre, nombre del rango
	 * @param nombrePestanya, nombre de la pestaña
	 * @param numFilaInicial, fila inicial de la hoja Excel
	 */
	public ContenidoHojaExcel(
			String consulta,
			CachedRowSet crs,
			long numRegs,
			String nombre,
			String nombrePestanya,
			long numFilaInicial) {
		super();
		
		this.crs = crs;
		this.numRegs = numRegs;
		this.consulta = consulta;
		this.nombre = nombre;
		this.nombrePestanya = nombrePestanya;
		this.numFilaInicial = numFilaInicial;
	}
	
	/**
	 * @param consulta, nombre de la Consulta
	 * @param nombre, nombre del rango
	 * @param nombrePestanya, nombre de la pestaña
	 */
	public ContenidoHojaExcel(
			String consulta,
			String nombre,
			String nombrePestanya,
			long numFilaInicial) {
		super();
		
		this.consulta = consulta;
		this.nombre = nombre;
		this.nombrePestanya = nombrePestanya;
		this.numFilaInicial = numFilaInicial;
	}
	
	/**
	 * @param consulta, nombre de la Consulta
	 * @param nombre, nombre del rango
	 * @param nombrePestanya, nombre de la pestaña
	 */
	public ContenidoHojaExcel(
			String consulta,
			String nombre,
			String nombrePestanya) {
		super();
		
		this.consulta = consulta;
		this.nombre = nombre;
		this.nombrePestanya = nombrePestanya;
		this.numFilaInicial = 0;
	}
	
	public ContenidoHojaExcel(){};
	
	
	
	public CachedRowSet getCrs() {
		return crs;
	}


	public void setCrs(CachedRowSet crs) {
		this.crs = crs;
	}


	public long getNumRegs() {
		return numRegs;
	}


	public void setNumRegs(long numRegs) {
		this.numRegs = numRegs;
	}
	
	
	/**
	 * @return numFilaInicial
	 */
	public long getNumFilaInicial() {
		return numFilaInicial;
	}

	
	/**
	 * @param numFilaInicial valor a asignar al campo numFilaInicial
	 */
	public void setNumFilaInicial(long numFilaInicial) {
		this.numFilaInicial = numFilaInicial;
	}



}
