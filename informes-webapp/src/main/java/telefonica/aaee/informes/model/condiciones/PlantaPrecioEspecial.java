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
 * The persistent class for the tbl_planta_precios_especiales database table.
 * 
 */
@Entity
@Table(name="tbl_planta_precios_especiales")
@NamedQueries({ 
	@NamedQuery(name = "PPE.findAll", query = "SELECT c FROM PlantaPrecioEspecial c")
	, @NamedQuery(name = "FindByAcuerdoTipoDeServicioMulticonexionNCNCACF"
		, query = "SELECT c FROM PlantaPrecioEspecial c "
				+ "WHERE "
				+ "c.acuerdo = :ac "
				+ "and c.tipoDeServicio = :ts "
				+ "and c.multiconexion = :m "
				+ "and c.numeroComercial = :nc "
				+ "and c.numeroComercialAsociado = :nca "
				+ "and c.conceptoFacturable = :cf")
})
@XmlRootElement
public class PlantaPrecioEspecial 
	implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="idAcuerdo")
	private Long idAcuerdo;
	
	@Column(name="acuerdo")
	private String acuerdo;

	@Column(name="TIPO_DE_SERVICIO")
	private String tipoDeServicio;
	@Column(name="DESC_TIPO_DE_SERVICIO")
	private String descTipoDeServicio;

	@Column(name="MULTICONEXION")
	private String multiconexion;

	@Column(name="NUMERO_COMERCIAL")
	private String numeroComercial;

	@Column(name="NUMERO_COMERCIAL_ASOCIADO")
	private String numeroComercialAsociado;

	@Column(name="CONCEPTO_FACTURABLE")
	private String conceptoFacturable;

	@Column(name="DESC_CONCEPTO_FACTURABLE")
	private String descConceptoFacturable;

	@Column(name="MAX_IMPORTE_ESTANDAR_PRODUCTO")
	private double maxImporteEstandarProducto;
	
	@Column(name="MIN_IMPORTE_ESTANDAR_PRODUCTO")
	private double minImporteEstandarProducto;
	
	@Column(name="MAX_IMPORTE_UNITARIO")
	private double maxImporteUnitario;
	
	@Column(name="MIN_IMPORTE_UNITARIO")
	private double minImporteUnitario;
	
	@Column(name="precio_especial")
	private String precioEspecial;

	@Column(name="tipo_precio_especial")
	private String tipoPrecioEspecial;
	
	@Column(name="importe_acuerdo")
	private double importeAcuerdo;

	@Column(name="INI_PERIODO")
	private String iniPeriodo;

	@Column(name="FIN_PERIODO")
	private String finPeriodo;
	
	
	
	
	

    public PlantaPrecioEspecial() {
    }






	public Long getId() {
		return id;
	}






	public void setId(Long id) {
		this.id = id;
	}






	public Long getIdAcuerdo() {
		return idAcuerdo;
	}






	public void setIdAcuerdo(Long idAcuerdo) {
		this.idAcuerdo = idAcuerdo;
	}






	public String getAcuerdo() {
		return acuerdo;
	}






	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}






	public String getTipoDeServicio() {
		return tipoDeServicio;
	}






	public void setTipoDeServicio(String tipoDeServicio) {
		this.tipoDeServicio = tipoDeServicio;
	}






	public String getDescTipoDeServicio() {
		return descTipoDeServicio;
	}






	public void setDescTipoDeServicio(String descTipoDeServicio) {
		this.descTipoDeServicio = descTipoDeServicio;
	}






	public String getMulticonexion() {
		return multiconexion;
	}






	public void setMulticonexion(String multiconexion) {
		this.multiconexion = multiconexion;
	}






	public String getNumeroComercial() {
		return numeroComercial;
	}






	public void setNumeroComercial(String numeroComercial) {
		this.numeroComercial = numeroComercial;
	}






	public String getNumeroComercialAsociado() {
		return numeroComercialAsociado;
	}






	public void setNumeroComercialAsociado(String numeroComercialAsociado) {
		this.numeroComercialAsociado = numeroComercialAsociado;
	}






	public String getConceptoFacturable() {
		return conceptoFacturable;
	}






	public void setConceptoFacturable(String conceptoFacturable) {
		this.conceptoFacturable = conceptoFacturable;
	}






	public String getDescConceptoFacturable() {
		return descConceptoFacturable;
	}






	public void setDescConceptoFacturable(String descConceptoFacturable) {
		this.descConceptoFacturable = descConceptoFacturable;
	}






	public double getMaxImporteEstandarProducto() {
		return maxImporteEstandarProducto;
	}






	public void setMaxImporteEstandarProducto(double maxImporteEstandarProducto) {
		this.maxImporteEstandarProducto = maxImporteEstandarProducto;
	}






	public double getMinImporteEstandarProducto() {
		return minImporteEstandarProducto;
	}






	public void setMinImporteEstandarProducto(double minImporteEstandarProducto) {
		this.minImporteEstandarProducto = minImporteEstandarProducto;
	}






	public double getMaxImporteUnitario() {
		return maxImporteUnitario;
	}






	public void setMaxImporteUnitario(double maxImporteUnitario) {
		this.maxImporteUnitario = maxImporteUnitario;
	}






	public double getMinImporteUnitario() {
		return minImporteUnitario;
	}






	public void setMinImporteUnitario(double minImporteUnitario) {
		this.minImporteUnitario = minImporteUnitario;
	}






	public String getPrecioEspecial() {
		return precioEspecial;
	}






	public void setPrecioEspecial(String precioEspecial) {
		this.precioEspecial = precioEspecial;
	}






	public String getTipoPrecioEspecial() {
		return tipoPrecioEspecial;
	}






	public void setTipoPrecioEspecial(String tipoPrecioEspecial) {
		this.tipoPrecioEspecial = tipoPrecioEspecial;
	}






	public double getImporteAcuerdo() {
		return importeAcuerdo;
	}






	public void setImporteAcuerdo(double importeAcuerdo) {
		this.importeAcuerdo = importeAcuerdo;
	}






	public String getIniPeriodo() {
		return iniPeriodo;
	}






	public void setIniPeriodo(String iniPeriodo) {
		this.iniPeriodo = iniPeriodo;
	}






	public String getFinPeriodo() {
		return finPeriodo;
	}






	public void setFinPeriodo(String finPeriodo) {
		this.finPeriodo = finPeriodo;
	}






	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuerdo == null) ? 0 : acuerdo.hashCode());
		result = prime
				* result
				+ ((conceptoFacturable == null) ? 0 : conceptoFacturable
						.hashCode());
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
		result = prime * result
				+ ((idAcuerdo == null) ? 0 : idAcuerdo.hashCode());
		long temp;
		temp = Double.doubleToLongBits(importeAcuerdo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((iniPeriodo == null) ? 0 : iniPeriodo.hashCode());
		temp = Double.doubleToLongBits(maxImporteEstandarProducto);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxImporteUnitario);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minImporteEstandarProducto);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minImporteUnitario);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((multiconexion == null) ? 0 : multiconexion.hashCode());
		result = prime * result
				+ ((numeroComercial == null) ? 0 : numeroComercial.hashCode());
		result = prime
				* result
				+ ((numeroComercialAsociado == null) ? 0
						: numeroComercialAsociado.hashCode());
		result = prime * result
				+ ((precioEspecial == null) ? 0 : precioEspecial.hashCode());
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
		PlantaPrecioEspecial other = (PlantaPrecioEspecial) obj;
		if (acuerdo == null) {
			if (other.acuerdo != null)
				return false;
		} else if (!acuerdo.equals(other.acuerdo))
			return false;
		if (conceptoFacturable == null) {
			if (other.conceptoFacturable != null)
				return false;
		} else if (!conceptoFacturable.equals(other.conceptoFacturable))
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
		if (idAcuerdo == null) {
			if (other.idAcuerdo != null)
				return false;
		} else if (!idAcuerdo.equals(other.idAcuerdo))
			return false;
		if (Double.doubleToLongBits(importeAcuerdo) != Double
				.doubleToLongBits(other.importeAcuerdo))
			return false;
		if (iniPeriodo == null) {
			if (other.iniPeriodo != null)
				return false;
		} else if (!iniPeriodo.equals(other.iniPeriodo))
			return false;
		if (Double.doubleToLongBits(maxImporteEstandarProducto) != Double
				.doubleToLongBits(other.maxImporteEstandarProducto))
			return false;
		if (Double.doubleToLongBits(maxImporteUnitario) != Double
				.doubleToLongBits(other.maxImporteUnitario))
			return false;
		if (Double.doubleToLongBits(minImporteEstandarProducto) != Double
				.doubleToLongBits(other.minImporteEstandarProducto))
			return false;
		if (Double.doubleToLongBits(minImporteUnitario) != Double
				.doubleToLongBits(other.minImporteUnitario))
			return false;
		if (multiconexion == null) {
			if (other.multiconexion != null)
				return false;
		} else if (!multiconexion.equals(other.multiconexion))
			return false;
		if (numeroComercial == null) {
			if (other.numeroComercial != null)
				return false;
		} else if (!numeroComercial.equals(other.numeroComercial))
			return false;
		if (numeroComercialAsociado == null) {
			if (other.numeroComercialAsociado != null)
				return false;
		} else if (!numeroComercialAsociado
				.equals(other.numeroComercialAsociado))
			return false;
		if (precioEspecial == null) {
			if (other.precioEspecial != null)
				return false;
		} else if (!precioEspecial.equals(other.precioEspecial))
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






	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlantaPrecioEspecial [id=");
		builder.append(id);
		builder.append(", idAcuerdo=");
		builder.append(idAcuerdo);
		builder.append(", acuerdo=");
		builder.append(acuerdo);
		builder.append(", tipoDeServicio=");
		builder.append(tipoDeServicio);
		builder.append(", descTipoDeServicio=");
		builder.append(descTipoDeServicio);
		builder.append(", multiconexion=");
		builder.append(multiconexion);
		builder.append(", numeroComercial=");
		builder.append(numeroComercial);
		builder.append(", numeroComercialAsociado=");
		builder.append(numeroComercialAsociado);
		builder.append(", conceptoFacturable=");
		builder.append(conceptoFacturable);
		builder.append(", descConceptoFacturable=");
		builder.append(descConceptoFacturable);
		builder.append(", maxImporteEstandarProducto=");
		builder.append(maxImporteEstandarProducto);
		builder.append(", minImporteEstandarProducto=");
		builder.append(minImporteEstandarProducto);
		builder.append(", maxImporteUnitario=");
		builder.append(maxImporteUnitario);
		builder.append(", minImporteUnitario=");
		builder.append(minImporteUnitario);
		builder.append(", precioEspecial=");
		builder.append(precioEspecial);
		builder.append(", tipoPrecioEspecial=");
		builder.append(tipoPrecioEspecial);
		builder.append(", importeAcuerdo=");
		builder.append(importeAcuerdo);
		builder.append(", iniPeriodo=");
		builder.append(iniPeriodo);
		builder.append(", finPeriodo=");
		builder.append(finPeriodo);
		builder.append("]");
		return builder.toString();
	}

    
    
	
}