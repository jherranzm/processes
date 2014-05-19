package telefonica.aaee.excel.model;

import java.io.Serializable;

public class PestanyaExcel 

	implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;

	protected String consulta;
	protected String nombre;
	protected String nombrePestanya;
	
	private long numFilaInicial = 0;
	
	

	
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

	public PestanyaExcel() {
		super();
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombrePestanya() {
		return nombrePestanya;
	}

	public void setNombrePestanya(String nombrePestanya) {
		this.nombrePestanya = nombrePestanya;
	}

	/**
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id valor a asignar al campo id
	 */
	public void setId(long id) {
		this.id = id;
	}

}