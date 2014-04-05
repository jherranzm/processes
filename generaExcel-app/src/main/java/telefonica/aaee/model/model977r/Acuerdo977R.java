package telefonica.aaee.model.model977r;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the tconceptosfacturables database table.
 * 
 */
@Entity
@Table(name="t000000")
@XmlRootElement
public class Acuerdo977R 

	extends telefonica.aaee.xml.GenericXML
	implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(name="ACUERDO", nullable = false)
	private String acuerdo;

	@Column(name="fichero", nullable = false)
	private String fichero = "";

	@Column(name="FECHA_FACTURA", nullable = false)
	private String fechaFactura;

	@Column(name="CIF_CLIENTE_Clave", nullable = false)
	private String supraCliente = "";
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_CARGA", nullable = false)
	private Date fechaCarga;


	
	
	

    public Acuerdo977R() {
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



	
	/**
	 * @return fichero
	 */
	public final String getFichero() {
		return fichero;
	}



	
	/**
	 * @param fichero valor a asignar al campo fichero
	 */
	public final void setFichero(String fichero) {
		this.fichero = fichero;
	}



	
	/**
	 * @return fechaFactura
	 */
	public final String getFechaFactura() {
		return fechaFactura;
	}



	
	/**
	 * @param fechaFactura valor a asignar al campo fechaFactura
	 */
	public final void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}



	
	/**
	 * @return fechaCarga
	 */
	public final Date getFechaCarga() {
		return fechaCarga;
	}



	
	/**
	 * @param fechaCarga valor a asignar al campo fechaCarga
	 */
	public final void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}



	
	/**
	 * @return supraCliente
	 */
	public final String getSupraCliente() {
		return supraCliente;
	}



	
	/**
	 * @param supraCliente valor a asignar al campo supraCliente
	 */
	public final void setSupraCliente(String supraCliente) {
		this.supraCliente = supraCliente;
	}


	
}