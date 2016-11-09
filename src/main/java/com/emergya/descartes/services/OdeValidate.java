package com.emergya.descartes.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.codec.Base64;

import com.emergya.descartes.job.JobValidator;

import es.pode.validador.negocio.servicio.SrvValidadorService;
import es.pode.validador.negocio.servicio.SrvValidadorServiceProxy;
import es.pode.validador.negocio.servicio.ValidaVO;

// TODO: Auto-generated Javadoc
/**
 * @author affernandez The Class odeValidate.
 */
public class OdeValidate {

	private static Logger log = Logger.getLogger(OdeValidate.class);

	/**
	 * Instantiates a new ode validate.
	 */
	public OdeValidate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Manifest validator.
	 *
	 * @param base64Contentmanifest
	 *            the base 64 contentmanifest
	 * @return true, if successful
	 * @throws IOException
	 */
	public ValidaVO manifestValidator(Path manifest, JobValidator job)
			throws IOException {

		byte[] manifestBytes = Files.readAllBytes(manifest);
		byte[] base64Contentmanifest = Base64.encode(manifestBytes);

		SrvValidadorService odeValidate = new SrvValidadorServiceProxy(job
				.getJobConfig().getValidatorUrlService());
		ValidaVO valida = null;

		valida = odeValidate.validarCatalogacionExterna(base64Contentmanifest,
				"".getBytes());

		return valida;
	}

}
