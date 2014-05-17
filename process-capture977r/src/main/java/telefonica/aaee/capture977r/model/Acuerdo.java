package telefonica.aaee.capture977r.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the tbl_acuerdos database table.
 * 
 */
@Entity
@Table(name = "tbl_acuerdos")
@NamedQueries({
	@NamedQuery(name = "Acuerdo.findAll", query = "SELECT c FROM Acuerdo c"),
	@NamedQuery(name = "Acuerdo.findByNombre", query = "SELECT p "
			+ "FROM Acuerdo p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.acuerdo = :nom ")
})
@XmlRootElement
public class Acuerdo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String acuerdo;

	public String getAcuerdo() {
		return this.acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}
	
	
	

	public Acuerdo() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuerdo == null) ? 0 : acuerdo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Acuerdo [id=");
		builder.append(id);
		builder.append(", acuerdo=");
		builder.append(acuerdo);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String acuerdo;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder acuerdo(String acuerdo) {
			this.acuerdo = acuerdo;
			return this;
		}

		public Acuerdo build() {
			return new Acuerdo(this);
		}
	}

	private Acuerdo(Builder builder) {
		this.id = builder.id;
		this.acuerdo = builder.acuerdo;
	}
}