package telefonica.aaee.informes.form;

import java.io.Serializable;

import telefonica.aaee.informes.model.Consulta;

public class PestanyaForm   implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String nombre;
	private String rango;
	private int numFilaInicial;
	private long consultaSQL_id;
	private Consulta consulta;

	public PestanyaForm() {
		super();
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDefinicion() {
		return getRango();
	}
	
	public void setDefinicion(String definicion) {
		this.setRango(definicion);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}



	public int getNumFilaInicial() {
		return numFilaInicial;
	}
	public void setNumFilaInicial(int numFilaInicial) {
		this.numFilaInicial = numFilaInicial;
	}
	
	public String getRango() {
		return rango;
	}
	public void setRango(String rango) {
		this.rango = rango;
	}

	public long getConsultaSQL_id() {
		return consultaSQL_id;
	}

	public void setConsultaSQL_id(long consultaSQL_id) {
		this.consultaSQL_id = consultaSQL_id;
	}


	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PestanyaForm [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", rango=");
		builder.append(rango);
		builder.append(", numFilaInicial=");
		builder.append(numFilaInicial);
		builder.append(", consultaSQL_id=");
		builder.append(consultaSQL_id);
		builder.append(", consulta=");
		builder.append(consulta);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	

	
	
	
	
	
	
	/**
	 * 
	 */
	
	
}
