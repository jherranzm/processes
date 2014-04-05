package telefonica.aaee.model.model977r;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the tconceptosfacturables database table.
 * 
 */
@Entity
@Table(name="tconceptosfacturables")
@XmlRootElement
public class ConceptoFacturable 

	implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String acuerdo;

	@Column(name="CONCEPTO_FACTURABLE", nullable = false)
	private String conceptoFacturable = "";

	@Column(name="CODIGO_PERSONALIZACION", nullable = false)
	private String codigoPersonalizacion = "";

	@Column(name="DESC_CONCEPTO_FACTURABLE", nullable = false)
	private String descConceptoFacturable = "";

	@Column(name="DESC_TIPO_DE_SERVICIO", nullable = false)
	private String descTipoDeServicio = "";

	@Column(name="importe_original", columnDefinition="Decimal(21,4) default '0.0000'", nullable = false)
	private double importeOriginal;

	@Column(name="importe_acuerdo", columnDefinition="Decimal(21,4) default '0.0000'", nullable = false)
	private double importeAcuerdo;

	@Column(name="precio_especial", nullable = false)
	private String precioEspecial;

	@Column(name="tipo_cf")
	private String tipoCf = "";

	@Column(name="subtipo_cf")
	private String subtipoCf = "";

	@Column(name="producto_servicio_cf")
	private String productoServicioCf = "";

	@Column(name="contratable_cf")
	private String contratableCf = "";

	@Column(name="TIPO_DE_SERVICIO", nullable = false)
	private String tipoDeServicio = "";

	@Column(name="tipo_precio_especial", nullable = false)
	private String tipoPrecioEspecial = "";

	@Column(name="ini_periodo", nullable = false)
	private String iniPeriodo = "20110128";

	@Column(name="fin_periodo", nullable = false)
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

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
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
	
	
	
	
}