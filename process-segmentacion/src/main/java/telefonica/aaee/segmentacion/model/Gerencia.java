package telefonica.aaee.segmentacion.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import telefonica.aaee.segmentacion.util.Constantes;

@Entity
@Table(name = "tbl_gerencia")
public class Gerencia implements Serializable, Exportable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Gerencia")
	private String codGerencia;

	@Column(name = "Nom_Gerencia")
	private String nomGerencia;

	public Gerencia() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodGerencia() {
		return codGerencia;
	}

	public void setCodGerencia(String codGerencia) {
		this.codGerencia = codGerencia;
	}

	public String getNomGerencia() {
		return nomGerencia;
	}

	public void setNomGerencia(String nomGerencia) {
		this.nomGerencia = nomGerencia;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Gerencia [id=");
		builder.append(id);
		builder.append(", codGerencia=");
		builder.append(codGerencia);
		builder.append(", nomGerencia=");
		builder.append(nomGerencia);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codGerencia == null) ? 0 : codGerencia.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomGerencia == null) ? 0 : nomGerencia.hashCode());
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
		Gerencia other = (Gerencia) obj;
		if (codGerencia == null) {
			if (other.codGerencia != null)
				return false;
		} else if (!codGerencia.equals(other.codGerencia))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomGerencia == null) {
			if (other.nomGerencia != null)
				return false;
		} else if (!nomGerencia.equals(other.nomGerencia))
			return false;
		return true;
	}

	public static class Builder {
		private Long id;
		private String codGerencia;
		private String nomGerencia;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder codGerencia(String codGerencia) {
			this.codGerencia = codGerencia;
			return this;
		}

		public Builder nomGerencia(String nomGerencia) {
			this.nomGerencia = nomGerencia;
			return this;
		}

		public Gerencia build() {
			return new Gerencia(this);
		}
	}

	private Gerencia(Builder builder) {
		this.id = builder.id;
		this.codGerencia = builder.codGerencia;
		this.nomGerencia = builder.nomGerencia;
	}

	public String toCSV(){
		StringBuilder sb = new StringBuilder();
		sb
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.codGerencia)
		.append(Constantes.COMILLAS_DOBLES).append(";")
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.nomGerencia)
		.append(Constantes.COMILLAS_DOBLES).append(";")
			;
		return sb.toString();
	}
}
