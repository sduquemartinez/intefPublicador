package com.emergya.descartes.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.apache.log4j.PropertyConfigurator;

import com.emergya.descartes.job.BaseSearchDescartesContents;
import com.emergya.descartes.job.JobConfiguration;
import com.emergya.descartes.job.JobValidator;
import com.emergya.descartes.utils.Constants;
import com.emergya.descartes.worker.InitWorker;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		  try {
	            if (args.length != 1) {
	                throw new Exception();
	            } else {
	                PropertyConfigurator.configure(args[0]);
	                JobValidator job = initJob(args[0]);
	                BaseSearchDescartesContents checkContents = new BaseSearchDescartesContents(
	                        job.getJobConfig().getOriginalContentPath());

	                if (checkContents.getNumContents() > 0) {
	                    initWorkers(job);
	                } else {
	                    System.console()
	                            .writer()
	                            .println(
	                                    "-----No se han encontrado contenidos Descartes en la ruta especificada en la configuración------");
	                }
	            }
	        } catch (Exception e) {
	            String errorArgs = "**No se han pasado todos los argumentos, debe indicar el nombre de un fichero de configuración**\n";
	            System.err.print(errorArgs);
	        }
		
		ResourceBundle resource = ResourceBundle.getBundle("descartesPublisher");
		
		resource.getString("publisher.source");
		
		try {
			Files.walk(Paths.get(resource.getString("publisher.source")))
			.filter(p -> p.toString().endsWith(Constants.ZIP) || p.toString().endsWith(Constants.ZIP.toUpperCase()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static JobValidator initJob(String configFile) {
        JobConfiguration config = new JobConfiguration(configFile);
        JobValidator job = new JobValidator(config);
        return job;
    }

    private static void initWorkers(JobValidator job) {
        InitWorker initWorker = new InitWorker(job);
        Thread thread = new Thread(initWorker);
        thread.start();
    }
	
}
