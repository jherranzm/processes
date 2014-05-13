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
@Table(name = "tbl_segmento")
@NamedQueries({
		@NamedQuery(name = "Segmento.findAll", query = "SELECT c FROM Segmento c"),
		@NamedQuery(name = "Segmento.findByNombre", query = "SELECT p "
				+ "FROM Segmento p " + "WHERE " + " 1 = 1 "
				+ " AND p.nomSegmento = :nom "),
		@NamedQuery(name = "Segmento.findByCodigo", query = "SELECT p "
				+ "FROM Segmento p " + "WHERE " + " 1 = 1 "
				+ " AND p.codSegmento = :cod ") })
public class Segmento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Segmento")
	private String codSegmento;

	@Column(name = "Nom_Segmento")
	private String nomSegmento;

	public Segmento() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodSegmento() {
		return codSegmento;
	}

	public void setCodSegmento(String codSegmento) {
		this.codSegmento = codSegmento;
	}

	public String getNomSegmento() {
		return nomSegmento;
	}

	public void setNomSegmento(String nomSegmento) {
		this.nomSegmento = nomSegmento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codSegmento == null) ? 0 : codSegmento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomSegmento == null) ? 0 : nomSegmento.hashCode());
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
		Segmento other = (Segmento) obj;
		if (codSegmento == null) {
			if (other.codSegmento != null)
				return false;
		} else if (!codSegmento.equals(other.codSegmento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomSegmento == null) {
			if (other.nomSegmento != null)
				return false;
		} else if (!nomSegmento.equals(other.nomSegmento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Segmento [id=");
		builder.append(id);
		builder.append(", codSegmento=");
		builder.append(codSegmento);
		builder.append(", nomSegmento=");
		builder.append(nomSegmento);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String codSegmento;
		private String nomSegmento;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder codSegmento(String codSegmento) {
			this.codSegmento = codSegmento;
			return this;
		}

		public Builder nomSegmento(String nomSegmento) {
			this.nomSegmento = nomSegmento;
			return this;
		}

		public Segmento build() {
			return new Segmento(this);
		}
	}

	private Segmento(Builder builder) {
		this.id = builder.id;
		this.codSegmento = builder.codSegmento;
		this.nomSegmento = builder.nomSegmento;
	}
}
