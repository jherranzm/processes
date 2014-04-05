package telefonica.aaee.model.model977r;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="tbl_ConsultasSQL")
@XmlRootElement
public class ConsultaSQL 
	
	extends telefonica.aaee.xml.GenericXML

	implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
	
	private String nombre;
	
	@Lob
	private String definicion;
	
	
	
	
	
	
	
	public ConsultaSQL() {
		super();
	}

	// Getters and Setters
	
	
	

	
	/**
	 * @return nombre de la consulta SQL
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * @param nombre valor a asignar al campo nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @return definicion
	 */
	public String getDefinicion() {
		return definicion;
	}
	
	/**
	 * @param definicion valor a asignar al campo definicion
	 */
	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id valor a asignar al campo id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConsultaSQL [id=" + id + ",\\n nombre=" + nombre + ",\\n definicion=" + definicion + "]";
	}
	
	
	
}
