package com.emergya.descartes.content;

import java.io.File;

import com.emergya.descartes.utils.Constants;

/**
 * The Class DescartesContentProxy.
 */
public class DescartesContentProxy {
	private File logFile;
	
	/**
	 * Identificador único del contenido
	 */
	private String id;
	/**
	 * Titulo del contenido.
	 */
	private String title;

	/**
	 * Manifest file
	 */
	private String manifestFile;
	/**
	 * Tipo de contenido
	 */
	private String contentType;
	/**
	 * Referencia a la copia local del metadato.
	 */
	private File localCopy;

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the localCopy
	 */
	public File getLocalCopy() {
		return localCopy;
	}

	/**
	 * @param localCopy
	 *            the localCopy to set
	 */
	public void setLocalCopy(File localCopy) {
		this.localCopy = localCopy;
	}

	public String getManifestFile() {
		return Constants.FILE_MANIFEST;
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}



}
