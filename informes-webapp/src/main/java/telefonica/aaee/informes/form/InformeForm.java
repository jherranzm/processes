package telefonica.aaee.informes.form;

import java.io.Serializable;
import java.util.Arrays;

import telefonica.aaee.informes.model.Pestanya;

public class InformeForm   implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String nombre;
	
	private Pestanya[] pestanyes;
	
	
	
	
	
	public InformeForm() {
		super();
	}
	
	
	
	
	
	
	
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}









	public Pestanya[] getPestanyes() {
		return pestanyes;
	}









	public void setPestanyes(Pestanya[] pestanyes) {
		this.pestanyes = pestanyes;
	}









	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InformeForm [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", pestanyes=");
		builder.append(Arrays.toString(pestanyes));
		builder.append("]");
		return builder.toString();
	}

	
	
	

}