package telefonica.aaee.informes.model;

import java.io.Serializable;

public class FileInfoDTO implements Serializable{
	
	private static final long serialVersionUID = 7941962583695729804L;

	private String fileName;
	private String fileNameAbsolute;
	private long fileSize;
	private long fileLines;
	private String url;
	
	
	
	
	
	/**
	 * Getters & Setters
	 */

	
	
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the fileNameAbsolute
	 */
	public String getFileNameAbsolute() {
		return fileNameAbsolute;
	}
	/**
	 * @param fileNameAbsolute the fileNameAbsolute to set
	 */
	public void setFileNameAbsolute(String fileNameAbsolute) {
		this.fileNameAbsolute = fileNameAbsolute;
	}
	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return the fileLines
	 */
	public long getFileLines() {
		return fileLines;
	}
	/**
	 * @param fileLines the fileLines to set
	 */
	public void setFileLines(long fileLines) {
		this.fileLines = fileLines;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	
	
	

}
