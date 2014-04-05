package telefonica.aaee.informes.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import telefonica.aaee.informes.model.FileInfoDTO;

public class MUploadForm {

	// Ficheros a subir
	private List<MultipartFile> files;
	
	// Ficheros subidos
	private List<FileInfoDTO> uploadedFiles;
	
	// URLs
	private List<String> urls;
	
	
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

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

}
