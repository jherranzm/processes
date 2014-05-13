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
@Table(name = "tbl_subsegmento")
@NamedQueries({
	@NamedQuery(name = "SubSegmento.findAll", query = "SELECT c FROM SubSegmento c"),
	@NamedQuery(name = "SubSegmento.findByNombre", query = "SELECT p "
			+ "FROM SubSegmento p " + "WHERE " + " 1 = 1 "
			+ " AND p.nomSubSegmento = :nom "),
	@NamedQuery(name = "SubSegmento.findByCodigo", query = "SELECT p "
			+ "FROM SubSegmento p " + "WHERE " + " 1 = 1 "
			+ " AND p.codSubSegmento = :cod ") 
})
public class SubSegmento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_SubSegmento")
	private String codSubSegmento;

	@Column(name = "Nom_Subsegmento")
	private String nomSubSegmento;

	public SubSegmento() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodSubSegmento() {
		return codSubSegmento;
	}

	public void setCodSubSegmento(String codSubSegmento) {
		this.codSubSegmento = codSubSegmento;
	}

	public String getNomSubSegmento() {
		return nomSubSegmento;
	}

	public void setNomSubSegmento(String nomSubSegmento) {
		this.nomSubSegmento = nomSubSegmento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codSubSegmento == null) ? 0 : codSubSegmento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomSubSegmento == null) ? 0 : nomSubSegmento.hashCode());
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
		SubSegmento other = (SubSegmento) obj;
		if (codSubSegmento == null) {
			if (other.codSubSegmento != null)
				return false;
		} else if (!codSubSegmento.equals(other.codSubSegmento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomSubSegmento == null) {
			if (other.nomSubSegmento != null)
				return false;
		} else if (!nomSubSegmento.equals(other.nomSubSegmento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubSegmento [id=");
		builder.append(id);
		builder.append(", codSubSegmento=");
		builder.append(codSubSegmento);
		builder.append(", nomSubSegmento=");
		builder.append(nomSubSegmento);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String codSubSegmento;
		private String nomSubSegmento;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder codSubSegmento(String codSubSegmento) {
			this.codSubSegmento = codSubSegmento;
			return this;
		}

		public Builder nomSubSegmento(String nomSubSegmento) {
			this.nomSubSegmento = nomSubSegmento;
			return this;
		}

		public SubSegmento build() {
			return new SubSegmento(this);
		}
	}

	private SubSegmento(Builder builder) {
		this.id = builder.id;
		this.codSubSegmento = builder.codSubSegmento;
		this.nomSubSegmento = builder.nomSubSegmento;
	}
}
