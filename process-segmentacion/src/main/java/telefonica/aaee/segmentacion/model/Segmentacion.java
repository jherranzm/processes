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
@Table(name = "tbl_segmentacion")
@NamedQueries({
		@NamedQuery(name = "Segmentacion.findAll", query = "SELECT c FROM Segmentacion c"),
		@NamedQuery(name = "Segmentacion.findByCuc", query = "SELECT p "
				+ "FROM Segmentacion p "
				+ "WHERE "
				+ " 1 = 1 "
				+ " AND p.cucCliente = :cod ")
})
public class Segmentacion implements Serializable, Exportable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Cliente")
	private String cucCliente;

	@Column(name = "Cod_Territorio")
	private String codTerritorio;

	@Column(name = "Cod_Gerencia")
	private String codGerencia;

	@Column(name = "Cod_Oficina")
	private String codOficina;

	@Column(name = "Cod_Sector", nullable = true)
	private String codSector;

	@Column(name = "Cod_SubSector", nullable = true)
	private String codSubSector;

	@Column(name = "Mat_Vendedor", nullable = true)
	private String matVendedor;

	@Column(name = "Mat_Desarrollador", nullable = true)
	private String matDesarrollador;

	@Column(name = "Mat_JVentas", nullable = true)
	private String matJVentas;

	@Column(name = "Mat_JArea", nullable = true)
	private String matJArea;

	@Column(name = "Mat_Gerente", nullable = true)
	private String matGerente;

	public Segmentacion() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCucCliente() {
		return cucCliente;
	}

	public void setCucCliente(String cucCliente) {
		this.cucCliente = cucCliente;
	}

	public String getCodTerritorio() {
		return codTerritorio;
	}

	public void setCodTerritorio(String codTerritorio) {
		this.codTerritorio = codTerritorio;
	}

	public String getCodGerencia() {
		return codGerencia;
	}

	public void setCodGerencia(String codGerencia) {
		this.codGerencia = codGerencia;
	}

	public String getCodOficina() {
		return codOficina;
	}

	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
	}

	public String getCodSector() {
		return codSector;
	}

	public void setCodSector(String codSector) {
		this.codSector = codSector;
	}

	public String getCodSubSector() {
		return codSubSector;
	}

	public void setCodSubSector(String codSubSector) {
		this.codSubSector = codSubSector;
	}

	public String getMatVendedor() {
		return matVendedor;
	}

	public void setMatVendedor(String matVendedor) {
		this.matVendedor = matVendedor;
	}

	public String getMatDesarrollador() {
		return matDesarrollador;
	}

	public void setMatDesarrollador(String matDesarrollador) {
		this.matDesarrollador = matDesarrollador;
	}

	public String getMatJVentas() {
		return matJVentas;
	}

	public void setMatJVentas(String matJVentas) {
		this.matJVentas = matJVentas;
	}

	public String getMatJArea() {
		return matJArea;
	}

	public void setMatJArea(String matJArea) {
		this.matJArea = matJArea;
	}

	public String getMatGerente() {
		return matGerente;
	}

	public void setMatGerente(String matGerente) {
		this.matGerente = matGerente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codGerencia == null) ? 0 : codGerencia.hashCode());
		result = prime * result
				+ ((codOficina == null) ? 0 : codOficina.hashCode());
		result = prime * result
				+ ((codSector == null) ? 0 : codSector.hashCode());
		result = prime * result
				+ ((codSubSector == null) ? 0 : codSubSector.hashCode());
		result = prime * result
				+ ((codTerritorio == null) ? 0 : codTerritorio.hashCode());
		result = prime * result
				+ ((cucCliente == null) ? 0 : cucCliente.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((matDesarrollador == null) ? 0 : matDesarrollador.hashCode());
		result = prime * result
				+ ((matGerente == null) ? 0 : matGerente.hashCode());
		result = prime * result
				+ ((matJArea == null) ? 0 : matJArea.hashCode());
		result = prime * result
				+ ((matJVentas == null) ? 0 : matJVentas.hashCode());
		result = prime * result
				+ ((matVendedor == null) ? 0 : matVendedor.hashCode());
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
		Segmentacion other = (Segmentacion) obj;
		if (codGerencia == null) {
			if (other.codGerencia != null)
				return false;
		} else if (!codGerencia.equals(other.codGerencia))
			return false;
		if (codOficina == null) {
			if (other.codOficina != null)
				return false;
		} else if (!codOficina.equals(other.codOficina))
			return false;
		if (codSector == null) {
			if (other.codSector != null)
				return false;
		} else if (!codSector.equals(other.codSector))
			return false;
		if (codSubSector == null) {
			if (other.codSubSector != null)
				return false;
		} else if (!codSubSector.equals(other.codSubSector))
			return false;
		if (codTerritorio == null) {
			if (other.codTerritorio != null)
				return false;
		} else if (!codTerritorio.equals(other.codTerritorio))
			return false;
		if (cucCliente == null) {
			if (other.cucCliente != null)
				return false;
		} else if (!cucCliente.equals(other.cucCliente))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (matDesarrollador == null) {
			if (other.matDesarrollador != null)
				return false;
		} else if (!matDesarrollador.equals(other.matDesarrollador))
			return false;
		if (matGerente == null) {
			if (other.matGerente != null)
				return false;
		} else if (!matGerente.equals(other.matGerente))
			return false;
		if (matJArea == null) {
			if (other.matJArea != null)
				return false;
		} else if (!matJArea.equals(other.matJArea))
			return false;
		if (matJVentas == null) {
			if (other.matJVentas != null)
				return false;
		} else if (!matJVentas.equals(other.matJVentas))
			return false;
		if (matVendedor == null) {
			if (other.matVendedor != null)
				return false;
		} else if (!matVendedor.equals(other.matVendedor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Segmentacion [id=");
		builder.append(id);
		builder.append(", cucCliente=");
		builder.append(cucCliente);
		builder.append(", codTerritorio=");
		builder.append(codTerritorio);
		builder.append(", codGerencia=");
		builder.append(codGerencia);
		builder.append(", codOficina=");
		builder.append(codOficina);
		builder.append(", codSector=");
		builder.append(codSector);
		builder.append(", codSubSector=");
		builder.append(codSubSector);
		builder.append(", matVendedor=");
		builder.append(matVendedor);
		builder.append(", matDesarrollador=");
		builder.append(matDesarrollador);
		builder.append(", matJVentas=");
		builder.append(matJVentas);
		builder.append(", matJArea=");
		builder.append(matJArea);
		builder.append(", matGerente=");
		builder.append(matGerente);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public String toCSV() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				Constantes.COMILLAS_DOBLES)
					.append(this.cucCliente)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.codTerritorio)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.codGerencia)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.codOficina)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.codSector)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.codSubSector)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.matVendedor)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.matDesarrollador)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.matJVentas)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.matJArea)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(Constantes.COMILLAS_DOBLES)
					.append(this.matGerente)
				.append(Constantes.COMILLAS_DOBLES).append(";");
		return sb.toString();
	}

	public static class Builder {
		private Long id;
		private String cucCliente;
		private String codTerritorio;
		private String codGerencia;
		private String codOficina;
		private String codSector;
		private String codSubSector;
		private String matVendedor;
		private String matDesarrollador;
		private String matJVentas;
		private String matJArea;
		private String matGerente;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder cucCliente(String cucCliente) {
			this.cucCliente = cucCliente;
			return this;
		}

		public Builder codTerritorio(String codTerritorio) {
			this.codTerritorio = codTerritorio;
			return this;
		}

		public Builder codGerencia(String codGerencia) {
			this.codGerencia = codGerencia;
			return this;
		}

		public Builder codOficina(String codOficina) {
			this.codOficina = codOficina;
			return this;
		}

		public Builder codSector(String codSector) {
			this.codSector = codSector;
			return this;
		}

		public Builder codSubSector(String codSubSector) {
			this.codSubSector = codSubSector;
			return this;
		}

		public Builder matVendedor(String matVendedor) {
			this.matVendedor = matVendedor;
			return this;
		}

		public Builder matDesarrollador(String matDesarrollador) {
			this.matDesarrollador = matDesarrollador;
			return this;
		}

		public Builder matJVentas(String matJVentas) {
			this.matJVentas = matJVentas;
			return this;
		}

		public Builder matJArea(String matJArea) {
			this.matJArea = matJArea;
			return this;
		}

		public Builder matGerente(String matGerente) {
			this.matGerente = matGerente;
			return this;
		}

		public Segmentacion build() {
			return new Segmentacion(this);
		}
	}

	private Segmentacion(Builder builder) {
		this.id = builder.id;
		this.cucCliente = builder.cucCliente;
		this.codTerritorio = builder.codTerritorio;
		this.codGerencia = builder.codGerencia;
		this.codOficina = builder.codOficina;
		this.codSector = builder.codSector;
		this.codSubSector = builder.codSubSector;
		this.matVendedor = builder.matVendedor;
		this.matDesarrollador = builder.matDesarrollador;
		this.matJVentas = builder.matJVentas;
		this.matJArea = builder.matJArea;
		this.matGerente = builder.matGerente;
	}
}
