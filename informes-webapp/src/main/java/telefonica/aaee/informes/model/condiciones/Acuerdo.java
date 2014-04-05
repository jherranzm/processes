package telefonica.aaee.informes.model.condiciones;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the Acuerdo977R database table.
 * 
 */
@Entity
@Table(name="tbl_acuerdos")
@NamedQueries({
	@NamedQuery(name = "Acuerdo.findAll", query = "SELECT c FROM Acuerdo c"),
	@NamedQuery(name = "Acuerdo.findByNombre", query = "SELECT p "
			+ "FROM Acuerdo p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.acuerdo = :nom ") 
})
@XmlRootElement
public class Acuerdo 

	implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="acuerdo", nullable = false)
	private String acuerdo;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_carga", nullable = false)
	private Date fechaCarga;


	
	
	

    public Acuerdo() {
    }

	

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcuerdo() {
		return this.acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}



	
	/**
	 * @return fechaCarga
	 */
	public final Date getFechaCarga() {
		return fechaCarga;
	}



	
	/**
	 * @param fechaCarga valor a asignar al campo fechaCarga
	 */
	public final void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Acuerdo [id=");
		builder.append(id);
		builder.append(", acuerdo=");
		builder.append(acuerdo);
		builder.append(", fechaCarga=");
		builder.append(fechaCarga);
		builder.append("]");
		return builder.toString();
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuerdo == null) ? 0 : acuerdo.hashCode());
		result = prime * result
				+ ((fechaCarga == null) ? 0 : fechaCarga.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Acuerdo other = (Acuerdo) obj;
		if (acuerdo == null) {
			if (other.acuerdo != null)
				return false;
		} else if (!acuerdo.equals(other.acuerdo))
			return false;
		if (fechaCarga == null) {
			if (other.fechaCarga != null)
				return false;
		} else if (!fechaCarga.equals(other.fechaCarga))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
}