package com.emergya.descartes.job;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.emergya.descartes.utils.Constants;

// TODO: Auto-generated Javadoc
/**
 * Clase que gestiona la configuracion de un trabajo de conversión.
 *
 * @author affernandez
 */
public class JobConfiguration {

    /** The log. */
    private static Logger log = Logger.getLogger(JobConfiguration.class);

    /** The common properties. */
    private Map<String, String> commonProperties;

    /** The size array blockingqueue. */
    private int sizeArrayBlockingqueue;

    /**
     * Gets the size array blockingqueue.
     *
     * @return int
     */
    public int getSizeArrayBlockingqueue() {
        return sizeArrayBlockingqueue;
    }

    /**
     * Sets the size array blockingqueue.
     *
     * @param sizeArrayBlockingqueue the new size array blockingqueue
     */
    private void setSizeArrayBlockingqueue(int sizeArrayBlockingqueue) {
        this.sizeArrayBlockingqueue = sizeArrayBlockingqueue;
    }

    /**
     * Instantiates a new job configuration.
     *
     * @param commonPropertiesFile the common properties file
     * @throws IllegalArgumentException Cuando la configuración es inválida
     */
    public JobConfiguration(String commonPropertiesFile)
            throws IllegalArgumentException {
        this.setCommonProperties(this.getProperties(commonPropertiesFile));
        this.setSizeArrayBlockingqueue(Integer.parseInt(this
                .getCommonProperties().get(Constants.SIZE_ARRAY_BLOCKINGQUEUE)));
    }

    /**
     * Gets the properties.
     *
     * @param PropertiesFile the properties file
     * @return  Map<String, String>
     */
    private Map<String, String> getProperties(String PropertiesFile) {
        Properties props = new Properties();
        try {

            FileInputStream fileIS = new FileInputStream(PropertiesFile);
            props.load(fileIS);

        } catch (FileNotFoundException e) {
            log.error("Fichero no encontrado. " + e);
        } catch (IOException e) {
            log.error("Fichero no accesible. " + e);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        Map<String, String> properties = new HashMap<String, String>(
                (Map) props);
        return properties;
    }


    /**
     * Gets the original content path.
     *
     * @return String
     */
    public String getOriginalContentPath() {
        String originalContentPath = getCommonProperties().get(
                Constants.SOURCE_PATH);
        return originalContentPath;
    }
    
    
    /**
     * Gets the working path.
     *
     * @return String
     */
    public String getWorkingPath() {
        String workingPath = getCommonProperties().get(
                Constants.WORKING_PATH);
        return workingPath;
    }

    /**
     * get tittle of REA from manifest.
     *
     * @return the tittle manifest
     */
    public String getTittleManifest(){
    	String tittleManifest = getCommonProperties().get(Constants.MANIFEST_TITTLE);
    	return tittleManifest;
    }
    
    /**
     * Gets the converted content path.
     *
     * @return String
     */
    public String getPublisherContentPath() {
        String publishedContentPath = getCommonProperties().get(
                Constants.PUBLISHED_PATH);
        return publishedContentPath;
    }
    
    /**
     * Gets the publicator url service.
     *
     * @return the publicator url service
     */
    public String getPublicatorUrlService() {
        String publicatorUrlService = getCommonProperties().get(
                Constants.PUBLICATOR_SERVICE);
        return publicatorUrlService;
    }
    
    /**
     * Gets the validator url service.
     *
     * @return the validator url service
     */
    public String getValidatorUrlService() {
        String validatorUrlService = getCommonProperties().get(
                Constants.VALIDATOR_SERVICE);
        return validatorUrlService;
    }

    /**
     * Gets the common properties.
     *
     * @return the commonProperties
     */
    public Map<String, String> getCommonProperties() {
        return commonProperties;
    }

    /**
     * Sets the common properties.
     *
     * @param commonProperties the commonProperties to set
     */
    public void setCommonProperties(Map<String, String> commonProperties) {
        this.commonProperties = commonProperties;
    }
}
