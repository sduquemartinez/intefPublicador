package com.emergya.descartes.job;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.emergya.descartes.content.DescartesContentProxy;
import com.emergya.descartes.content.DescartesZipContentProxy;

// TODO: Auto-generated Javadoc
/**
 * Clase que gestiona el proceso de transformación a HTML5.
 *
 * @author affernandez
 */
public class JobValidator {

	/** The job config. */
	private JobConfiguration jobConfig;

	/** The publish queue ready flag. */
	/* Flags */
	private boolean publishQueueReadyFlag = false;
	
	/** The validate queue ready flag. */
	private boolean validateQueueReadyFlag = false;

	/** The contents to publish. */
	/* Colas de proceso */
	private BlockingQueue<DescartesContentProxy> contentsToPublish;
	
	/** The contents to validate. */
	private BlockingQueue<DescartesContentProxy> contentsToValidate;
	
	/** Poison pill para consumir los threads que usan Blocking Queues con DescartesContentProxy. */
	public static final DescartesContentProxy STOP_QUEUE = new DescartesContentProxy();

	/**
	 * Contenidos que se han publicado con éxito.
	 */
	private BlockingQueue<DescartesZipContentProxy> contentsPublished;

	/** Contenidos que se han validado con éxito. */
	private BlockingQueue<DescartesContentProxy> contentsValidate;

	/**
	 * Contenidos que no se han publicado correctamente.
	 */
	private Queue<DescartesContentProxy> contentsPublishedError;

	/**
	 * Contenidos que no se han validado correctamente.
	 */
	private Queue<DescartesContentProxy> contentsValidateError;

	/**
	 * Instantiates a new job publisher.
	 *
	 * @param config the config
	 */
	public JobValidator(JobConfiguration config) {

		this.setJobConfig(config);

		this.setContentsToPublish(generateArrayBlockingQueue());
		this.setContentsToValidate(generateArrayBlockingQueueValidate());

		this.setContentsPublished(generateArrayBlockingQueueZip());
		this.setContentsValidate(generateArrayBlockingQueueValidate());
		this.setContentsPublishedError(generateArrayBlockingQueue());
		this.setContentsValidateError(generateArrayBlockingQueueValidate());
	}

	/**
	 * Gets the job config.
	 *
	 * @return the jobConfig
	 */
	public JobConfiguration getJobConfig() {
		return jobConfig;
	}

	/**
	 * Sets the job config.
	 *
	 * @param jobConfig            the jobConfig to set
	 */
	public void setJobConfig(JobConfiguration jobConfig) {
		this.jobConfig = jobConfig;
	}

	/**
	 * Generate array blocking queue.
	 *
	 * @return the blocking queue
	 */
	private BlockingQueue<DescartesContentProxy> generateArrayBlockingQueue() {
		return new ArrayBlockingQueue<DescartesContentProxy>(
				this.jobConfig.getSizeArrayBlockingqueue(), true);
	}
	
	
	/**
	 * Generate array blocking queue.
	 *
	 * @return the blocking queue
	 */
	private BlockingQueue<DescartesZipContentProxy> generateArrayBlockingQueueZip() {
		return new ArrayBlockingQueue<DescartesZipContentProxy>(
				this.jobConfig.getSizeArrayBlockingqueue(), true);
	}
	
	/**
	 * Generate array blocking queue validate.
	 *
	 * @return the blocking queuen
	 */
	private BlockingQueue<DescartesContentProxy> generateArrayBlockingQueueValidate() {
		return new ArrayBlockingQueue<DescartesContentProxy>(
				this.jobConfig.getSizeArrayBlockingqueue(), true);
	}

	/**
	 * Checks if is convert queue ready flag.
	 *
	 * @return the convertQueueReadyFlag
	 */
	public boolean isPublishQueueReadyFlag() {
		return publishQueueReadyFlag;
	}

	/**
	 * Sets the convert queue ready flag.
	 *
	 * @param convertQueueReadyFlag            the convertQueueReadyFlag to set
	 */
	public void setPublishQueueReadyFlag(boolean publishQueueReadyFlag) {
		this.publishQueueReadyFlag = publishQueueReadyFlag;
	}

	/**
	 * Checks if is validate queue ready flag.
	 *
	 * @return the validateQueueReadyFlag
	 */
	public boolean isValidateQueueReadyFlag() {
		return validateQueueReadyFlag;
	}

	/**
	 * Sets the validate queue ready flag.
	 *
	 * @param validateQueueReadyFlag            the validateQueueReadyFlag to set
	 */
	public void setValidateQueueReadyFlag(boolean validateQueueReadyFlag) {
		this.validateQueueReadyFlag = validateQueueReadyFlag;
	}

	
	/**
	 * Gets the contents to publish.
	 *
	 * @return the contentsToConvert
	 */
	public BlockingQueue<DescartesContentProxy> getContentsToPublish() {
		return contentsToPublish;
	}

	/**
	 * Sets the contents to publish.
	 *
	 * @param contentsToPublish the new contents to publish
	 */
	public void setContentsToPublish(
			BlockingQueue<DescartesContentProxy> contentsToPublish) {
		this.contentsToPublish = contentsToPublish;
	}

	/**
	 * Gets the contents to validate.
	 *
	 * @return the contentsToValidate
	 */
	public BlockingQueue<DescartesContentProxy> getContentsToValidate() {
		return contentsToValidate;
	}

	/**
	 * Sets the contents to validate.
	 *
	 * @param contentsToValidate            the contentsToValidate to set
	 */
	public void setContentsToValidate(
			BlockingQueue<DescartesContentProxy> contentsToValidate) {
		this.contentsToValidate = contentsToValidate;
	}

	/**
	 * Gets the contents published.
	 *
	 * @return the contentsConverted
	 */
	public BlockingQueue<DescartesZipContentProxy> getContentsPublished() {
		return contentsPublished;
	}

	/**
	 * Sets the contents published.
	 *
	 * @param contentsPublished the new contents published
	 */
	public void setContentsPublished(
			BlockingQueue<DescartesZipContentProxy> contentsPublished) {
		this.contentsPublished = contentsPublished;
	}

	/**
	 * Gets the contents validate.
	 *
	 * @return the contentsValidate
	 */
	public BlockingQueue<DescartesContentProxy> getContentsValidate() {
		return contentsValidate;
	}

	/**
	 * Sets the contents validate.
	 *
	 * @param contentsValidate            the contentsValidate to set
	 */
	public void setContentsValidate(
			BlockingQueue<DescartesContentProxy> contentsValidate) {
		this.contentsValidate = contentsValidate;
	}

	/**
	 * Gets the contents published error.
	 *
	 * @return the contentsConvertedError
	 */
	public Queue<DescartesContentProxy> getContentsPublishedError() {
		return contentsPublishedError;
	}

	/**
	 * Sets the contents published error.
	 *
	 * @param contentsPublishedError the new contents published error
	 */
	public void setContentsPublishedError(
			Queue<DescartesContentProxy> contentsPublishedError) {
		this.contentsPublishedError = contentsPublishedError;
	}

	/**
	 * Gets the contents validate error.
	 *
	 * @return the contentsValidateError
	 */
	public Queue<DescartesContentProxy> getContentsValidateError() {
		return contentsValidateError;
	}

	/**
	 * Sets the contents validate error.
	 *
	 * @param contentsValidateError            the contentsValidateError to set
	 */
	public void setContentsValidateError(
			Queue<DescartesContentProxy> contentsValidateError) {
		this.contentsValidateError = contentsValidateError;
	}
}
