package com.emergya.descartes.worker;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.emergya.descartes.content.DescartesContentProxy;
import com.emergya.descartes.content.DescartesZipContentProxy;
import com.emergya.descartes.job.JobValidator;
import com.emergya.descartes.job.SearchDescartesContents;
import com.emergya.descartes.job.SearchZipDescartesContents;
import com.emergya.descartes.utils.FileManager;

/**
 * 
 * Prepara el entorno de trabajo para realizar el proceso de conversión.
 *   
 * @author fbaena
 *
 */
public class InitWorker extends BaseWorker implements Runnable {

    private static Logger log = Logger.getLogger(InitWorker.class);

    /**
     * @param job
     * @param checkContents 
     */
    public InitWorker(JobValidator job) {
        super(job);
    }

    /**
    * Realiza el proceso de carga de contenidos e inicia los hilos de análisis, conversión y validación
    *  
    * @author fbaena
    */
    @Override
    public void run() {

        new Thread(new ValidateWorker(getJob())).start();
        new Thread(new PublishWorker(getJob())).start();
        
//		SearchDescartesContents descartesContents = new SearchDescartesContents(
//				getJob().getJobConfig().getOriginalContentPath());
//		doWorkFromFolders(getJob(), descartesContents);
		
		SearchZipDescartesContents descartesContents = new SearchZipDescartesContents(
				getJob().getJobConfig().getOriginalContentPath());
		doWorkFromZip(getJob(), descartesContents);
//        if (getJob().getJobConfig().isZipSource()) {
//
//            // Control de existencia de carpeta de trabajo
//            File resultPath = new File(getJob().getJobConfig()
//                    .getWorkingContentPath());
//
//            if (resultPath.exists()) {
//                FileManager.deleteFilesInFolder(resultPath);
//            }
//
//            resultPath.mkdirs();
//
//            SearchZipDescartesContents zipDescartesContents = new SearchZipDescartesContents(
//                    getJob().getJobConfig().getOriginalContentPath());
//
//            doWorkFromZip(getJob(), zipDescartesContents, isToConvert);
//
//        } else {
//            SearchDescartesContents descartesContents = new SearchDescartesContents(
//                    getJob().getJobConfig().getOriginalContentPath());
//            doWorkFromFolders(getJob(), descartesContents, isToConvert);
//        }

    }

    /**
     * Método que inicializa la cola de validaciones de contenidos
     * @param ZipFilesNames
     * @param job
     */
//    private void doWorkFromFolders(JobValidator job,
//            SearchDescartesContents descartesContents) {
//        ArrayList<DescartesContentProxy> listaContenidos = (ArrayList<DescartesContentProxy>) descartesContents
//                .getDescartesContentProxyListNames();
//        try {
//                for (DescartesContentProxy contentFolder : listaContenidos) {
//                    job.getContentsToValidate().put(contentFolder);
//                }
//            log.info("-->> Número Total de Contenidos: "
//                    + (listaContenidos.size()));
//            // Añadimos a la cola la poison pill para consumir el thread cuando
//            // llegue a ella
//            job.getContentsToValidate().put(JobValidator.STOP_QUEUE);
//        } catch (InterruptedException e) {
//            log.error("Error de Interrupción. " + e, e);
//            try {
//            	job.getContentsToValidate().put(JobValidator.STOP_QUEUE);
//            } catch (InterruptedException e1) {
//                log.error("Error de Interrupción. " + e, e);
//            }
//        }
//    }

    /**
     * Método que inicializa la cola de análisis de contenidos
     * @param ZipFilesNames
     * @param job
     */
    private void doWorkFromZip(JobValidator job,
            SearchZipDescartesContents zipDescartesContents) {
        ArrayList<DescartesZipContentProxy> listaContenidos = (ArrayList<DescartesZipContentProxy>) zipDescartesContents
                .getDescartesZipContentProxyListNames();
        try {
                for (DescartesZipContentProxy zipFile : listaContenidos) {
                    File contentFolder = FileManager.extractZipContents(
                            zipFile, job);
                    DescartesContentProxy contentProxy = new DescartesContentProxy();
                    contentProxy.setLocalCopy(contentFolder);
                    job.getContentsToValidate().put(contentProxy);
                }
            log.info("-->> Número Total de Contenidos: "
                    + (listaContenidos.size()));
            // Añadimos a la cola la poison pill para consumir el thread cuando
            // llegue a ella
            job.getContentsToValidate().put(JobValidator.STOP_QUEUE);
        } catch (InterruptedException e) {
            log.error("Error de Interrupción. " + e, e);
            try {
            	 job.getContentsToValidate().put(JobValidator.STOP_QUEUE);
            } catch (InterruptedException e1) {
                log.error("Error de Interrupción. " + e, e);
            }
        }
    }
}
