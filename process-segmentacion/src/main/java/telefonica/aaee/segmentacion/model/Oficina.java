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
@Table(name = "tbl_oficina")
@NamedQueries({
	@NamedQuery(name = "Oficina.findAll", query = "SELECT c FROM Oficina c"),
	@NamedQuery(name = "Oficina.findByNombre", query = "SELECT p "
			+ "FROM Oficina p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.nomOficina = :nom "),
	@NamedQuery(name = "Oficina.findByCodigo", query = "SELECT p "
			+ "FROM Oficina p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.codOficina = :cod ") 
})
public class Oficina implements Serializable, Exportable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Oficina")
	private String codOficina;

	@Column(name = "Nom_Oficina")
	private String nomOficina;

	public Oficina() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodOficina() {
		return codOficina;
	}

	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
	}

	public String getNomOficina() {
		return nomOficina;
	}

	public void setNomOficina(String nomOficina) {
		this.nomOficina = nomOficina;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codOficina == null) ? 0 : codOficina.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomOficina == null) ? 0 : nomOficina.hashCode());
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
		Oficina other = (Oficina) obj;
		if (codOficina == null) {
			if (other.codOficina != null)
				return false;
		} else if (!codOficina.equals(other.codOficina))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomOficina == null) {
			if (other.nomOficina != null)
				return false;
		} else if (!nomOficina.equals(other.nomOficina))
			return false;
		return true;
	}
	
	




	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Oficina [id=");
		builder2.append(id);
		builder2.append(", codOficina=");
		builder2.append(codOficina);
		builder2.append(", nomOficina=");
		builder2.append(nomOficina);
		builder2.append("]");
		return builder2.toString();
	}






	public static class Builder {
		private String codOficina;
		private String nomOficina;

		public Builder codOficina(String codOficina) {
			this.codOficina = codOficina;
			return this;
		}

		public Builder nomOficina(String nomOficina) {
			this.nomOficina = nomOficina;
			return this;
		}

		public Oficina build() {
			return new Oficina(this);
		}
	}

	private Oficina(Builder builder) {
		this.codOficina = builder.codOficina;
		this.nomOficina = builder.nomOficina;
	}

	public String toCSV(){
		StringBuilder sb = new StringBuilder();
		sb
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.codOficina)
		.append(Constantes.COMILLAS_DOBLES).append(";")
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.nomOficina)
		.append(Constantes.COMILLAS_DOBLES).append(";")
			;
		return sb.toString();
	}
}
