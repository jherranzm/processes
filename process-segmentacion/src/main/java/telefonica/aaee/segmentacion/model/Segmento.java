package telefonica.aaee.segmentacion.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_segmento")
public class Segmento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Segmento")
	private String segmento;

	@Column(name = "Subsegmento")
	private String subSegmento;

	@Column(name = "Nivel_de_Atencion")
	private String nivelDeAtencion;

	public Segmento() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getSubSegmento() {
		return subSegmento;
	}

	public void setSubSegmento(String subSegmento) {
		this.subSegmento = subSegmento;
	}

	public String getNivelDeAtencion() {
		return nivelDeAtencion;
	}

	public void setNivelDeAtencion(String nivelDeAtencion) {
		this.nivelDeAtencion = nivelDeAtencion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nivelDeAtencion == null) ? 0 : nivelDeAtencion.hashCode());
		result = prime * result
				+ ((segmento == null) ? 0 : segmento.hashCode());
		result = prime * result
				+ ((subSegmento == null) ? 0 : subSegmento.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nivelDeAtencion == null) {
			if (other.nivelDeAtencion != null)
				return false;
		} else if (!nivelDeAtencion.equals(other.nivelDeAtencion))
			return false;
		if (segmento == null) {
			if (other.segmento != null)
				return false;
		} else if (!segmento.equals(other.segmento))
			return false;
		if (subSegmento == null) {
			if (other.subSegmento != null)
				return false;
		} else if (!subSegmento.equals(other.subSegmento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Segmento [id=");
		builder.append(id);
		builder.append(", segmento=");
		builder.append(segmento);
		builder.append(", subSegmento=");
		builder.append(subSegmento);
		builder.append(", nivelDeAtencion=");
		builder.append(nivelDeAtencion);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String segmento;
		private String subSegmento;
		private String nivelDeAtencion;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder segmento(String segmento) {
			this.segmento = segmento;
			return this;
		}

		public Builder subSegmento(String subSegmento) {
			this.subSegmento = subSegmento;
			return this;
		}

		public Builder nivelDeAtencion(String nivelDeAtencion) {
			this.nivelDeAtencion = nivelDeAtencion;
			return this;
		}

		public Segmento build() {
			return new Segmento(this);
		}
	}

	private Segmento(Builder builder) {
		this.id = builder.id;
		this.segmento = builder.segmento;
		this.subSegmento = builder.subSegmento;
		this.nivelDeAtencion = builder.nivelDeAtencion;
	}
}
