package com.emergya.descartes.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.emergya.descartes.job.BaseSearchDescartesContents;
import com.emergya.descartes.job.JobConfiguration;
import com.emergya.descartes.job.JobValidator;
import com.emergya.descartes.utils.Constants;
import com.emergya.descartes.worker.InitWorker;

public class Main {
	static Thread thread = null;

	private static String argumento;
	private static JobValidator job;
	private static int execute = 0;
	private static int maxExecute = 50;
	
	public static void main(String[] args) {
		final Logger log = Logger.getLogger(Main.class);

		try {
			if (args.length != 1) {
				throw new Exception();
			} else {
				argumento = args[0];

				execute();
			}
		} catch (Exception e) {
			String errorArgs = "**No se han pasado todos los argumentos, debe indicar el nombre de un fichero de configuración**\n";
			System.err.print(errorArgs);
		}
	}

	public static void execute() {
		execute++;
		try {

			PropertyConfigurator.configure(argumento);
			job = initJob(argumento);
			
			if (recursosPdte()) {
				initWorkers(job);
			} else {
				System.console()
						.writer()
						.println(
								"-----No se han encontrado contenidos Descartes en la ruta especificada en la configuración------");
			}

		} catch (Exception e) {
			String errorArgs = "**No se han pasado todos los argumentos, debe indicar el nombre de un fichero de configuración**\n";
			System.err.print(errorArgs);
		}

		ResourceBundle resource = ResourceBundle
				.getBundle("descartesPublisher");

		resource.getString("publisher.source");

		try {
			Files.walk(Paths.get(resource.getString("publisher.source")))
					.filter(p -> p.toString().endsWith(Constants.ZIP)
							|| p.toString().endsWith(
									Constants.ZIP.toUpperCase()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean executable(){
		return execute < maxExecute;
	}
	
	public static boolean recursosPdte(){
		BaseSearchDescartesContents checkContents = new BaseSearchDescartesContents(
				job.getJobConfig().getOriginalContentPath());

//		System.out.println("\n ------------ \nContenidos: "+checkContents.getNumContents()+"\n ------------ \n");
		
		return (checkContents.getNumContents() > 0 && executable());
	}

	private static JobValidator initJob(String configFile) {
		JobConfiguration config = new JobConfiguration(configFile);
		JobValidator job = new JobValidator(config);
		return job;
	}

	private static void initWorkers(JobValidator job) {
		InitWorker initWorker = new InitWorker(job);
		thread = new Thread(initWorker);
		thread.start();
	}

}
