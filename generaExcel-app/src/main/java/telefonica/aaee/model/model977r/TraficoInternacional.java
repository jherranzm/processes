package telefonica.aaee.model.model977r;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the ttrafico_internacional database table.
 * 
 */
@Entity
@Table(name="ttrafico_internacional")
@XmlRootElement
public class TraficoInternacional 

	implements Serializable {
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

	private String destino;

	@Column(name="EST_LLAMADA")
	private double estLlamada;

	@Column(name="PAIS_DESTINO")
	private String paisDestino;

	@Column(name="PORCENTAJE_DESCUENTO")
	private double porcentajeDescuento;

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

	public TraficoInternacional() {
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

	public String getDestino() {
		return this.destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public double getEstLlamada() {
		return this.estLlamada;
	}

	public void setEstLlamada(double estLlamada) {
		this.estLlamada = estLlamada;
	}

	public String getPaisDestino() {
		return this.paisDestino;
	}

	public void setPaisDestino(String paisDestino) {
		this.paisDestino = paisDestino;
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

	@Override
	public String toString() {
		return "TraficoInternacional [id=" + id + ",\\n acuerdo=" + acuerdo
				+ ",\\n ambitoDeTrafico=" + ambitoDeTrafico + ",\\n cif=" + cif
				+ ",\\n descAmbitoDeTrafico=" + descAmbitoDeTrafico
				+ ",\\n destino=" + destino + ",\\n estLlamada=" + estLlamada
				+ ",\\n paisDestino=" + paisDestino
				+ ",\\n porcentajeDescuento=" + porcentajeDescuento
				+ ",\\n precioEspecial=" + precioEspecial
				+ ",\\n precioPorMinuto=" + precioPorMinuto
				+ ",\\n tipoDescuento=" + tipoDescuento + "]";
	}
	
	
	

}