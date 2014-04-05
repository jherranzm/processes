package telefonica.aaee.informes.model.condiciones;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the tbl_planta_cond_especiales database table.
 * 
 */
@Entity
@Table(name="tbl_planta_cond_especiales")
@XmlRootElement
public class PlantaCondEspecial 
	implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String cif;
	private String acuerdo;

	@Column(name="CONCEPTO_FACTURABLE")
	private String conceptoFacturable;

	@Column(name="DESC_CONCEPTO_FACTURABLE")
	private String descConceptoFacturable;

	@Column(name="DESC_TIPO_DE_SERVICIO")
	private String descTipoDeServicio;

	@Column(name="importe_acuerdo")
	private double importeAcuerdo;

	private String multiconexion;

	@Column(name="NOMBRE_CLIENTE")
	private String nombreCliente;

	@Column(name="NUMERO_COMERCIAL")
	private String numeroComercial;

	@Column(name="NUMERO_COMERCIAL_ASOCIADO")
	private String numeroComercialAsociado;

    @Column(name="precio_especial")
	private String precioEspecial;

	@Column(name="TIPO_DE_SERVICIO")
	private String tipoDeServicio;

	private String tipo_Doc;

	@Column(name="tipo_precio_especial")
	private String tipoPrecioEspecial;
	
	
	@Lob()
	private String obs1;
	@Lob()
	private String obs2;
	@Lob()
	private String obs3;
	
	
	
	
	
	
	
	
	

    public PlantaCondEspecial() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCif() {
		return this.cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getConceptoFacturable() {
		return this.conceptoFacturable;
	}

	public void setConceptoFacturable(String conceptoFacturable) {
		this.conceptoFacturable = conceptoFacturable;
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

	public String getMulticonexion() {
		return this.multiconexion;
	}

	public void setMulticonexion(String multiconexion) {
		this.multiconexion = multiconexion;
	}

	public String getNombreCliente() {
		return this.nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNumeroComercial() {
		return this.numeroComercial;
	}

	public void setNumeroComercial(String numeroComercial) {
		this.numeroComercial = numeroComercial;
	}

	public String getNumeroComercialAsociado() {
		return this.numeroComercialAsociado;
	}

	public void setNumeroComercialAsociado(String numeroComercialAsociado) {
		this.numeroComercialAsociado = numeroComercialAsociado;
	}

	public String getPrecioEspecial() {
		return this.precioEspecial;
	}

	public void setPrecioEspecial(String precioEspecial) {
		this.precioEspecial = precioEspecial;
	}

	public String getTipoDeServicio() {
		return this.tipoDeServicio;
	}

	public void setTipoDeServicio(String tipoDeServicio) {
		this.tipoDeServicio = tipoDeServicio;
	}

	public String getTipo_Doc() {
		return this.tipo_Doc;
	}

	public void setTipo_Doc(String tipo_Doc) {
		this.tipo_Doc = tipo_Doc;
	}

	public String getTipoPrecioEspecial() {
		return this.tipoPrecioEspecial;
	}

	public void setTipoPrecioEspecial(String tipoPrecioEspecial) {
		this.tipoPrecioEspecial = tipoPrecioEspecial;
	}

	/**
	 * @return acuerdo
	 */
	public String getAcuerdo() {
		return acuerdo;
	}

	/**
	 * @param acuerdo valor a asignar al campo acuerdo
	 */
	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}

	
	/**
	 * @return obs1
	 */
	public String getObs1() {
		return obs1;
	}

	
	/**
	 * @param obs1 valor a asignar al campo obs1
	 */
	public void setObs1(String obs1) {
		this.obs1 = obs1;
	}

	
	/**
	 * @return obs2
	 */
	public String getObs2() {
		return obs2;
	}

	
	/**
	 * @param obs2 valor a asignar al campo obs2
	 */
	public void setObs2(String obs2) {
		this.obs2 = obs2;
	}

	
	/**
	 * @return obs3
	 */
	public String getObs3() {
		return obs3;
	}

	
	/**
	 * @param obs3 valor a asignar al campo obs3
	 */
	public void setObs3(String obs3) {
		this.obs3 = obs3;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlantaCondEspecial [id=" + id + ",\\n cif=" + cif + ",\\n acuerdo=" + acuerdo
				+ ",\\n conceptoFacturable=" + conceptoFacturable + ",\\n descConceptoFacturable="
				+ descConceptoFacturable + ",\\n descTipoDeServicio=" + descTipoDeServicio + ",\\n importeAcuerdo="
				+ importeAcuerdo + ",\\n multiconexion=" + multiconexion + ",\\n nombreCliente=" + nombreCliente
				+ ",\\n numeroComercial=" + numeroComercial + ",\\n numeroComercialAsociado=" + numeroComercialAsociado
				+ ",\\n \\n precioEspecial=" + precioEspecial + ",\\n tipoDeServicio=" + tipoDeServicio
				+ ",\\n tipo_Doc=" + tipo_Doc + ",\\n tipoPrecioEspecial=" + tipoPrecioEspecial + ",\\n obs1=" + obs1
				+ ",\\n obs2=" + obs2 + ",\\n obs3=" + obs3 + "]";
	}

	@Override
	public int hashCode(){
		int  result = 17;
		result = 31 * result + acuerdo.hashCode();
		result = 13 * result + cif.hashCode();
		result = 19 * result + multiconexion.hashCode();
		result = 17 * result + numeroComercial.hashCode();
		result = 23 * result + conceptoFacturable.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof PlantaCondEspecial)) return false;
		
		PlantaCondEspecial p = (PlantaCondEspecial) o;
		if(p.acuerdo == null || !p.acuerdo.equals(this.acuerdo)) return false;
		if(p.cif == null || !p.cif.equals(this.cif)) return false;
		if(p.multiconexion == null || !p.multiconexion.equals(this.multiconexion)) return false;
		if(p.numeroComercial == null || !p.numeroComercial.equals(this.numeroComercial)) return false;
		if(p.conceptoFacturable == null || !p.conceptoFacturable.equals(this.conceptoFacturable)) return false;
		return true;
	}

}