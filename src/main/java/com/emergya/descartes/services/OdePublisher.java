package com.emergya.descartes.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.codec.Utf8;

import com.emergya.descartes.content.DescartesContentProxy;
import com.emergya.descartes.job.JobValidator;
import com.emergya.descartes.utils.Constants;
import com.emergya.descartes.utils.OdesUtil;

import es.pode.publicacion.negocio.servicios.ResultadoPublicacionVO;
import es.pode.publicacion.negocio.servicios.SrvPublicacionService;
import es.pode.publicacion.negocio.servicios.SrvPublicacionServiceProxy;

// TODO: Auto-generated Javadoc
/**
 * The Class OdePublisher.
 *
 * @author affernandez The Class odePublisher.
 */
public class OdePublisher {

	private static Logger log = Logger.getLogger(OdePublisher.class);
	/**
	 * Instantiates a new ode publisher.
	 */
	public OdePublisher() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Ode publisher.
	 *
	 * @param contentToPublish
	 *            the content to publish
	 * @param job
	 *            the job
	 * @return the file
	 * @throws ServiceException
	 *             the service exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public File publish(DescartesContentProxy contentToPublish, JobValidator job)
			throws ServiceException, IOException {

		Path pathManifest = Paths.get(contentToPublish.getLocalCopy()
				+ File.separator + contentToPublish.getManifestFile());

		/* Obtenemos el tittle del manifest */
		byte[] manifestBytes = Files.readAllBytes(pathManifest);

		String title = (String) OdesUtil.getManifestProperty(manifestBytes, job
				.getJobConfig().getTittleManifest(), false);
		/* Obtenemos el tittle del manifest */

		String idUsers[] = { "jaescriza" };

//		String[] cadena = contentToPublish.getLocalCopy().toString().split("/");

		String[] cadena = contentToPublish.getLocalCopy().toString().split(
				File.separator.equals("/")?"/":"\\\\");
		
//		File zipFile = zipBase64REA(
//				contentToPublish,
//				Paths.get(job.getJobConfig().getWorkingPath() + File.separator
//						+ cadena[cadena.length - 1] + ".zip"));
		File zipFile = Paths.get(job.getJobConfig().getOriginalContentPath() + File.separator
				+ cadena[cadena.length - 1] + ".zip").toFile();

//		byte[] base64Content = Files.readAllBytes(zipFile.toPath());
		byte[] base64Content = Base64.encode(Files.readAllBytes(zipFile.toPath()));
				
		SrvPublicacionService odePublish = new SrvPublicacionServiceProxy(
				job.getJobConfig().getPublicatorUrlService());
		
		ResultadoPublicacionVO resultado = odePublish.publicacionExterna(base64Content, "".getBytes(),
				"jaescriza@emergya.com", title, idUsers, Boolean.TRUE,
				Constants.ODE_COMPLETO);
						
		 if (!(resultado != null && "0.0".equals(resultado.getIdResultado()))) {
             zipFile = null;
             log.info("******************RESULTADO NOK********************");
             log.info("ID: "+ resultado.getIdODE());
             log.info("Descripción: "+ resultado.getDescripcion());
             log.info("ID Resultado: "+ resultado.getIdResultado());
             log.info("Nodo Publicación: "+resultado.getNodoPublicacion());
             log.info("Path Imagen: "+ resultado.getPathImagen());
             log.info("Path Repositorio: "+resultado.getPathRepositorio());
         }

		return zipFile;
	}
	

	/**
	 * Zip base 64 REA.
	 *
	 * @param contentToPublish
	 *            the content to publish
	 * @param workPath
	 *            the work path
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private File zipBase64REA(DescartesContentProxy contentToPublish,
			Path workPath) throws IOException {

		File zipFileName = Paths.get(workPath.toUri()).toFile();

		FileOutputStream fos = new FileOutputStream(zipFileName);

		ZipOutputStream zos = new ZipOutputStream(fos);
		addDirToArchive(zos, contentToPublish.getLocalCopy(), contentToPublish
				.getLocalCopy().toString());
		

		zos.close();

		return zipFileName;
	}
	
	

	/**
	 * Método recursivo para la formación del ZIP.
	 *
	 * @param zos
	 *            buffer del zip
	 * @param srcFile
	 *            ruta de los ficheros a zippear
	 * @param noFolderInZip
	 *            necesario para que no meta todoa la ruta de directorios dentro
	 *            de zip
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void addDirToArchive(ZipOutputStream zos, File srcFile,
			String noFolderInZip) throws IOException {

		if (srcFile.isDirectory()) {
			File[] files = srcFile.listFiles();

			for (int i = 0; i < files.length; i++) {
				addDirToArchive(zos, new File(srcFile, files[i].getName()),
						noFolderInZip);
			}
		} else {
			byte[] buf = new byte[1024];
			int len;
			// Create a new Zip entry with the file's name.
			ZipEntry zipEntry = new ZipEntry(srcFile.toString().replace(
					noFolderInZip+File.separator, ""));
			// Create a buffered input stream out of the file

			// we're trying to add into the Zip archive.

			FileInputStream fin;
			fin = new FileInputStream(srcFile);

			BufferedInputStream in = new BufferedInputStream(fin);
			zos.putNextEntry(zipEntry);
			// Read bytes from the file and write into the Zip archive.

			while ((len = in.read(buf)) >= 0) {
				zos.write(buf, 0, len);
			}
			// Close the input stream.

			in.close();
		}
	}

}
