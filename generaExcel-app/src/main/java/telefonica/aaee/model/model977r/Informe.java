package telefonica.aaee.model.model977r;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="tbl_informesxls")
@XmlRootElement
public class Informe 
	extends telefonica.aaee.xml.GenericXML

	implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
	private String nombre;
	
	@ManyToMany
	@JoinTable(
			name="tbl_Informe_Pestanya", 
	        joinColumns=@JoinColumn(name="informe_id"),
	        inverseJoinColumns=@JoinColumn(name="pestanya_id")
			)
	@OrderColumn(name="orden")
	private List<Pestanya> pestanyes;
	
	
	
	
	public Informe() {
		pestanyes = new ArrayList<Pestanya>();
	}
	/**
	 * @return pestanyes
	 */
	public List<Pestanya> getPestanyes() {
		return pestanyes;
	}
	/**
	 * @param pestanyes valor a asignar al campo pestanyes
	 */
	public void setPestanyes(List<Pestanya> pestanyes) {
		this.pestanyes = pestanyes;
	}
	/**
	 * @return nombre
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

}
