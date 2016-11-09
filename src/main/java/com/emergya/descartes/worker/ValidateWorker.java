package com.emergya.descartes.worker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.emergya.descartes.content.DescartesContentProxy;
import com.emergya.descartes.content.ValidatedContent;
import com.emergya.descartes.job.JobValidator;
import com.emergya.descartes.services.OdeValidate;
import com.emergya.descartes.utils.ConverterOutputManager;

import es.pode.validador.negocio.servicio.ValidaVO;

/**
 * 
 * @author affernandez
 * @param <T>
 *
 */
public class ValidateWorker<T> extends BaseWorker implements Runnable {

	private static Logger log = Logger.getLogger(ValidateWorker.class);

	/**
	 * @param job
	 */
	public ValidateWorker(JobValidator job) {
		super(job);
	}

	/**
	 * Realiza el proceso de validación de los html de los contenidos por la W3C
	 */
	@Override
	public void run() {
		JobValidator currentJob = getJob();

		DescartesContentProxy contentToValidate = new DescartesContentProxy();
		while (!currentJob.isValidateQueueReadyFlag()
				|| !currentJob.getContentsToValidate().isEmpty()) {
			try {
				contentToValidate = currentJob.getContentsToValidate().take();

				if (contentToValidate != JobValidator.STOP_QUEUE) {
					if (contentToValidate != null) {
						doWork(contentToValidate, currentJob);
						// Info estadística
						currentJob.getContentsValidate().add(contentToValidate);
					}
				} else {

					currentJob.getContentsToPublish().put(
							JobValidator.STOP_QUEUE);

					currentJob.setValidateQueueReadyFlag(true);
					currentJob.setPublishQueueReadyFlag(true);

					log.info("-->> Total Validados: "
							+ (currentJob.getContentsValidate().size()));
					log.info("****VALIDACIÓN FINALIZADA****");
				}
			} catch (InterruptedException e1) {
				log.error("Interrupción de la cola de validación", e1);
			} catch (Exception e) {
				currentJob.getContentsValidateError().add(contentToValidate);
				log.error(
						"No se ha podido realizar la validación del contenido: "
								+ contentToValidate.getTitle() + ": ", e);
			}
		}

		log.info("Contenidos que han provocado errores en la validación: "
				+ currentJob.getContentsValidateError().size());
	}

	/**
	 * Método que realiza el trabajo de validación
	 * 
	 * @throws IOException
	 */
	private void doWork(DescartesContentProxy contentToValidate,
			JobValidator job) throws IOException {

		// TODO: Comprobar el manifest y si todo OK comprimir en zip

		OdeValidate validateService = new OdeValidate();
		Path pathManifest = Paths.get(contentToValidate.getLocalCopy()
				+ File.separator + contentToValidate.getManifestFile());

		ValidaVO valida = validateService.manifestValidator(pathManifest, job);
		if (valida != null && valida.getEsValidoManifest() != null) {
			boolean isValidate = valida.getEsValidoManifest();
			if (!isValidate) {
				/* Dentro de la cola de error de validación */
				job.getContentsValidateError().add(contentToValidate);
				String[] reaName = contentToValidate.getLocalCopy().toString()
						.split(File.separator);
				/* Registramos errores */
				File errorValidationLogFile = new File(job.getJobConfig()
						.getOriginalContentPath()
						+ File.separator
						+ "validation_" + reaName[reaName.length - 1] + ".csv");

				ValidatedContent<?> validatedContent = new ValidatedContent<T>();
				validatedContent.setLogFile(errorValidationLogFile);
				ConverterOutputManager.createLogValidatedContentCSV(
						validatedContent, valida);
			} else {

				try {
					job.getContentsToPublish().put(contentToValidate);
				} catch (InterruptedException e) {
					log.error("Error al almacenar el contenido para su publicación");
				}
				
				log.info(">> Validación realizada para el contenido: "
						+ contentToValidate.getLocalCopy() + File.separator
						+ contentToValidate.getManifestFile());
			}

		}
	}
}