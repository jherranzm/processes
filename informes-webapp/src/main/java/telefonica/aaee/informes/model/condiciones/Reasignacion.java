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
 * The persistent class for the tbl_reasignacion database table.
 * 
 */
@Entity
@Table(name="tbl_reasignacion")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="FindByAcuerdoNumeroComercialAsociado"
			, query="SELECT c FROM Reasignacion c "
							+ "WHERE "
							+ "1 = 1 "
							+ "and c.acuerdo = :ac "
							+ "and c.multiconexion = :m " 
							+ "and c.numeroComercial = :nc "
							+ "and c.numeroComercialAsociado = :nca ")
})
public class Reasignacion 
	implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String acuerdo;

	@Column(name="idAcuerdo")
	private Long idAcuerdo;

	@Column(name="cif_nuevo")
	private String cifNuevo;

	@Column(name="nombre_nuevo")
	private String nombreNuevo;

	@Column(name="cif_original")
	private String cifOriginal;

	@Column(name="nombre_original")
	private String nombreOriginal;

	private String multiconexion;

	@Column(name="numero_comercial")
	private String numeroComercial;

	@Column(name="numero_comercial_asociado")
	private String numeroComercialAsociado;

	private String centroCoste;

	private String numeroCuenta;



	
	
	
	
	
	
	
	
	
	
	
	public Reasignacion() {
    }














	public Long getId() {
		return id;
	}














	public void setId(Long id) {
		this.id = id;
	}














	public String getAcuerdo() {
		return acuerdo;
	}














	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}














	public Long getIdAcuerdo() {
		return idAcuerdo;
	}














	public void setIdAcuerdo(Long idAcuerdo) {
		this.idAcuerdo = idAcuerdo;
	}














	public String getCifNuevo() {
		return cifNuevo;
	}














	public void setCifNuevo(String cifNuevo) {
		this.cifNuevo = cifNuevo;
	}














	public String getNombreNuevo() {
		return nombreNuevo;
	}














	public void setNombreNuevo(String nombreNuevo) {
		this.nombreNuevo = nombreNuevo;
	}














	public String getCifOriginal() {
		return cifOriginal;
	}














	public void setCifOriginal(String cifOriginal) {
		this.cifOriginal = cifOriginal;
	}














	public String getNombreOriginal() {
		return nombreOriginal;
	}














	public void setNombreOriginal(String nombreOriginal) {
		this.nombreOriginal = nombreOriginal;
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














	public String getCentroCoste() {
		return centroCoste;
	}














	public void setCentroCoste(String centroCoste) {
		this.centroCoste = centroCoste;
	}














	public String getNumeroCuenta() {
		return numeroCuenta;
	}














	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

























	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuerdo == null) ? 0 : acuerdo.hashCode());
		result = prime * result
				+ ((centroCoste == null) ? 0 : centroCoste.hashCode());
		result = prime * result
				+ ((cifNuevo == null) ? 0 : cifNuevo.hashCode());
		result = prime * result
				+ ((cifOriginal == null) ? 0 : cifOriginal.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idAcuerdo == null) ? 0 : idAcuerdo.hashCode());
		result = prime * result
				+ ((multiconexion == null) ? 0 : multiconexion.hashCode());
		result = prime * result
				+ ((nombreNuevo == null) ? 0 : nombreNuevo.hashCode());
		result = prime * result
				+ ((nombreOriginal == null) ? 0 : nombreOriginal.hashCode());
		result = prime * result
				+ ((numeroComercial == null) ? 0 : numeroComercial.hashCode());
		result = prime
				* result
				+ ((numeroComercialAsociado == null) ? 0
						: numeroComercialAsociado.hashCode());
		result = prime * result
				+ ((numeroCuenta == null) ? 0 : numeroCuenta.hashCode());
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
		Reasignacion other = (Reasignacion) obj;
		if (acuerdo == null) {
			if (other.acuerdo != null)
				return false;
		} else if (!acuerdo.equals(other.acuerdo))
			return false;
		if (centroCoste == null) {
			if (other.centroCoste != null)
				return false;
		} else if (!centroCoste.equals(other.centroCoste))
			return false;
		if (cifNuevo == null) {
			if (other.cifNuevo != null)
				return false;
		} else if (!cifNuevo.equals(other.cifNuevo))
			return false;
		if (cifOriginal == null) {
			if (other.cifOriginal != null)
				return false;
		} else if (!cifOriginal.equals(other.cifOriginal))
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
		if (multiconexion == null) {
			if (other.multiconexion != null)
				return false;
		} else if (!multiconexion.equals(other.multiconexion))
			return false;
		if (nombreNuevo == null) {
			if (other.nombreNuevo != null)
				return false;
		} else if (!nombreNuevo.equals(other.nombreNuevo))
			return false;
		if (nombreOriginal == null) {
			if (other.nombreOriginal != null)
				return false;
		} else if (!nombreOriginal.equals(other.nombreOriginal))
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
		if (numeroCuenta == null) {
			if (other.numeroCuenta != null)
				return false;
		} else if (!numeroCuenta.equals(other.numeroCuenta))
			return false;
		return true;
	}














	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Reasignacion [id=");
		builder.append(id);
		builder.append(", acuerdo=");
		builder.append(acuerdo);
		builder.append(", idAcuerdo=");
		builder.append(idAcuerdo);
		builder.append(", cifNuevo=");
		builder.append(cifNuevo);
		builder.append(", nombreNuevo=");
		builder.append(nombreNuevo);
		builder.append(", cifOriginal=");
		builder.append(cifOriginal);
		builder.append(", nombreOriginal=");
		builder.append(nombreOriginal);
		builder.append(", multiconexion=");
		builder.append(multiconexion);
		builder.append(", numeroComercial=");
		builder.append(numeroComercial);
		builder.append(", numeroComercialAsociado=");
		builder.append(numeroComercialAsociado);
		builder.append(", centroCoste=");
		builder.append(centroCoste);
		builder.append(", numeroCuenta=");
		builder.append(numeroCuenta);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}