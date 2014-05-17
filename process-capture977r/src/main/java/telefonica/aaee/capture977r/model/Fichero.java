package telefonica.aaee.capture977r.model;

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
 * The persistent class for the tbl_acuerdos database table.
 * 
 */
@Entity
@Table(name = "tbl_ficheros")
@NamedQueries({
		@NamedQuery(name = "Fichero.findAll", 
					query = "SELECT c FROM Fichero c"),
		@NamedQuery(name = "Fichero.findByNombre", 
					query = "SELECT p FROM Fichero p "
							+ "WHERE 1 = 1 "
							+ "AND p.fichero = :nom "),
		@NamedQuery(name = "Fichero.findByNombreFechaFactura", 
					query="select u from Fichero u "
							+ "WHERE 1 = 1 "
							+ "and u.fichero = :elfichero "
							+ "and u.fechaFactura = :laFecha "),
		@NamedQuery(name = "Fichero.findByNombreFechaFacturaCif", 
					query="select u from Fichero u "
							+ "WHERE 1 = 1 "
							+ "and u.fichero = :elfichero "
							+ "and u.fechaFactura = :laFecha "
							+ "and u.cifSupraCliente = :elCif ")
})
@XmlRootElement
public class Fichero

implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fichero")
	private String fichero;

	@Column(name = "FECHA_FACTURA")
	private String fechaFactura;

	@Column(name = "CIF_CLIENTE_Clave")
	private String cifSupraCliente;
	
	
	

	public Fichero() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Fichero(Long id, String fichero, String fechaFactura,
			String cifSupraCliente) {
		super();
		this.id = id;
		this.fichero = fichero;
		this.fechaFactura = fechaFactura;
		this.cifSupraCliente = cifSupraCliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cifSupraCliente == null) ? 0 : cifSupraCliente.hashCode());
		result = prime * result
				+ ((fechaFactura == null) ? 0 : fechaFactura.hashCode());
		result = prime * result + ((fichero == null) ? 0 : fichero.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Fichero other = (Fichero) obj;
		if (cifSupraCliente == null) {
			if (other.cifSupraCliente != null)
				return false;
		} else if (!cifSupraCliente.equals(other.cifSupraCliente))
			return false;
		if (fechaFactura == null) {
			if (other.fechaFactura != null)
				return false;
		} else if (!fechaFactura.equals(other.fechaFactura))
			return false;
		if (fichero == null) {
			if (other.fichero != null)
				return false;
		} else if (!fichero.equals(other.fichero))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class Builder {
		private Long id;
		private String fichero;
		private String fechaFactura;
		private String cifSupraCliente;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder fichero(String fichero) {
			this.fichero = fichero;
			return this;
		}

		public Builder fechaFactura(String fechaFactura) {
			this.fechaFactura = fechaFactura;
			return this;
		}

		public Builder cifSupraCliente(String cifSupraCliente) {
			this.cifSupraCliente = cifSupraCliente;
			return this;
		}

		public Fichero build() {
			return new Fichero(this);
		}
	}

	private Fichero(Builder builder) {
		this.id = builder.id;
		this.fichero = builder.fichero;
		this.fechaFactura = builder.fechaFactura;
		this.cifSupraCliente = builder.cifSupraCliente;
	}

	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Fichero [id=");
		builder2.append(id);
		builder2.append(", fichero=");
		builder2.append(fichero);
		builder2.append(", fechaFactura=");
		builder2.append(fechaFactura);
		builder2.append(", cifSupraCliente=");
		builder2.append(cifSupraCliente);
		builder2.append("]");
		return builder2.toString();
	}
	
	
	
}