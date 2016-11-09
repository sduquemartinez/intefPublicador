package com.emergya.descartes.model;

import java.io.File;
import java.util.Iterator;

import com.emergya.descartes.content.DescartesContentProxy;
import com.emergya.descartes.content.DescartesContentType;

public class ValidatedContent<T> implements Iterable<T> {

    private DescartesContentProxy contentProxy;
    private File logFile;
    private Double totalSize;
    private DescartesContentType typeContent = DescartesContentType.mef10;

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    /**
     * @return the contentProxy
     */
    public DescartesContentProxy getContentProxy() {
        return contentProxy;
    }

    /**
     * @param contentProxy the contentProxy to set
     */
    public void setContentProxy(DescartesContentProxy contentProxy) {
        this.contentProxy = contentProxy;
    }

    /**
     * @return the totalSize
     */
    public Double getTotalSize() {
        return totalSize;
    }

    /**
     * @param totalSize the totalSize to set
     */
    public void setTotalSize(Double totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * @return the typeContent
     */
    public DescartesContentType getTypeContent() {
        return typeContent;
    }

    /**
     * @param typeContent the typeContent to set
     */
    public void setTypeContent(DescartesContentType typeContent) {
        this.typeContent = typeContent;
    }

 

    /**
     * @return the logFile
     */
    public File getLogFile() {
        return logFile;
    }

    /**
     * @param logFile the logFile to set
     */
    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    
}
