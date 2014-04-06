package telefonica.aaee.model.datos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the tbl_acuerdos database table.
 * 
 */
@Entity
@Table(name="tbl_ficheros")
@XmlRootElement
public class TblFicheros 

	extends telefonica.aaee.model.GenericXML
	implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="fichero")
	private String fichero;
	
	@Column(name="FECHA_FACTURA")
	private String fechaFactura;
	
	@Column(name="CIF_CLIENTE_Clave")
	private String cifSupraCliente;

   
    


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}














	/**
	 * @return the fichero
	 */
	public String getFichero() {
		return fichero;
	}

	/**
	 * @param fichero the fichero to set
	 */
	public void setFichero(String fichero) {
		this.fichero = fichero;
	}

	/**
	 * @return the fechaFactura
	 */
	public String getFechaFactura() {
		return fechaFactura;
	}

	/**
	 * @param fechaFactura the fechaFactura to set
	 */
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	/**
	 * @return the cifSupraCliente
	 */
	public String getCifSupraCliente() {
		return cifSupraCliente;
	}

	/**
	 * @param cifSupraCliente the cifSupraCliente to set
	 */
	public void setCifSupraCliente(String cifSupraCliente) {
		this.cifSupraCliente = cifSupraCliente;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TblFicheros [id=");
		builder.append(id);
		builder.append(", fichero=");
		builder.append(fichero);
		builder.append(", fechaFactura=");
		builder.append(fechaFactura);
		builder.append(", cifSupraCliente=");
		builder.append(cifSupraCliente);
		builder.append("]");
		return builder.toString();
	}


}