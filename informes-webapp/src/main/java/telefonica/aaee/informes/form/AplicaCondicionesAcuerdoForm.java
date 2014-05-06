package telefonica.aaee.informes.form;

import java.io.Serializable;
import java.util.List;

import telefonica.aaee.informes.model.FileInfoDTO;
import telefonica.aaee.informes.model.condiciones.Acuerdo;

public class AplicaCondicionesAcuerdoForm   implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<Acuerdo> acuerdos;

	private Long acuerdoId;

	// URLs
	private List<FileInfoDTO> urls;

	public AplicaCondicionesAcuerdoForm() {
		super();
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