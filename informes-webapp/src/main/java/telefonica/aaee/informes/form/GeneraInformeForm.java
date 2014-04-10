package telefonica.aaee.informes.form;

import java.io.Serializable;
import java.util.List;

import telefonica.aaee.informes.model.FileInfoDTO;
import telefonica.aaee.informes.model.Informe;
import telefonica.aaee.informes.model.condiciones.Acuerdo;

public class GeneraInformeForm   implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<Informe> informes;
	private List<Acuerdo> acuerdos;

	private Long informeId;
	private Long acuerdoId;

	// URLs
	private List<FileInfoDTO> urls;

	public GeneraInformeForm() {
		super();
	}


	/**
	 * @return the informes
	 */
	public List<Informe> getInformes() {
		return informes;
	}

	/**
	 * @param informes the informes to set
	 */
	public void setInformes(List<Informe> informes) {
		this.informes = informes;
	}

	/**
	 * @return the acuerdos
	 */
	public List<Acuerdo> getAcuerdos() {
		return acuerdos;
	}

	/**
	 * @param acuerdos the acuerdos to set
	 */
	public void setAcuerdos(List<Acuerdo> acuerdos) {
		this.acuerdos = acuerdos;
	}

	/**
	 * @return the informeId
	 */
	public Long getInformeId() {
		return informeId;
	}

	/**
	 * @param informeId the informeId to set
	 */
	public void setInformeId(Long informeId) {
		this.informeId = informeId;
	}

	/**
	 * @return the acuerdoId
	 */
	public Long getAcuerdoId() {
		return acuerdoId;
	}

	/**
	 * @param acuerdoId the acuerdoId to set
	 */
	public void setAcuerdoId(Long acuerdoId) {
		this.acuerdoId = acuerdoId;
	}


	public List<FileInfoDTO> getUrls() {
		return urls;
	}


	public void setUrls(List<FileInfoDTO> urls) {
		this.urls = urls;
	}
	
}