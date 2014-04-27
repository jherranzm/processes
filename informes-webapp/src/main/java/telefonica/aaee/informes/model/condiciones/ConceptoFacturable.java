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
 * The persistent class for the tconceptosfacturables database table.
 * 
 */
@Entity
@Table(name = "tconceptosfacturables")
@NamedQueries({ 
	@NamedQuery(name = "CF.findAll", 
			query = "SELECT c "
					+ "FROM ConceptoFacturable c "
					)
	, @NamedQuery(name = "FindByAcuerdoCifTipoDeServicio", 
			query = "SELECT c "
					+ "FROM ConceptoFacturable c "
					+ "WHERE "
					+ "c.acuerdo = :ac "
					+ "and c.conceptoFacturable = :cf "
					+ "and c.tipoDeServicio = :sv"
					)
})
@XmlRootElement
public class ConceptoFacturable

implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String acuerdo;

	@Column(name = "CONCEPTO_FACTURABLE", nullable = false)
	private String conceptoFacturable = "";

	@Column(name = "CODIGO_PERSONALIZACION", nullable = false)
	private String codigoPersonalizacion = "";

	@Column(name = "DESC_CONCEPTO_FACTURABLE", nullable = false)
	private String descConceptoFacturable = "";

	@Column(name = "DESC_TIPO_DE_SERVICIO", nullable = false)
	private String descTipoDeServicio = "";

	@Column(name = "importe_original", columnDefinition = "Decimal(21,4) default '0.0000'", nullable = false)
	private double importeOriginal;

	@Column(name = "importe_acuerdo", columnDefinition = "Decimal(21,4) default '0.0000'", nullable = false)
	private double importeAcuerdo;

	@Column(name = "precio_especial", nullable = false)
	private String precioEspecial;

	@Column(name = "tipo_cf")
	private String tipoCf = "";

	@Column(name = "subtipo_cf")
	private String subtipoCf = "";

	@Column(name = "producto_servicio_cf")
	private String productoServicioCf = "";

	@Column(name = "contratable_cf")
	private String contratableCf = "";

	@Column(name = "TIPO_DE_SERVICIO", nullable = false)
	private String tipoDeServicio = "";

	@Column(name = "tipo_precio_especial", nullable = false)
	private String tipoPrecioEspecial = "";

	@Column(name = "ini_periodo", nullable = false)
	private String iniPeriodo = "20110128";

	@Column(name = "fin_periodo", nullable = false)
	private String finPeriodo = "25001228";

	public ConceptoFacturable() {
	}

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

	public String getConceptoFacturable() {
		return this.conceptoFacturable;
	}

	public void setConceptoFacturable(String conceptoFacturable) {
		this.conceptoFacturable = conceptoFacturable;
	}

	public String getContratableCf() {
		return this.contratableCf;
	}

	public void setContratableCf(String contratableCf) {
		this.contratableCf = contratableCf;
	}

	public String getDescConceptoFacturable() {
		return this.descConceptoFacturable;
	}

	public void setDescConceptoFacturable(String descConceptoFacturable) {
		this.descConceptoFacturable = descConceptoFacturable;
	}

	public String getDescTipoDeServicio() {
		return this.descTipoDeServicio;
	}

	public void setDescTipoDeServicio(String descTipoDeServicio) {
		this.descTipoDeServicio = descTipoDeServicio;
	}

	public double getImporteAcuerdo() {
		return this.importeAcuerdo;
	}

	public void setImporteAcuerdo(double importeAcuerdo) {
		this.importeAcuerdo = importeAcuerdo;
	}

	public String getPrecioEspecial() {
		return this.precioEspecial;
	}

	public void setPrecioEspecial(String precioEspecial) {
		this.precioEspecial = precioEspecial;
	}

	public String getProductoServicioCf() {
		return this.productoServicioCf;
	}

	public void setProductoServicioCf(String productoServicioCf) {
		this.productoServicioCf = productoServicioCf;
	}

	public String getSubtipoCf() {
		return this.subtipoCf;
	}

	public void setSubtipoCf(String subtipoCf) {
		this.subtipoCf = subtipoCf;
	}

	public String getTipoCf() {
		return this.tipoCf;
	}

	public void setTipoCf(String tipoCf) {
		this.tipoCf = tipoCf;
	}

	public String getTipoDeServicio() {
		return this.tipoDeServicio;
	}

	public void setTipoDeServicio(String tipoDeServicio) {
		this.tipoDeServicio = tipoDeServicio;
	}

	public String getTipoPrecioEspecial() {
		return this.tipoPrecioEspecial;
	}

	public void setTipoPrecioEspecial(String tipoPrecioEspecial) {
		this.tipoPrecioEspecial = tipoPrecioEspecial;
	}

	/**
	 * @return importeOriginal
	 */
	public double getImporteOriginal() {
		return importeOriginal;
	}

	/**
	 * @param importeOriginal valor a asignar al campo importeOriginal
	 */
	public void setImporteOriginal(double importeOriginal) {
		this.importeOriginal = importeOriginal;
	}

	/**
	 * @return codigoPersonalizacion
	 */
	public String getCodigoPersonalizacion() {
		return codigoPersonalizacion;
	}

	/**
	 * @param codigoPersonalizacion valor a asignar al campo codigoPersonalizacion
	 */
	public void setCodigoPersonalizacion(String codigoPersonalizacion) {
		this.codigoPersonalizacion = codigoPersonalizacion;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConceptoFacturable [id=");
		builder.append(id);
		builder.append(",\\n acuerdo=");
		builder.append(acuerdo);
		builder.append(",\\n conceptoFacturable=");
		builder.append(conceptoFacturable);
		builder.append(",\\n codigoPersonalizacion=");
		builder.append(codigoPersonalizacion);
		builder.append(",\\n descConceptoFacturable=");
		builder.append(descConceptoFacturable);
		builder.append(",\\n descTipoDeServicio=");
		builder.append(descTipoDeServicio);
		builder.append(",\\n importeOriginal=");
		builder.append(importeOriginal);
		builder.append(",\\n importeAcuerdo=");
		builder.append(importeAcuerdo);
		builder.append(",\\n precioEspecial=");
		builder.append(precioEspecial);
		builder.append(",\\n tipoCf=");
		builder.append(tipoCf);
		builder.append(",\\n subtipoCf=");
		builder.append(subtipoCf);
		builder.append(",\\n productoServicioCf=");
		builder.append(productoServicioCf);
		builder.append(",\\n contratableCf=");
		builder.append(contratableCf);
		builder.append(",\\n tipoDeServicio=");
		builder.append(tipoDeServicio);
		builder.append(",\\n tipoPrecioEspecial=");
		builder.append(tipoPrecioEspecial);
		builder.append(",\\n iniPeriodo=");
		builder.append(iniPeriodo);
		builder.append(",\\n finPeriodo=");
		builder.append(finPeriodo);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String acuerdo;
		private String conceptoFacturable;
		private String codigoPersonalizacion;
		private String descConceptoFacturable;
		private String descTipoDeServicio;
		private double importeOriginal;
		private double importeAcuerdo;
		private String precioEspecial;
		private String tipoCf;
		private String subtipoCf;
		private String productoServicioCf;
		private String contratableCf;
		private String tipoDeServicio;
		private String tipoPrecioEspecial;
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

		public Builder conceptoFacturable(String conceptoFacturable) {
			this.conceptoFacturable = conceptoFacturable;
			return this;
		}

		public Builder codigoPersonalizacion(String codigoPersonalizacion) {
			this.codigoPersonalizacion = codigoPersonalizacion;
			return this;
		}

		public Builder descConceptoFacturable(String descConceptoFacturable) {
			this.descConceptoFacturable = descConceptoFacturable;
			return this;
		}

		public Builder descTipoDeServicio(String descTipoDeServicio) {
			this.descTipoDeServicio = descTipoDeServicio;
			return this;
		}

		public Builder importeOriginal(double importeOriginal) {
			this.importeOriginal = importeOriginal;
			return this;
		}

		public Builder importeAcuerdo(double importeAcuerdo) {
			this.importeAcuerdo = importeAcuerdo;
			return this;
		}

		public Builder precioEspecial(String precioEspecial) {
			this.precioEspecial = precioEspecial;
			return this;
		}

		public Builder tipoCf(String tipoCf) {
			this.tipoCf = tipoCf;
			return this;
		}

		public Builder subtipoCf(String subtipoCf) {
			this.subtipoCf = subtipoCf;
			return this;
		}

		public Builder productoServicioCf(String productoServicioCf) {
			this.productoServicioCf = productoServicioCf;
			return this;
		}

		public Builder contratableCf(String contratableCf) {
			this.contratableCf = contratableCf;
			return this;
		}

		public Builder tipoDeServicio(String tipoDeServicio) {
			this.tipoDeServicio = tipoDeServicio;
			return this;
		}

		public Builder tipoPrecioEspecial(String tipoPrecioEspecial) {
			this.tipoPrecioEspecial = tipoPrecioEspecial;
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

		public ConceptoFacturable build() {
			return new ConceptoFacturable(this);
		}
	}

	private ConceptoFacturable(Builder builder) {
		this.id = builder.id;
		this.acuerdo = builder.acuerdo;
		this.conceptoFacturable = builder.conceptoFacturable;
		this.codigoPersonalizacion = builder.codigoPersonalizacion;
		this.descConceptoFacturable = builder.descConceptoFacturable;
		this.descTipoDeServicio = builder.descTipoDeServicio;
		this.importeOriginal = builder.importeOriginal;
		this.importeAcuerdo = builder.importeAcuerdo;
		this.precioEspecial = builder.precioEspecial;
		this.tipoCf = builder.tipoCf;
		this.subtipoCf = builder.subtipoCf;
		this.productoServicioCf = builder.productoServicioCf;
		this.contratableCf = builder.contratableCf;
		this.tipoDeServicio = builder.tipoDeServicio;
		this.tipoPrecioEspecial = builder.tipoPrecioEspecial;
		this.iniPeriodo = builder.iniPeriodo;
		this.finPeriodo = builder.finPeriodo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuerdo == null) ? 0 : acuerdo.hashCode());
		result = prime
				* result
				+ ((codigoPersonalizacion == null) ? 0 : codigoPersonalizacion
						.hashCode());
		result = prime
				* result
				+ ((conceptoFacturable == null) ? 0 : conceptoFacturable
						.hashCode());
		result = prime * result
				+ ((contratableCf == null) ? 0 : contratableCf.hashCode());
		result = prime
				* result
				+ ((descConceptoFacturable == null) ? 0
						: descConceptoFacturable.hashCode());
		result = prime
				* result
				+ ((descTipoDeServicio == null) ? 0 : descTipoDeServicio
						.hashCode());
		result = prime * result
				+ ((finPeriodo == null) ? 0 : finPeriodo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(importeAcuerdo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(importeOriginal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((iniPeriodo == null) ? 0 : iniPeriodo.hashCode());
		result = prime * result
				+ ((precioEspecial == null) ? 0 : precioEspecial.hashCode());
		result = prime
				* result
				+ ((productoServicioCf == null) ? 0 : productoServicioCf
						.hashCode());
		result = prime * result
				+ ((subtipoCf == null) ? 0 : subtipoCf.hashCode());
		result = prime * result + ((tipoCf == null) ? 0 : tipoCf.hashCode());
		result = prime * result
				+ ((tipoDeServicio == null) ? 0 : tipoDeServicio.hashCode());
		result = prime
				* result
				+ ((tipoPrecioEspecial == null) ? 0 : tipoPrecioEspecial
						.hashCode());
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
		ConceptoFacturable other = (ConceptoFacturable) obj;
		if (acuerdo == null) {
			if (other.acuerdo != null)
				return false;
		} else if (!acuerdo.equals(other.acuerdo))
			return false;
		if (codigoPersonalizacion == null) {
			if (other.codigoPersonalizacion != null)
				return false;
		} else if (!codigoPersonalizacion.equals(other.codigoPersonalizacion))
			return false;
		if (conceptoFacturable == null) {
			if (other.conceptoFacturable != null)
				return false;
		} else if (!conceptoFacturable.equals(other.conceptoFacturable))
			return false;
		if (contratableCf == null) {
			if (other.contratableCf != null)
				return false;
		} else if (!contratableCf.equals(other.contratableCf))
			return false;
		if (descConceptoFacturable == null) {
			if (other.descConceptoFacturable != null)
				return false;
		} else if (!descConceptoFacturable.equals(other.descConceptoFacturable))
			return false;
		if (descTipoDeServicio == null) {
			if (other.descTipoDeServicio != null)
				return false;
		} else if (!descTipoDeServicio.equals(other.descTipoDeServicio))
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
		if (Double.doubleToLongBits(importeAcuerdo) != Double
				.doubleToLongBits(other.importeAcuerdo))
			return false;
		if (Double.doubleToLongBits(importeOriginal) != Double
				.doubleToLongBits(other.importeOriginal))
			return false;
		if (iniPeriodo == null) {
			if (other.iniPeriodo != null)
				return false;
		} else if (!iniPeriodo.equals(other.iniPeriodo))
			return false;
		if (precioEspecial == null) {
			if (other.precioEspecial != null)
				return false;
		} else if (!precioEspecial.equals(other.precioEspecial))
			return false;
		if (productoServicioCf == null) {
			if (other.productoServicioCf != null)
				return false;
		} else if (!productoServicioCf.equals(other.productoServicioCf))
			return false;
		if (subtipoCf == null) {
			if (other.subtipoCf != null)
				return false;
		} else if (!subtipoCf.equals(other.subtipoCf))
			return false;
		if (tipoCf == null) {
			if (other.tipoCf != null)
				return false;
		} else if (!tipoCf.equals(other.tipoCf))
			return false;
		if (tipoDeServicio == null) {
			if (other.tipoDeServicio != null)
				return false;
		} else if (!tipoDeServicio.equals(other.tipoDeServicio))
			return false;
		if (tipoPrecioEspecial == null) {
			if (other.tipoPrecioEspecial != null)
				return false;
		} else if (!tipoPrecioEspecial.equals(other.tipoPrecioEspecial))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	
	
	
}