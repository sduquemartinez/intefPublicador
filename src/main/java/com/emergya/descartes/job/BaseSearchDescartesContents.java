package com.emergya.descartes.job;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

public class BaseSearchDescartesContents {

    private static final Logger log = Logger
            .getLogger(BaseSearchDescartesContents.class);

    private long numContents;
    private String path;

    public BaseSearchDescartesContents(String path) {
        super();
        this.setNumContents(path);
        this.setPath(path);
    }

    private void setNumContents(final String _path) {
        try {
            numContents = Files.find(
                    Paths.get(_path),
                    0,
                    (path, attributes) -> attributes.isDirectory()
                            || attributes.isRegularFile()).count();
        } catch (IOException e) {
            log.error(
                    "Error al obtener el ńumero de contenidos Descartes en la ruta de origen",
                    e);
        }
    }

    /**
     * @return the numContents
     */
    public long getNumContents() {
        return numContents;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    private void setPath(String path) {
        this.path = path;
    }
}
