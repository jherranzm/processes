package telefonica.aaee.informes.model.condiciones;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the tbl_reasignacion database table.
 * 
 */
@Entity
@Table(name="tbl_reasignacion")
@XmlRootElement
public class Reasignacion 
	implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String acuerdo;

	private String multiconexion;

	@Column(name="numero_comercial")
	private String numeroComercial;

	@Column(name="numero_comercial_asociado")
	private String numeroComercialAsociado;

	private String centroCoste;

	private String numeroCuenta;

	@Column(name="cif_nuevo")
	private String cifNuevo;

	@Column(name="nombre_nuevo")
	private String nombreNuevo;


	
	
	
	
	
	
	
	
	
	
	
	public Reasignacion() {
    }

	public String getAcuerdo() {
		return this.acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}

	public String getCentroCoste() {
		return this.centroCoste;
	}

	public void setCentroCoste(String centroCoste) {
		this.centroCoste = centroCoste;
	}

	public String getCifNuevo() {
		return this.cifNuevo;
	}

	public void setCifNuevo(String cifNuevo) {
		this.cifNuevo = cifNuevo;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMultiConexion() {
		return this.multiconexion;
	}

	public void setMultiConexion(String multiConexion) {
		this.multiconexion = multiConexion;
	}

	public String getNombreNuevo() {
		return this.nombreNuevo;
	}

	public void setNombreNuevo(String nombreNuevo) {
		this.nombreNuevo = nombreNuevo;
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

	public void setNumeroComercialAsociado(String numero_Comercial_Asociado) {
		this.numeroComercialAsociado = numero_Comercial_Asociado;
	}

	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	
	@Override
	public int hashCode(){
		int  result = 17;
		result = 17 * result + numeroComercialAsociado.hashCode();
		result = 23 * result + centroCoste.hashCode();
		result = 37 * result + numeroCuenta.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof Reasignacion)) return false;
		
		Reasignacion p = (Reasignacion) o;
		if(p.numeroComercialAsociado == null || !p.numeroComercialAsociado.equals(this.numeroComercialAsociado)) return false;
		if(p.centroCoste == null || !p.centroCoste.equals(this.centroCoste)) return false;
		if(p.numeroCuenta == null || !p.numeroCuenta.equals(this.numeroCuenta)) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Reasignacion [id=" + id + ",\\n acuerdo=" + acuerdo + ",\\n cifNuevo=" + cifNuevo + ",\\n nombreNuevo="
				+ nombreNuevo + ",\\n multiconexion=" + multiconexion + ",\\n numeroComercial=" + numeroComercial
				+ ",\\n numeroComercialAsociado=" + numeroComercialAsociado + ",\\n centroCoste=" + centroCoste
				+ ",\\n numeroCuenta=" + numeroCuenta + "]";
	}

	
	
	
}