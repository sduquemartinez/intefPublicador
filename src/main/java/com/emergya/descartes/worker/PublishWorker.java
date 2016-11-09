package com.emergya.descartes.worker;

import java.io.File;
import java.io.IOException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.emergya.descartes.content.DescartesContentProxy;
import com.emergya.descartes.content.DescartesZipContentProxy;
import com.emergya.descartes.job.JobValidator;
import com.emergya.descartes.main.Main;
import com.emergya.descartes.services.OdePublisher;
import com.emergya.descartes.utils.FileManager;

/**
 * 
 * @author affernandez
 *
 */
public class PublishWorker extends BaseWorker implements Runnable {

	private static Logger log = Logger.getLogger(PublishWorker.class);

	/**
	 * @param job
	 */
	public PublishWorker(JobValidator job) {
		super(job);
	}

	/**
	 * Realiza el proceso de validación de los html de los contenidos por la W3C
	 */
	@Override
	public void run() {
		JobValidator currentJob = getJob();
		log.info("****INICIO DE LA PUBLICACIÓN DE RECURSOS****");
		DescartesContentProxy contentToPublish = new DescartesContentProxy();
		DescartesZipContentProxy contentPublished = null;
		while (!currentJob.isPublishQueueReadyFlag()
				|| !currentJob.getContentsToPublish().isEmpty()) {
			try {
				contentToPublish = currentJob.getContentsToPublish().take();

				if (contentToPublish != JobValidator.STOP_QUEUE) {
					if (contentToPublish != null) {
						contentPublished = new DescartesZipContentProxy();
						contentPublished = doWork(contentToPublish, currentJob);

						if (contentPublished != null) {
							currentJob.getContentsPublished().put(
									contentPublished);

							// Movemos al directorio de recursos publicados
							FileManager.copy(contentPublished.getFileName()
									.toString(), currentJob.getJobConfig()
									.getPublisherContentPath(),
									contentPublished.getFileName().toFile()
											.getName());
							
							//Borramos fichero original
							FileManager.deleteFilesInFolder(contentPublished.getFileName().toFile());
						} else {
							currentJob.getContentsPublishedError().add(
									contentToPublish);
						}
					}
				} else {
					log.info("-->> Total publicados: "
							+ (currentJob.getContentsPublished().size()));
					log.info("****PUBLICACIÓN FINALIZADA****");
					currentJob.setPublishQueueReadyFlag(true);
					
					// Eliminamos el contenido del directorio working
					FileManager.deleteFilesInFolder(new File(currentJob.getJobConfig()
							.getWorkingPath()));
					
					if(Main.recursosPdte()){
						log.debug("\n ####################### \n LOTE FINALIZADO, REINTENTO \n ####################### \n");
						Main.execute();
					}
				}
			} catch (InterruptedException e1) {
				log.error("Interrupción de la cola de punlicación", e1);
			} catch (IOException e) {
				currentJob.getContentsPublishedError().add(contentToPublish);
				log.error(
						"Se ha producido un error al formar el fichero ZIP o al publicarlo: "
								+ contentToPublish.getLocalCopy() + ": ", e);

			} catch (ServiceException e) {
				currentJob.getContentsPublishedError().add(contentToPublish);
				log.error(
						"Se ha producido un error en el manejo o conexión de los servicios de publicación de PROCOMUN: "
								+ contentToPublish.getLocalCopy() + ": ", e);
			}
		}

		log.info("Contenidos que han provocado errores en la publicación: "
				+ currentJob.getContentsValidateError().size());

		
	}

	/**
	 * Método que realiza el trabajo de validación
	 * 
	 * @throws IOException
	 * @throws ServiceException
	 */
	private DescartesZipContentProxy doWork(
			DescartesContentProxy contentToPublish, JobValidator job)
			throws ServiceException, IOException {

		OdePublisher publishService = new OdePublisher();
		File zipfile = publishService.publish(contentToPublish, job);
		DescartesZipContentProxy zipFileProxy = null;
		if (zipfile != null) {
			log.info(">> Publicación realizada para el contenido: "
					+ contentToPublish.getLocalCopy());

			zipFileProxy = new DescartesZipContentProxy(zipfile.toPath());
		}

		return zipFileProxy;
	}
}