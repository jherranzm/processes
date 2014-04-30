package telefonica.aaee.model.model977r;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="tbl_pestanyes")
@XmlRootElement
public class Pestanya
	extends telefonica.aaee.xml.GenericXML

	implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private ConsultaSQL consulta;
	private String nombre;
	private String rango;
	
	private long numFilaInicial = 0;
	
	@ManyToMany
	@JoinTable(
			name="tbl_informe_pestanya", 
	        joinColumns=@JoinColumn(name="pestanya_id"),
	        inverseJoinColumns=@JoinColumn(name="informe_id")
			)
	private List<Informe> informes;
	
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
	
	/**
	 * @return nombre de la pestaña
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
	 * @return rango
	 */
	public String getRango() {
		return rango;
	}
	
	/**
	 * @param rango valor a asignar al campo rango
	 */
	public void setRango(String rango) {
		this.rango = rango;
	}

	/**
	 * @return consulta
	 */
	public ConsultaSQL getConsulta() {
		return consulta;
	}

	/**
	 * @param consulta valor a asignar al campo consulta
	 */
	public void setConsulta(ConsultaSQL consulta) {
		this.consulta = consulta;
	}

	/**
	 * @return informes
	 */
	public List<Informe> getInformes() {
		return informes;
	}

	/**
	 * @param informes valor a asignar al campo informes
	 */
	public void setInformes(List<Informe> informes) {
		this.informes = informes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pestanya [id=" + id + ",\\n nombre=" + nombre + ",\\n rango=" + rango 
				+ ",\\n numFilaInicial=" + numFilaInicial
				+ ",\\n consulta=" + consulta
				+ "]";
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
