package telefonica.aaee.informes.form;

import java.io.Serializable;

public class ConsultaForm   implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String nombre;
	private String definicion;

	public ConsultaForm() {
		super();
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDefinicion() {
		return definicion;
	}
	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}
	

	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("ConsultaSQLForm [id=");
		builder2.append(getId());
		builder2.append(", nombre=");
		builder2.append(nombre);
		builder2.append(", definicion=");
		builder2.append(definicion);
		builder2.append("]");
		return builder2.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
	
	

}