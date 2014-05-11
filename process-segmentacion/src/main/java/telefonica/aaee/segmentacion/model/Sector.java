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

@Entity
@Table(name = "tbl_sector")
@NamedQueries({
	@NamedQuery(name = "Sector.findAll", query = "SELECT c FROM Sector c"),
	@NamedQuery(name = "Sector.findByNombre", query = "SELECT p "
			+ "FROM Sector p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.nomSector = :nom "),
	@NamedQuery(name = "Sector.findByCodigo", query = "SELECT p "
			+ "FROM Sector p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.codSector = :cod ") 
})
public class Sector implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Sector")
	private String codSector;

	@Column(name = "Nom_Sector")
	private String nomSector;

	public Sector() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodSector() {
		return codSector;
	}

	public void setCodSector(String codSector) {
		this.codSector = codSector;
	}

	public String getNomSector() {
		return nomSector;
	}

	public void setNomSector(String nomSector) {
		this.nomSector = nomSector;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codSector == null) ? 0 : codSector.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomSector == null) ? 0 : nomSector.hashCode());
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
		Sector other = (Sector) obj;
		if (codSector == null) {
			if (other.codSector != null)
				return false;
		} else if (!codSector.equals(other.codSector))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomSector == null) {
			if (other.nomSector != null)
				return false;
		} else if (!nomSector.equals(other.nomSector))
			return false;
		return true;
	}
	
	




	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Sector [id=");
		builder2.append(id);
		builder2.append(", codSector=");
		builder2.append(codSector);
		builder2.append(", nomSector=");
		builder2.append(nomSector);
		builder2.append("]");
		return builder2.toString();
	}






	public static class Builder {
		private String codSector;
		private String nomSector;

		public Builder codSector(String codSector) {
			this.codSector = codSector;
			return this;
		}

		public Builder nomSector(String nomSector) {
			this.nomSector = nomSector;
			return this;
		}

		public Sector build() {
			return new Sector(this);
		}
	}

	private Sector(Builder builder) {
		this.codSector = builder.codSector;
		this.nomSector = builder.nomSector;
	}
}
