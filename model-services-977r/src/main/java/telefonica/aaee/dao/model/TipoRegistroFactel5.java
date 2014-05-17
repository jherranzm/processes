package telefonica.aaee.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the tbl_tipo_registro database table.
 * 
 */
@Entity
@Table(name = "tbl_tipo_registro")
@NamedQueries({
		@NamedQuery(name = "TipoRegistroFactel5.findAll", query = "SELECT c FROM TipoRegistroFactel5 c"),
		@NamedQuery(name = "TipoRegistroFactel5.findByTipoRegistro", query = "SELECT p FROM TipoRegistroFactel5 p "
				+ "WHERE 1 = 1 " + "AND p.tipoRegistro = :nom ") })
@XmlRootElement
public class TipoRegistroFactel5

implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tipo_registro")
	private String tipoRegistro;

	@Column(name = "descripcion")
	private String descripcion;

	public TipoRegistroFactel5() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((tipoRegistro == null) ? 0 : tipoRegistro.hashCode());
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
		TipoRegistroFactel5 other = (TipoRegistroFactel5) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tipoRegistro == null) {
			if (other.tipoRegistro != null)
				return false;
		} else if (!tipoRegistro.equals(other.tipoRegistro))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TipoRegistro [id=");
		builder.append(id);
		builder.append(", tipoRegistro=");
		builder.append(tipoRegistro);
		builder.append(", descripcion=");
		builder.append(descripcion);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String tipoRegistro;
		private String descripcion;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder tipoRegistro(String tipoRegistro) {
			this.tipoRegistro = tipoRegistro;
			return this;
		}

		public Builder descripcion(String descripcion) {
			this.descripcion = descripcion;
			return this;
		}

		public TipoRegistroFactel5 build() {
			return new TipoRegistroFactel5(this);
		}
	}

	private TipoRegistroFactel5(Builder builder) {
		this.id = builder.id;
		this.tipoRegistro = builder.tipoRegistro;
		this.descripcion = builder.descripcion;
	}
}