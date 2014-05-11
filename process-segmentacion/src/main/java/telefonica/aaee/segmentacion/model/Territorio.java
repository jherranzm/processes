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
@Table(name = "tbl_territorio")
@NamedQueries({
	@NamedQuery(name = "Territorio.findAll", query = "SELECT c FROM Territorio c"),
	@NamedQuery(name = "Territorio.findByNombre", query = "SELECT p "
			+ "FROM Territorio p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.nomTerritorio = :nom "),
	@NamedQuery(name = "Territorio.findByCodigo", query = "SELECT p "
			+ "FROM Territorio p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.codTerritorio = :cod ") 
})
public class Territorio implements Serializable, Exportable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Territorio")
	private String codTerritorio;

	@Column(name = "Nom_Territorio")
	private String nomTerritorio;

	public Territorio() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodTerritorio() {
		return codTerritorio;
	}

	public void setCodTerritorio(String codTerritorio) {
		this.codTerritorio = codTerritorio;
	}

	public String getNomTerritorio() {
		return nomTerritorio;
	}

	public void setNomTerritorio(String nomTerritorio) {
		this.nomTerritorio = nomTerritorio;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Territorio [id=");
		builder.append(id);
		builder.append(", codTerritorio=");
		builder.append(codTerritorio);
		builder.append(", nomTerritorio=");
		builder.append(nomTerritorio);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codTerritorio == null) ? 0 : codTerritorio.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomTerritorio == null) ? 0 : nomTerritorio.hashCode());
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
		Territorio other = (Territorio) obj;
		if (codTerritorio == null) {
			if (other.codTerritorio != null)
				return false;
		} else if (!codTerritorio.equals(other.codTerritorio))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomTerritorio == null) {
			if (other.nomTerritorio != null)
				return false;
		} else if (!nomTerritorio.equals(other.nomTerritorio))
			return false;
		return true;
	}

	public static class Builder {
		private Long id;
		private String codTerritorio;
		private String nomTerritorio;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder codTerritorio(String codTerritorio) {
			this.codTerritorio = codTerritorio;
			return this;
		}

		public Builder nomTerritorio(String nomTerritorio) {
			this.nomTerritorio = nomTerritorio;
			return this;
		}

		public Territorio build() {
			return new Territorio(this);
		}
	}

	private Territorio(Builder builder) {
		this.id = builder.id;
		this.codTerritorio = builder.codTerritorio;
		this.nomTerritorio = builder.nomTerritorio;
	}

	public String toCSV(){
		StringBuilder sb = new StringBuilder();
		sb
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.codTerritorio)			
		.append(Constantes.COMILLAS_DOBLES).append(";")
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.nomTerritorio)
		.append(Constantes.COMILLAS_DOBLES).append(";")
			;
		return sb.toString();
	}
}
