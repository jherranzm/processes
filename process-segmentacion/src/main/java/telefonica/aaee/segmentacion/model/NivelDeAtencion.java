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
@Table(name = "tbl_niveldeatencion")
@NamedQueries({
	@NamedQuery(name = "NivelDeAtencion.findAll", query = "SELECT c FROM NivelDeAtencion c"),
	@NamedQuery(name = "NivelDeAtencion.findByNombre", query = "SELECT p "
			+ "FROM NivelDeAtencion p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.nomNivelDeAtencion = :nom "),
	@NamedQuery(name = "NivelDeAtencion.findByCodigo", query = "SELECT p "
			+ "FROM NivelDeAtencion p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.codNivelDeAtencion = :cod ") 
})
public class NivelDeAtencion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Nivel_de_Atencion")
	private String codNivelDeAtencion;

	@Column(name = "Nom_Nivel_de_Atencion")
	private String nomNivelDeAtencion;

	public NivelDeAtencion() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodNivelDeAtencion() {
		return codNivelDeAtencion;
	}

	public void setCodNivelDeAtencion(String codNivelDeAtencion) {
		this.codNivelDeAtencion = codNivelDeAtencion;
	}

	public String getNomNivelDeAtencion() {
		return nomNivelDeAtencion;
	}

	public void setNomNivelDeAtencion(String nomNivelDeAtencion) {
		this.nomNivelDeAtencion = nomNivelDeAtencion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codNivelDeAtencion == null) ? 0 : codNivelDeAtencion
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((nomNivelDeAtencion == null) ? 0 : nomNivelDeAtencion
						.hashCode());
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
		NivelDeAtencion other = (NivelDeAtencion) obj;
		if (codNivelDeAtencion == null) {
			if (other.codNivelDeAtencion != null)
				return false;
		} else if (!codNivelDeAtencion.equals(other.codNivelDeAtencion))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomNivelDeAtencion == null) {
			if (other.nomNivelDeAtencion != null)
				return false;
		} else if (!nomNivelDeAtencion.equals(other.nomNivelDeAtencion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NivelDeAtencion [id=");
		builder.append(id);
		builder.append(", codNivelDeAtencion=");
		builder.append(codNivelDeAtencion);
		builder.append(", nomNivelDeAtencion=");
		builder.append(nomNivelDeAtencion);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String codNivelDeAtencion;
		private String nomNivelDeAtencion;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder codNivelDeAtencion(String codNivelDeAtencion) {
			this.codNivelDeAtencion = codNivelDeAtencion;
			return this;
		}

		public Builder nomNivelDeAtencion(String nomNivelDeAtencion) {
			this.nomNivelDeAtencion = nomNivelDeAtencion;
			return this;
		}

		public NivelDeAtencion build() {
			return new NivelDeAtencion(this);
		}
	}

	private NivelDeAtencion(Builder builder) {
		this.id = builder.id;
		this.codNivelDeAtencion = builder.codNivelDeAtencion;
		this.nomNivelDeAtencion = builder.nomNivelDeAtencion;
	}
}
