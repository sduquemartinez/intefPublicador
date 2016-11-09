package com.emergya.descartes.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.emergya.descartes.content.DescartesZipContentProxy;
import com.emergya.descartes.job.JobValidator;

/**
 * The Class FileManager.
 */
public class FileManager {

    private static Logger log = Logger.getLogger(FileManager.class);

    public static File extractZipContents(DescartesZipContentProxy zipContent,
            JobValidator job) {
        String pathTransformDir = job.getJobConfig().getWorkingPath();
        File outputFolder = new File(pathTransformDir + File.separator
                + zipContent.getTitle());
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipContent.getFileName().toFile());
            // get an enumeration of the ZIP file entries
            Enumeration<? extends ZipEntry> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                File destinationPath = new File(outputFolder, entry.getName());
                // create parent directories
                destinationPath.getParentFile().mkdirs();
                // if the entry is a file extract it
                if (entry.isDirectory()) {
                    continue;
                } else {
                    BufferedInputStream bis = new BufferedInputStream(
                            zipFile.getInputStream(entry));
                    int b;
                    byte buffer[] = new byte[1024];
                    FileOutputStream fos = new FileOutputStream(destinationPath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos,
                            1024);
                    while ((b = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, b);
                    }
                    bos.close();
                    bis.close();
                }
            }

        } catch (IOException e) {
            log.error(
                    "Error al descomprimir el contenido "
                            + zipContent.getTitle() + ": " + e, e);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
                return outputFolder;
            } catch (IOException ioe) {
                log.error(
                        "Error al descomprimir el contenido "
                                + zipContent.getTitle() + ": " + ioe, ioe);
            }
        }

        return outputFolder;
    }

    /**
     * Crea una copia de un fichero.
     *
     * @param sourceFilePath the source file path
     * @param destinationPath the destination path
     * @param fileName the file name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void copy(String sourceFilePath, String destinationPath,
            String fileName) throws IOException {
        File destinationPathObject = new File(destinationPath);
        File sourceFilePathObject = new File(sourceFilePath);
        if (destinationPathObject.isDirectory()) {
            File statusFileNameObject = new File(destinationPath + "/"
                    + fileName);
            FileUtils.copyFile(sourceFilePathObject, statusFileNameObject);

        }
    }

    /**
     * @param path
     * @return boolean
     */
    public static void deleteFilesInFolder(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) {
                    files[i].delete();
                }
            }
        }
        
    }
}
