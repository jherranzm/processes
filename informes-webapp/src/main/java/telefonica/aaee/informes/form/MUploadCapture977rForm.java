package telefonica.aaee.informes.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import telefonica.aaee.informes.model.FileInfoDTO;

public class MUploadCapture977rForm {

	// Ficheros a subir
	private List<MultipartFile> files;
	
	// Ficheros subidos
	private List<FileInfoDTO> uploadedFiles;
	
	// URLs
	private List<FileInfoDTO> urls;
	
	//acuerdo
	private String acuerdo;
	
	private boolean detalleLlamadas = false;
	private boolean detalleLlamadasRI = false;
	
	
	
	/**
	 * 
	 * Getters & Setters
	 * 
	 * @return
	 */
	

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public List<FileInfoDTO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<FileInfoDTO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<FileInfoDTO> getUrls() {
		return urls;
	}

	public void setUrls(List<FileInfoDTO> urls) {
		this.urls = urls;
	}

	public String getAcuerdo() {
		return acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}

	public boolean isDetalleLlamadas() {
		return detalleLlamadas;
	}

	public void setDetalleLlamadas(boolean detalleLlamadas) {
		this.detalleLlamadas = detalleLlamadas;
	}

	public boolean isDetalleLlamadasRI() {
		return detalleLlamadasRI;
	}

	public void setDetalleLlamadasRI(boolean detalleLlamadasRI) {
		this.detalleLlamadasRI = detalleLlamadasRI;
	}

}
