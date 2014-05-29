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
				+ "FROM Segmentacion p " + "WHERE " + " 1 = 1 "
				+ " AND p.cucCliente = :cod ") })
public class Segmentacion implements Serializable, Exportable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Cliente")
	private String cucCliente;

	@Column(name = "Id_Territorio")
	private Long idTerritorio;

	@Column(name = "Id_Gerencia")
	private Long idGerencia;

	@Column(name = "Id_Oficina")
	private Long idOficina;

	@Column(name = "Id_Sector")
	private Long idSector;

	@Column(name = "Id_SubSector")
	private Long idSubSector;

	@Column(name = "Id_Segmento")
	private Long idSegmento;

	@Column(name = "Id_SubSegmento")
	private Long idSubSegmento;

	@Column(name = "Id_NivelDeAtencion")
	private Long idNivelDeAtencion;

	@Column(name = "Mat_Vendedor")
	private Long idVendedor;

	@Column(name = "Mat_Desarrollador")
	private Long idDesarrollador;

	@Column(name = "Mat_JVentas")
	private Long idJVentas;

	@Column(name = "Mat_JArea")
	private Long idJArea;

	@Column(name = "Mat_Gerente")
	private Long idGerente;

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

	public Long getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(Long idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public Long getIdGerencia() {
		return idGerencia;
	}

	public void setIdGerencia(Long idGerencia) {
		this.idGerencia = idGerencia;
	}

	public Long getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(Long idOficina) {
		this.idOficina = idOficina;
	}

	public Long getIdSector() {
		return idSector;
	}

	public void setIdSector(Long idSector) {
		this.idSector = idSector;
	}

	public Long getIdSubSector() {
		return idSubSector;
	}

	public void setIdSubSector(Long idSubSector) {
		this.idSubSector = idSubSector;
	}

	public Long getIdSegmento() {
		return idSegmento;
	}

	public void setIdSegmento(Long idSegmento) {
		this.idSegmento = idSegmento;
	}

	public Long getIdSubSegmento() {
		return idSubSegmento;
	}

	public void setIdSubSegmento(Long idSubSegmento) {
		this.idSubSegmento = idSubSegmento;
	}

	public Long getIdNivelDeAtencion() {
		return idNivelDeAtencion;
	}

	public void setIdNivelDeAtencion(Long idNivelDeAtencion) {
		this.idNivelDeAtencion = idNivelDeAtencion;
	}

	public Long getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(Long idVendedor) {
		this.idVendedor = idVendedor;
	}

	public Long getIdDesarrollador() {
		return idDesarrollador;
	}

	public void setIdDesarrollador(Long idDesarrollador) {
		this.idDesarrollador = idDesarrollador;
	}

	public Long getIdJVentas() {
		return idJVentas;
	}

	public void setIdJVentas(Long idJVentas) {
		this.idJVentas = idJVentas;
	}

	public Long getIdJArea() {
		return idJArea;
	}

	public void setIdJArea(Long idJArea) {
		this.idJArea = idJArea;
	}

	public Long getIdGerente() {
		return idGerente;
	}

	public void setIdGerente(Long idGerente) {
		this.idGerente = idGerente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cucCliente == null) ? 0 : cucCliente.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idDesarrollador == null) ? 0 : idDesarrollador.hashCode());
		result = prime * result
				+ ((idGerencia == null) ? 0 : idGerencia.hashCode());
		result = prime * result
				+ ((idGerente == null) ? 0 : idGerente.hashCode());
		result = prime * result + ((idJArea == null) ? 0 : idJArea.hashCode());
		result = prime * result
				+ ((idJVentas == null) ? 0 : idJVentas.hashCode());
		result = prime
				* result
				+ ((idNivelDeAtencion == null) ? 0 : idNivelDeAtencion
						.hashCode());
		result = prime * result
				+ ((idOficina == null) ? 0 : idOficina.hashCode());
		result = prime * result
				+ ((idSector == null) ? 0 : idSector.hashCode());
		result = prime * result
				+ ((idSegmento == null) ? 0 : idSegmento.hashCode());
		result = prime * result
				+ ((idSubSector == null) ? 0 : idSubSector.hashCode());
		result = prime * result
				+ ((idSubSegmento == null) ? 0 : idSubSegmento.hashCode());
		result = prime * result
				+ ((idTerritorio == null) ? 0 : idTerritorio.hashCode());
		result = prime * result
				+ ((idVendedor == null) ? 0 : idVendedor.hashCode());
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
		if (idDesarrollador == null) {
			if (other.idDesarrollador != null)
				return false;
		} else if (!idDesarrollador.equals(other.idDesarrollador))
			return false;
		if (idGerencia == null) {
			if (other.idGerencia != null)
				return false;
		} else if (!idGerencia.equals(other.idGerencia))
			return false;
		if (idGerente == null) {
			if (other.idGerente != null)
				return false;
		} else if (!idGerente.equals(other.idGerente))
			return false;
		if (idJArea == null) {
			if (other.idJArea != null)
				return false;
		} else if (!idJArea.equals(other.idJArea))
			return false;
		if (idJVentas == null) {
			if (other.idJVentas != null)
				return false;
		} else if (!idJVentas.equals(other.idJVentas))
			return false;
		if (idNivelDeAtencion == null) {
			if (other.idNivelDeAtencion != null)
				return false;
		} else if (!idNivelDeAtencion.equals(other.idNivelDeAtencion))
			return false;
		if (idOficina == null) {
			if (other.idOficina != null)
				return false;
		} else if (!idOficina.equals(other.idOficina))
			return false;
		if (idSector == null) {
			if (other.idSector != null)
				return false;
		} else if (!idSector.equals(other.idSector))
			return false;
		if (idSegmento == null) {
			if (other.idSegmento != null)
				return false;
		} else if (!idSegmento.equals(other.idSegmento))
			return false;
		if (idSubSector == null) {
			if (other.idSubSector != null)
				return false;
		} else if (!idSubSector.equals(other.idSubSector))
			return false;
		if (idSubSegmento == null) {
			if (other.idSubSegmento != null)
				return false;
		} else if (!idSubSegmento.equals(other.idSubSegmento))
			return false;
		if (idTerritorio == null) {
			if (other.idTerritorio != null)
				return false;
		} else if (!idTerritorio.equals(other.idTerritorio))
			return false;
		if (idVendedor == null) {
			if (other.idVendedor != null)
				return false;
		} else if (!idVendedor.equals(other.idVendedor))
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
		builder.append(", idTerritorio=");
		builder.append(idTerritorio);
		builder.append(", idGerencia=");
		builder.append(idGerencia);
		builder.append(", idOficina=");
		builder.append(idOficina);
		builder.append(", idSector=");
		builder.append(idSector);
		builder.append(", idSubSector=");
		builder.append(idSubSector);
		builder.append(", idSegmento=");
		builder.append(idSegmento);
		builder.append(", idSubSegmento=");
		builder.append(idSubSegmento);
		builder.append(", idNivelDeAtencion=");
		builder.append(idNivelDeAtencion);
		builder.append(", idVendedor=");
		builder.append(idVendedor);
		builder.append(", idDesarrollador=");
		builder.append(idDesarrollador);
		builder.append(", idJVentas=");
		builder.append(idJVentas);
		builder.append(", idJArea=");
		builder.append(idJArea);
		builder.append(", idGerente=");
		builder.append(idGerente);
		builder.append("]");
		return builder.toString();
	}

	public String toCSV() {
		StringBuilder sb = new StringBuilder();
		sb.append(Constantes.COMILLAS_DOBLES).append(this.cucCliente)
				.append(Constantes.COMILLAS_DOBLES).append(";")
				.append(this.idTerritorio).append(";").append(this.idGerencia)
				.append(";").append(this.idOficina).append(";")
				.append(this.idSector).append(";").append(this.idSubSector)
				.append(";").append(this.idSegmento).append(";")
				.append(this.idSubSegmento).append(";")
				.append(this.idNivelDeAtencion).append(";")
				.append(this.idVendedor).append(";")
				.append(this.idDesarrollador).append(";")
				.append(this.idJVentas).append(";").append(this.idJArea)
				.append(";").append(this.idGerente).append(";")

		;
		return sb.toString();
	}

	public static class Builder {
		private Long id;
		private String cucCliente;
		private Long idTerritorio;
		private Long idGerencia;
		private Long idOficina;
		
		private Long idSector;
		private Long idSubSector;
		
		private Long idSegmento;
		private Long idSubSegmento;
		private Long idNivelDeAtencion;
		
		private Long idVendedor;
		private Long idDesarrollador;
		private Long idJVentas;
		private Long idJArea;
		private Long idGerente;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder cucCliente(String cucCliente) {
			this.cucCliente = cucCliente;
			return this;
		}

		public Builder idTerritorio(Long idTerritorio) {
			this.idTerritorio = idTerritorio;
			return this;
		}

		public Builder idGerencia(Long idGerencia) {
			this.idGerencia = idGerencia;
			return this;
		}

		public Builder idOficina(Long idOficina) {
			this.idOficina = idOficina;
			return this;
		}

		public Builder idSector(Long idSector) {
			this.idSector = idSector;
			return this;
		}

		public Builder idSubSector(Long idSubSector) {
			this.idSubSector = idSubSector;
			return this;
		}

		public Builder idSegmento(Long idSegmento) {
			this.idSegmento = idSegmento;
			return this;
		}

		public Builder idSubSegmento(Long idSubSegmento) {
			this.idSubSegmento = idSubSegmento;
			return this;
		}

		public Builder idNivelDeAtencion(Long idNivelDeAtencion) {
			this.idNivelDeAtencion = idNivelDeAtencion;
			return this;
		}

		public Builder idVendedor(Long idVendedor) {
			this.idVendedor = idVendedor;
			return this;
		}

		public Builder idDesarrollador(Long idDesarrollador) {
			this.idDesarrollador = idDesarrollador;
			return this;
		}

		public Builder idJVentas(Long idJVentas) {
			this.idJVentas = idJVentas;
			return this;
		}

		public Builder idJArea(Long idJArea) {
			this.idJArea = idJArea;
			return this;
		}

		public Builder idGerente(Long idGerente) {
			this.idGerente = idGerente;
			return this;
		}

		public Segmentacion build() {
			return new Segmentacion(this);
		}
	}

	private Segmentacion(Builder builder) {
		this.id = builder.id;
		this.cucCliente = builder.cucCliente;
		this.idTerritorio = builder.idTerritorio;
		this.idGerencia = builder.idGerencia;
		this.idOficina = builder.idOficina;
		this.idSector = builder.idSector;
		this.idSubSector = builder.idSubSector;
		this.idSegmento = builder.idSegmento;
		this.idSubSegmento = builder.idSubSegmento;
		this.idNivelDeAtencion = builder.idNivelDeAtencion;
		this.idVendedor = builder.idVendedor;
		this.idDesarrollador = builder.idDesarrollador;
		this.idJVentas = builder.idJVentas;
		this.idJArea = builder.idJArea;
		this.idGerente = builder.idGerente;
	}
}
