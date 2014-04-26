package telefonica.aaee.informes.model.condiciones;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the ttraficori database table.
 * 
 */
@Entity
@Table(name = "ttraficori")
@NamedQueries({ 
	@NamedQuery(
			name = "FindByAcuerdoAmbitoDeTrafico", 
			query = "SELECT c "
		+ "FROM Trafico c "
		+ "WHERE "
		+ "1 = 1 "
		+ "and c.acuerdo = :ac "
		+ "and c.ambitoDeTrafico = :at ") })
@XmlRootElement
public class TraficoRI implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String acuerdo;
	
	private Long idAcuerdo;

	@Column(name = "AMBITO_DE_TRAFICO")
	private String ambitoDeTrafico;

	private String cif;

	@Column(name = "DESC_AMBITO_DE_TRAFICO")
	private String descAmbitoDeTrafico;

	@Column(name = "EST_LLAMADA")
	private double estLlamada;

	@Column(name = "PORCENTAJE_DESCUENTO")
	private double porcentajeDescuento;

	@Column(name = "PRECIO_ESPECIAL")
	private String precioEspecial;

	@Column(name = "PRECIO_POR_MINUTO")
	private double precioPorMinuto;

	@Column(name = "TIPO_DESCUENTO")
	private String tipoDescuento;

	@Column(name = "ini_periodo")
	private String iniPeriodo;

	@Column(name = "fin_periodo")
	private String finPeriodo;

	

	public TraficoRI() {
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



	public String getAmbitoDeTrafico() {
		return ambitoDeTrafico;
	}



	public void setAmbitoDeTrafico(String ambitoDeTrafico) {
		this.ambitoDeTrafico = ambitoDeTrafico;
	}



	public String getCif() {
		return cif;
	}



	public void setCif(String cif) {
		this.cif = cif;
	}



	public String getDescAmbitoDeTrafico() {
		return descAmbitoDeTrafico;
	}



	public void setDescAmbitoDeTrafico(String descAmbitoDeTrafico) {
		this.descAmbitoDeTrafico = descAmbitoDeTrafico;
	}



	public double getEstLlamada() {
		return estLlamada;
	}



	public void setEstLlamada(double estLlamada) {
		this.estLlamada = estLlamada;
	}



	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}



	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}



	public String getPrecioEspecial() {
		return precioEspecial;
	}



	public void setPrecioEspecial(String precioEspecial) {
		this.precioEspecial = precioEspecial;
	}



	public double getPrecioPorMinuto() {
		return precioPorMinuto;
	}



	public void setPrecioPorMinuto(double precioPorMinuto) {
		this.precioPorMinuto = precioPorMinuto;
	}



	public String getTipoDescuento() {
		return tipoDescuento;
	}



	public void setTipoDescuento(String tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}



	public String getIniPeriodo() {
		return iniPeriodo;
	}



	public void setIniPeriodo(String iniPeriodo) {
		this.iniPeriodo = iniPeriodo;
	}



	public String getFinPeriodo() {
		return finPeriodo;
	}



	public void setFinPeriodo(String finPeriodo) {
		this.finPeriodo = finPeriodo;
	}



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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idAcuerdo == null) ? 0 : idAcuerdo.hashCode());
		result = prime * result
				+ ((iniPeriodo == null) ? 0 : iniPeriodo.hashCode());
		temp = Double.doubleToLongBits(porcentajeDescuento);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((precioEspecial == null) ? 0 : precioEspecial.hashCode());
		temp = Double.doubleToLongBits(precioPorMinuto);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((tipoDescuento == null) ? 0 : tipoDescuento.hashCode());
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
		TraficoRI other = (TraficoRI) obj;
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
		if (iniPeriodo == null) {
			if (other.iniPeriodo != null)
				return false;
		} else if (!iniPeriodo.equals(other.iniPeriodo))
			return false;
		if (Double.doubleToLongBits(porcentajeDescuento) != Double
				.doubleToLongBits(other.porcentajeDescuento))
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



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TraficoRI [id=");
		builder.append(id);
		builder.append(", idAcuerdo=");
		builder.append(idAcuerdo);
		builder.append(", acuerdo=");
		builder.append(acuerdo);
		builder.append(", cif=");
		builder.append(cif);
		builder.append(", ambitoDeTrafico=");
		builder.append(ambitoDeTrafico);
		builder.append(", descAmbitoDeTrafico=");
		builder.append(descAmbitoDeTrafico);
		builder.append(", estLlamada=");
		builder.append(estLlamada);
		builder.append(", porcentajeDescuento=");
		builder.append(porcentajeDescuento);
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