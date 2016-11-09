package com.emergya.descartes.content;

import java.nio.file.Path;

public class DescartesZipContentProxy {

    private Path fileName;
    private String title;

    /**
     * @param fileName
     */
    public DescartesZipContentProxy(Path fileName) {
        this.setFileName(fileName);
        this.setTitle(fileName.toFile().getName().replace(".zip", ""));
    }

    public DescartesZipContentProxy() {
        super();
    }

    /**
     * @return the fileName
     */
    public Path getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    private void setFileName(Path fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    private void setTitle(String title) {
        this.title = title;
    }
}
