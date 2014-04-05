package telefonica.aaee.informes.model.condiciones;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the ttrafico database table.
 * 
 */
@Entity
@Table(name="ttrafico")
@NamedQueries({
	@NamedQuery(name = "FindByAcuerdoAmbitoDeTrafico"
			, query = "SELECT c "
					+ "FROM Trafico c " 
						+ "WHERE "
							+ "1 = 1 "
							+ "and c.acuerdo = :ac "
							+ "and c.ambitoDeTrafico = :at ")
})
@XmlRootElement
public class Trafico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String acuerdo;

	@Column(name="AMBITO_DE_TRAFICO")
	private String ambitoDeTrafico;

	private String cif;

	@Column(name="DESC_AMBITO_DE_TRAFICO")
	private String descAmbitoDeTrafico;

	@Column(name="EST_LLAMADA")
	private double estLlamada;

	@Column(name="PORCENTAJE_DESCUENTO")
	private double porcentajeDescuento;

	@Column(name="PORCENTAJE_ONNET")
	private double porcentajeOnnet;

	@Column(name="PRECIO_ESPECIAL")
	private String precioEspecial;

	@Column(name="PRECIO_POR_MINUTO")
	private double precioPorMinuto;

	@Column(name="TIPO_DESCUENTO")
	private String tipoDescuento;
	
	@Column(name="ini_periodo")
	private String iniPeriodo;

	@Column(name="fin_periodo")
	private String finPeriodo;

	

    
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

	public Trafico() {
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

	public String getAmbitoDeTrafico() {
		return this.ambitoDeTrafico;
	}

	public void setAmbitoDeTrafico(String ambitoDeTrafico) {
		this.ambitoDeTrafico = ambitoDeTrafico;
	}

	public String getCif() {
		return this.cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getDescAmbitoDeTrafico() {
		return this.descAmbitoDeTrafico;
	}

	public void setDescAmbitoDeTrafico(String descAmbitoDeTrafico) {
		this.descAmbitoDeTrafico = descAmbitoDeTrafico;
	}

	public double getEstLlamada() {
		return this.estLlamada;
	}

	public void setEstLlamada(double estLlamada) {
		this.estLlamada = estLlamada;
	}

	public double getPorcentajeDescuento() {
		return this.porcentajeDescuento;
	}

	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	public String getPrecioEspecial() {
		return this.precioEspecial;
	}

	public void setPrecioEspecial(String precioEspecial) {
		this.precioEspecial = precioEspecial;
	}

	public double getPrecioPorMinuto() {
		return this.precioPorMinuto;
	}

	public void setPrecioPorMinuto(double precioPorMinuto) {
		this.precioPorMinuto = precioPorMinuto;
	}

	public String getTipoDescuento() {
		return this.tipoDescuento;
	}

	public void setTipoDescuento(String tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}

	public double getPorcentajeOnnet() {
		return porcentajeOnnet;
	}

	public void setPorcentajeOnnet(double porcentajeOnnet) {
		this.porcentajeOnnet = porcentajeOnnet;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuerdo == null) ? 0 : acuerdo.hashCode());
		result = prime * result
				+ ((ambitoDeTrafico == null) ? 0 : ambitoDeTrafico.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime
				* result
				+ ((descAmbitoDeTrafico == null) ? 0 : descAmbitoDeTrafico
						.hashCode());
		long temp;
		temp = Double.doubleToLongBits(estLlamada);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((finPeriodo == null) ? 0 : finPeriodo.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((iniPeriodo == null) ? 0 : iniPeriodo.hashCode());
		temp = Double.doubleToLongBits(porcentajeDescuento);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(porcentajeOnnet);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((precioEspecial == null) ? 0 : precioEspecial.hashCode());
		temp = Double.doubleToLongBits(precioPorMinuto);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((tipoDescuento == null) ? 0 : tipoDescuento.hashCode());
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
		Trafico other = (Trafico) obj;
		if (acuerdo == null) {
			if (other.acuerdo != null)
				return false;
		} else if (!acuerdo.equals(other.acuerdo))
			return false;
		if (ambitoDeTrafico == null) {
			if (other.ambitoDeTrafico != null)
				return false;
		} else if (!ambitoDeTrafico.equals(other.ambitoDeTrafico))
			return false;
		if (cif == null) {
			if (other.cif != null)
				return false;
		} else if (!cif.equals(other.cif))
			return false;
		if (descAmbitoDeTrafico == null) {
			if (other.descAmbitoDeTrafico != null)
				return false;
		} else if (!descAmbitoDeTrafico.equals(other.descAmbitoDeTrafico))
			return false;
		if (Double.doubleToLongBits(estLlamada) != Double
				.doubleToLongBits(other.estLlamada))
			return false;
		if (finPeriodo == null) {
			if (other.finPeriodo != null)
				return false;
		} else if (!finPeriodo.equals(other.finPeriodo))
			return false;
		if (id != other.id)
			return false;
		if (iniPeriodo == null) {
			if (other.iniPeriodo != null)
				return false;
		} else if (!iniPeriodo.equals(other.iniPeriodo))
			return false;
		if (Double.doubleToLongBits(porcentajeDescuento) != Double
				.doubleToLongBits(other.porcentajeDescuento))
			return false;
		if (Double.doubleToLongBits(porcentajeOnnet) != Double
				.doubleToLongBits(other.porcentajeOnnet))
			return false;
		if (precioEspecial == null) {
			if (other.precioEspecial != null)
				return false;
		} else if (!precioEspecial.equals(other.precioEspecial))
			return false;
		if (Double.doubleToLongBits(precioPorMinuto) != Double
				.doubleToLongBits(other.precioPorMinuto))
			return false;
		if (tipoDescuento == null) {
			if (other.tipoDescuento != null)
				return false;
		} else if (!tipoDescuento.equals(other.tipoDescuento))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Trafico [id=");
		builder.append(id);
		builder.append(", acuerdo=");
		builder.append(acuerdo);
		builder.append(", ambitoDeTrafico=");
		builder.append(ambitoDeTrafico);
		builder.append(", cif=");
		builder.append(cif);
		builder.append(", descAmbitoDeTrafico=");
		builder.append(descAmbitoDeTrafico);
		builder.append(", estLlamada=");
		builder.append(estLlamada);
		builder.append(", porcentajeDescuento=");
		builder.append(porcentajeDescuento);
		builder.append(", porcentajeOnnet=");
		builder.append(porcentajeOnnet);
		builder.append(", precioEspecial=");
		builder.append(precioEspecial);
		builder.append(", precioPorMinuto=");
		builder.append(precioPorMinuto);
		builder.append(", tipoDescuento=");
		builder.append(tipoDescuento);
		builder.append(", iniPeriodo=");
		builder.append(iniPeriodo);
		builder.append(", finPeriodo=");
		builder.append(finPeriodo);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}