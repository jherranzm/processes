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

@Entity
@Table(name="tbl_reasignacion_cargos")
@NamedQueries({
	@NamedQuery(name="FindByAcuerdoCifGrupoDeGastoAgrupacionFacturable"
			, query="SELECT c FROM ReasignacionCargo c "
							+ "WHERE "
							+ "1 = 1 "
							+ "and c.acuerdo = :ac "
							+ "and c.cif = :c " 
							+ "and c.grupoDeGasto = :g "
							+ "and c.agrupacionFacturable = :af ")
})
@XmlRootElement
public class ReasignacionCargo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="acuerdo")
	private String acuerdo;

	@Column(name="tipo_doc")
	private String tipoDoc;

	@Column(name="cif")
	private String cif;
	
	@Column(name="nombre_cliente")
	private String nombreCliente;
	
	@Column(name="grupo_de_gasto")
	private String grupoDeGasto;

	@Column(name="agrupacion_facturable")
	private String agrupacionFacturable;
	
	@Column(name="tipo_doc_reasignado")
	private String tipoDocReasignado;
	
	@Column(name="cif_reasignado")
	private String cifReasignado;
	
	@Column(name="nombre_cliente_reasignado")
	private String nombreClienteReasignado;
	
	@Column(name="grupo_de_gasto_reasignado")
	private String grupoDeGastoReasignado;

	@Column(name="agrupacion_facturable_reasignado")
	private String agrupacionFacturableReasignado;
	
	@Column(name="comentarios")
	private String comentarios;
	
	public ReasignacionCargo() {
		super();
	}

	/**
	 * Getters & Setters
	 */
	

	public long getId() {
		return id;
	}


	public String getTipoDoc() {
		return tipoDoc;
	}


	public String getCif() {
		return cif;
	}


	public String getNombreCliente() {
		return nombreCliente;
	}


	public String getAgrupacionFacturable() {
		return agrupacionFacturable;
	}


	public String getTipoDocReasignado() {
		return tipoDocReasignado;
	}


	public String getCifReasignado() {
		return cifReasignado;
	}


	public String getNombreClienteReasignado() {
		return nombreClienteReasignado;
	}


	public String getAgrupacionFacturableReasignado() {
		return agrupacionFacturableReasignado;
	}


	public void setId(long id) {
		this.id = id;
	}


	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}


	public void setCif(String cif) {
		this.cif = cif;
	}


	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}


	public void setAgrupacionFacturable(String agrupacionFacturable) {
		this.agrupacionFacturable = agrupacionFacturable;
	}


	public void setTipoDocReasignado(String tipoDocReasignado) {
		this.tipoDocReasignado = tipoDocReasignado;
	}


	public void setCifReasignado(String cifReasignado) {
		this.cifReasignado = cifReasignado;
	}


	public void setNombreClienteReasignado(String nombreClienteReasignado) {
		this.nombreClienteReasignado = nombreClienteReasignado;
	}


	public void setAgrupacionFacturableReasignado(
			String agrupacionFacturableReasignado) {
		this.agrupacionFacturableReasignado = agrupacionFacturableReasignado;
	}
	
	public String getGrupoDeGasto() {
		return grupoDeGasto;
	}

	public void setGrupoDeGasto(String grupoDeGasto) {
		this.grupoDeGasto = grupoDeGasto;
	}

	public String getGrupoDeGastoReasignado() {
		return grupoDeGastoReasignado;
	}

	public void setGrupoDeGastoReasignado(String grupoDeGastoReasignado) {
		this.grupoDeGastoReasignado = grupoDeGastoReasignado;
	}
	
	public String getAcuerdo() {
		return acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}
	
	

	/**
	 * 
	 * equals & hashCode
	 * 
	 */


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuerdo == null) ? 0 : acuerdo.hashCode());
		result = prime
				* result
				+ ((agrupacionFacturable == null) ? 0 : agrupacionFacturable
						.hashCode());
		result = prime
				* result
				+ ((agrupacionFacturableReasignado == null) ? 0
						: agrupacionFacturableReasignado.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime * result
				+ ((cifReasignado == null) ? 0 : cifReasignado.hashCode());
		result = prime * result
				+ ((grupoDeGasto == null) ? 0 : grupoDeGasto.hashCode());
		result = prime
				* result
				+ ((grupoDeGastoReasignado == null) ? 0
						: grupoDeGastoReasignado.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((nombreCliente == null) ? 0 : nombreCliente.hashCode());
		result = prime
				* result
				+ ((nombreClienteReasignado == null) ? 0
						: nombreClienteReasignado.hashCode());
		result = prime * result + ((tipoDoc == null) ? 0 : tipoDoc.hashCode());
		result = prime
				* result
				+ ((tipoDocReasignado == null) ? 0 : tipoDocReasignado
						.hashCode());
		return result;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReasignacionCargo other = (ReasignacionCargo) obj;
		if (acuerdo == null) {
			if (other.acuerdo != null)
				return false;
		} else if (!acuerdo.equals(other.acuerdo))
			return false;
		if (agrupacionFacturable == null) {
			if (other.agrupacionFacturable != null)
				return false;
		} else if (!agrupacionFacturable.equals(other.agrupacionFacturable))
			return false;
		if (agrupacionFacturableReasignado == null) {
			if (other.agrupacionFacturableReasignado != null)
				return false;
		} else if (!agrupacionFacturableReasignado
				.equals(other.agrupacionFacturableReasignado))
			return false;
		if (cif == null) {
			if (other.cif != null)
				return false;
		} else if (!cif.equals(other.cif))
			return false;
		if (cifReasignado == null) {
			if (other.cifReasignado != null)
				return false;
		} else if (!cifReasignado.equals(other.cifReasignado))
			return false;
		if (grupoDeGasto == null) {
			if (other.grupoDeGasto != null)
				return false;
		} else if (!grupoDeGasto.equals(other.grupoDeGasto))
			return false;
		if (grupoDeGastoReasignado == null) {
			if (other.grupoDeGastoReasignado != null)
				return false;
		} else if (!grupoDeGastoReasignado.equals(other.grupoDeGastoReasignado))
			return false;
		if (id != other.id)
			return false;
		if (nombreCliente == null) {
			if (other.nombreCliente != null)
				return false;
		} else if (!nombreCliente.equals(other.nombreCliente))
			return false;
		if (nombreClienteReasignado == null) {
			if (other.nombreClienteReasignado != null)
				return false;
		} else if (!nombreClienteReasignado
				.equals(other.nombreClienteReasignado))
			return false;
		if (tipoDoc == null) {
			if (other.tipoDoc != null)
				return false;
		} else if (!tipoDoc.equals(other.tipoDoc))
			return false;
		if (tipoDocReasignado == null) {
			if (other.tipoDocReasignado != null)
				return false;
		} else if (!tipoDocReasignado.equals(other.tipoDocReasignado))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReasignacionCargo [id=");
		builder.append(id);
		builder.append(", acuerdo=");
		builder.append(acuerdo);
		builder.append(", tipoDoc=");
		builder.append(tipoDoc);
		builder.append(", cif=");
		builder.append(cif);
		builder.append(", nombreCliente=");
		builder.append(nombreCliente);
		builder.append(", grupoDeGasto=");
		builder.append(grupoDeGasto);
		builder.append(", agrupacionFacturable=");
		builder.append(agrupacionFacturable);
		builder.append(", tipoDocReasignado=");
		builder.append(tipoDocReasignado);
		builder.append(", cifReasignado=");
		builder.append(cifReasignado);
		builder.append(", nombreClienteReasignado=");
		builder.append(nombreClienteReasignado);
		builder.append(", grupoDeGastoReasignado=");
		builder.append(grupoDeGastoReasignado);
		builder.append(", agrupacionFacturableReasignado=");
		builder.append(agrupacionFacturableReasignado);
		builder.append(", comentarios=");
		builder.append(comentarios);
		builder.append("]");
		return builder.toString();
	}


	
	
	
	
	
	
}
