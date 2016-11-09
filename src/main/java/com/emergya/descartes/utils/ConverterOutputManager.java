package com.emergya.descartes.utils;

import java.io.FileWriter;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.emergya.descartes.content.ValidatedContent;

import es.pode.validador.negocio.servicio.ErrorParseoVO;
import es.pode.validador.negocio.servicio.ValidaVO;

public class ConverterOutputManager {
	private static Logger log = Logger.getLogger(ConverterOutputManager.class);

	/**
	 * Crea log de los contenidos convertidos.
	 * 
	 * @param <T>
	 * @param file
	 *            the file
	 * @param doc
	 *            the doc
	 * @return the file
	 */
	public static <T> void createLogValidatedContentCSV(
			final ValidatedContent<T> convertedContent, ValidaVO valida) {
		try {
			FileWriter writer = new FileWriter(convertedContent.getLogFile());
			CSVUtils.writeLine(writer, Arrays.asList("FILE_PATH", "ERRORS"));

			for (ErrorParseoVO error : valida.getErrores()) {
				CSVUtils.writeLine(writer,
						Arrays.asList(error.getLinea(), error.getMensaje()));
			}

			writer.flush();
			writer.close();

		} catch (Exception e) {
			log.error("Error al escribir en fichero "
					+ convertedContent.getLogFile().getName());
		}
	}

}
