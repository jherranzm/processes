package telefonica.aaee.informes.model.condiciones;

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
 * The persistent class for the ttrafico_internacional database table.
 * 
 */
@Entity
@Table(name = "ttrafico_internacional")
@NamedQueries({ @NamedQuery(name = "FindByAcuerdoDestino", query = "SELECT c FROM TraficoInternacional c "
		+ "WHERE " + "c.acuerdo = :ac " + "and c.destino = :pd ") })
@XmlRootElement
public class TraficoInternacional

implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String acuerdo;

	@Column(name = "AMBITO_DE_TRAFICO")
	private String ambitoDeTrafico;

	private String cif;

	@Column(name = "DESC_AMBITO_DE_TRAFICO")
	private String descAmbitoDeTrafico;

	private String destino;

	@Column(name = "EST_LLAMADA")
	private double estLlamada;

	@Column(name = "PAIS_DESTINO")
	private String paisDestino;

	@Column(name = "PORCENTAJE_DESCUENTO")
	private double porcentajeDescuento;

	@Column(name = "PRECIO_ESPECIAL")
	private String precioEspecial;

	@Column(name = "PRECIO_POR_MINUTO")
	private double precioPorMinuto;

	@Column(name = "TIPO_DESCUENTO")
	private String tipoDescuento;

	@Column(name = "ini_periodo")
	private String iniPeriodo;

	@Column(name = "fin_periodo")
	private String finPeriodo;

	/**
	 * @return iniPeriodo
	 */
	public String getIniPeriodo() {
		return iniPeriodo;
	}

	/**
	 * @param iniPeriodo valor a asignar al campo iniPeriodo
	 */
	public void setIniPeriodo(String iniPeriodo) {
		this.iniPeriodo = iniPeriodo;
	}

	/**
	 * @return finPeriodo
	 */
	public String getFinPeriodo() {
		return finPeriodo;
	}

	/**
	 * @param finPeriodo valor a asignar al campo finPeriodo
	 */
	public void setFinPeriodo(String finPeriodo) {
		this.finPeriodo = finPeriodo;
	}

	public TraficoInternacional() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcuerdo() {
		return this.acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}

	public String getAmbitoDeTrafico() {
		return this.ambitoDeTrafico;
	}

	public void setAmbitoDeTrafico(String ambitoDeTrafico) {
		this.ambitoDeTrafico = ambitoDeTrafico;
	}

	public String getCif() {
		return this.cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getDescAmbitoDeTrafico() {
		return this.descAmbitoDeTrafico;
	}

	public void setDescAmbitoDeTrafico(String descAmbitoDeTrafico) {
		this.descAmbitoDeTrafico = descAmbitoDeTrafico;
	}

	public String getDestino() {
		return this.destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public double getEstLlamada() {
		return this.estLlamada;
	}

	public void setEstLlamada(double estLlamada) {
		this.estLlamada = estLlamada;
	}

	public String getPaisDestino() {
		return this.paisDestino;
	}

	public void setPaisDestino(String paisDestino) {
		this.paisDestino = paisDestino;
	}

	public double getPorcentajeDescuento() {
		return this.porcentajeDescuento;
	}

	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	public String getPrecioEspecial() {
		return this.precioEspecial;
	}

	public void setPrecioEspecial(String precioEspecial) {
		this.precioEspecial = precioEspecial;
	}

	public double getPrecioPorMinuto() {
		return this.precioPorMinuto;
	}

	public void setPrecioPorMinuto(double precioPorMinuto) {
		this.precioPorMinuto = precioPorMinuto;
	}

	public String getTipoDescuento() {
		return this.tipoDescuento;
	}

	public void setTipoDescuento(String tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}

	@Override
	public String toString() {
		return "TraficoInternacional [id=" + id + ",\\n acuerdo=" + acuerdo
				+ ",\\n ambitoDeTrafico=" + ambitoDeTrafico + ",\\n cif=" + cif
				+ ",\\n descAmbitoDeTrafico=" + descAmbitoDeTrafico
				+ ",\\n destino=" + destino + ",\\n estLlamada=" + estLlamada
				+ ",\\n paisDestino=" + paisDestino
				+ ",\\n porcentajeDescuento=" + porcentajeDescuento
				+ ",\\n precioEspecial=" + precioEspecial
				+ ",\\n precioPorMinuto=" + precioPorMinuto
				+ ",\\n tipoDescuento=" + tipoDescuento + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuerdo == null) ? 0 : acuerdo.hashCode());
		result = prime * result
				+ ((ambitoDeTrafico == null) ? 0 : ambitoDeTrafico.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime
				* result
				+ ((descAmbitoDeTrafico == null) ? 0 : descAmbitoDeTrafico
						.hashCode());
		result = prime * result + ((destino == null) ? 0 : destino.hashCode());
		long temp;
		temp = Double.doubleToLongBits(estLlamada);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((finPeriodo == null) ? 0 : finPeriodo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((iniPeriodo == null) ? 0 : iniPeriodo.hashCode());
		result = prime * result
				+ ((paisDestino == null) ? 0 : paisDestino.hashCode());
		temp = Double.doubleToLongBits(porcentajeDescuento);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((precioEspecial == null) ? 0 : precioEspecial.hashCode());
		temp = Double.doubleToLongBits(precioPorMinuto);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((tipoDescuento == null) ? 0 : tipoDescuento.hashCode());
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
		TraficoInternacional other = (TraficoInternacional) obj;
		if (acuerdo == null) {
			if (other.acuerdo != null)
				return false;
		} else if (!acuerdo.equals(other.acuerdo))
			return false;
		if (ambitoDeTrafico == null) {
			if (other.ambitoDeTrafico != null)
				return false;
		} else if (!ambitoDeTrafico.equals(other.ambitoDeTrafico))
			return false;
		if (cif == null) {
			if (other.cif != null)
				return false;
		} else if (!cif.equals(other.cif))
			return false;
		if (descAmbitoDeTrafico == null) {
			if (other.descAmbitoDeTrafico != null)
				return false;
		} else if (!descAmbitoDeTrafico.equals(other.descAmbitoDeTrafico))
			return false;
		if (destino == null) {
			if (other.destino != null)
				return false;
		} else if (!destino.equals(other.destino))
			return false;
		if (Double.doubleToLongBits(estLlamada) != Double
				.doubleToLongBits(other.estLlamada))
			return false;
		if (finPeriodo == null) {
			if (other.finPeriodo != null)
				return false;
		} else if (!finPeriodo.equals(other.finPeriodo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (iniPeriodo == null) {
			if (other.iniPeriodo != null)
				return false;
		} else if (!iniPeriodo.equals(other.iniPeriodo))
			return false;
		if (paisDestino == null) {
			if (other.paisDestino != null)
				return false;
		} else if (!paisDestino.equals(other.paisDestino))
			return false;
		if (Double.doubleToLongBits(porcentajeDescuento) != Double
				.doubleToLongBits(other.porcentajeDescuento))
			return false;
		if (precioEspecial == null) {
			if (other.precioEspecial != null)
				return false;
		} else if (!precioEspecial.equals(other.precioEspecial))
			return false;
		if (Double.doubleToLongBits(precioPorMinuto) != Double
				.doubleToLongBits(other.precioPorMinuto))
			return false;
		if (tipoDescuento == null) {
			if (other.tipoDescuento != null)
				return false;
		} else if (!tipoDescuento.equals(other.tipoDescuento))
			return false;
		return true;
	}

	public static class Builder {
		private Long id;
		private String acuerdo;
		private String ambitoDeTrafico;
		private String cif;
		private String descAmbitoDeTrafico;
		private String destino;
		private double estLlamada;
		private String paisDestino;
		private double porcentajeDescuento;
		private String precioEspecial;
		private double precioPorMinuto;
		private String tipoDescuento;
		private String iniPeriodo;
		private String finPeriodo;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder acuerdo(String acuerdo) {
			this.acuerdo = acuerdo;
			return this;
		}

		public Builder ambitoDeTrafico(String ambitoDeTrafico) {
			this.ambitoDeTrafico = ambitoDeTrafico;
			return this;
		}

		public Builder cif(String cif) {
			this.cif = cif;
			return this;
		}

		public Builder descAmbitoDeTrafico(String descAmbitoDeTrafico) {
			this.descAmbitoDeTrafico = descAmbitoDeTrafico;
			return this;
		}

		public Builder destino(String destino) {
			this.destino = destino;
			return this;
		}

		public Builder estLlamada(double estLlamada) {
			this.estLlamada = estLlamada;
			return this;
		}

		public Builder paisDestino(String paisDestino) {
			this.paisDestino = paisDestino;
			return this;
		}

		public Builder porcentajeDescuento(double porcentajeDescuento) {
			this.porcentajeDescuento = porcentajeDescuento;
			return this;
		}

		public Builder precioEspecial(String precioEspecial) {
			this.precioEspecial = precioEspecial;
			return this;
		}

		public Builder precioPorMinuto(double precioPorMinuto) {
			this.precioPorMinuto = precioPorMinuto;
			return this;
		}

		public Builder tipoDescuento(String tipoDescuento) {
			this.tipoDescuento = tipoDescuento;
			return this;
		}

		public Builder iniPeriodo(String iniPeriodo) {
			this.iniPeriodo = iniPeriodo;
			return this;
		}

		public Builder finPeriodo(String finPeriodo) {
			this.finPeriodo = finPeriodo;
			return this;
		}

		public TraficoInternacional build() {
			return new TraficoInternacional(this);
		}
	}

	private TraficoInternacional(Builder builder) {
		this.id = builder.id;
		this.acuerdo = builder.acuerdo;
		this.ambitoDeTrafico = builder.ambitoDeTrafico;
		this.cif = builder.cif;
		this.descAmbitoDeTrafico = builder.descAmbitoDeTrafico;
		this.destino = builder.destino;
		this.estLlamada = builder.estLlamada;
		this.paisDestino = builder.paisDestino;
		this.porcentajeDescuento = builder.porcentajeDescuento;
		this.precioEspecial = builder.precioEspecial;
		this.precioPorMinuto = builder.precioPorMinuto;
		this.tipoDescuento = builder.tipoDescuento;
		this.iniPeriodo = builder.iniPeriodo;
		this.finPeriodo = builder.finPeriodo;
	}
}