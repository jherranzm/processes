package telefonica.aaee.segmentacion.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import telefonica.aaee.segmentacion.util.Constantes;

@Entity
@Table(name = "tbl_subsector")
@NamedQueries({
	@NamedQuery(name = "SubSector.findAll", query = "SELECT c FROM SubSector c"),
	@NamedQuery(name = "SubSector.findByNombre", query = "SELECT p "
			+ "FROM SubSector p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.nomSubSector = :nom "),
	@NamedQuery(name = "SubSector.findByCodigo", query = "SELECT p "
			+ "FROM SubSector p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.codSubSector = :cod ") 
})
public class SubSector implements Serializable, Exportable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_SubSector")
	private String codSubSector;

	@Column(name = "Nom_SubSector")
	private String nomSubSector;

	public SubSector() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodSubSector() {
		return codSubSector;
	}

	public void setCodSubSector(String codSubSector) {
		this.codSubSector = codSubSector;
	}

	public String getNomSubSector() {
		return nomSubSector;
	}

	public void setNomSubSector(String nomSubSector) {
		this.nomSubSector = nomSubSector;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubSector [id=");
		builder.append(id);
		builder.append(", codSubSector=");
		builder.append(codSubSector);
		builder.append(", nomSubSector=");
		builder.append(nomSubSector);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codSubSector == null) ? 0 : codSubSector.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomSubSector == null) ? 0 : nomSubSector.hashCode());
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
		SubSector other = (SubSector) obj;
		if (codSubSector == null) {
			if (other.codSubSector != null)
				return false;
		} else if (!codSubSector.equals(other.codSubSector))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomSubSector == null) {
			if (other.nomSubSector != null)
				return false;
		} else if (!nomSubSector.equals(other.nomSubSector))
			return false;
		return true;
	}

	public static class Builder {
		private Long id;
		private String codSubSector;
		private String nomSubSector;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder codSubSector(String codSubSector) {
			this.codSubSector = codSubSector;
			return this;
		}

		public Builder nomSubSector(String nomSubSector) {
			this.nomSubSector = nomSubSector;
			return this;
		}

		public SubSector build() {
			return new SubSector(this);
		}
	}

	private SubSector(Builder builder) {
		this.id = builder.id;
		this.codSubSector = builder.codSubSector;
		this.nomSubSector = builder.nomSubSector;
	}
	
	public String toCSV(){
		StringBuilder sb = new StringBuilder();
		sb
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.codSubSector)
		.append(Constantes.COMILLAS_DOBLES).append(";")
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.nomSubSector)
		.append(Constantes.COMILLAS_DOBLES).append(";")
			;
		return sb.toString();
	}
}
