package telefonica.aaee.informes.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tbl_informe_pestanya database table.
 * 
 */
@Entity
@Table(name="tbl_informe_pestanya")
@NamedQuery(name="InformePestanya.findAll", query="SELECT i FROM InformePestanya i")
public class InformePestanya implements Serializable {
	private static final long serialVersionUID = 1L;

	private int orden;

	//bi-directional many-to-one association to Informe
	@Id
	@ManyToOne()
	@JoinColumn(name="informe_id")
	private Informe informe;

	//bi-directional many-to-one association to Pestanya
	@Id
	@ManyToOne()
	@JoinColumn(name="pestanya_id")
	private Pestanya pestanya;

	public InformePestanya() {
	}

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public Informe getInforme() {
		return this.informe;
	}

	public void setInforme(Informe informe) {
		this.informe = informe;
	}

	public Pestanya getPestanya() {
		return this.pestanya;
	}

	public void setPestanya(Pestanya pestanya) {
		this.pestanya = pestanya;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InformePestanya [orden=");
		builder.append(orden);
		builder.append(", pestanya=");
		builder.append(pestanya);
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((informe == null) ? 0 : informe.hashCode());
		result = prime * result + orden;
		result = prime * result
				+ ((pestanya == null) ? 0 : pestanya.hashCode());
		return result;
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
		InformePestanya other = (InformePestanya) obj;
		if (informe == null) {
			if (other.informe != null)
				return false;
		} else if (!informe.equals(other.informe))
			return false;
		if (orden != other.orden)
			return false;
		if (pestanya == null) {
			if (other.pestanya != null)
				return false;
		} else if (!pestanya.equals(other.pestanya))
			return false;
		return true;
	}
	
	
	

}