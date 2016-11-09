package com.emergya.descartes.job;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.emergya.descartes.content.DescartesZipContentProxy;

public class SearchZipDescartesContents extends BaseSearchDescartesContents {

    private static final Logger log = Logger
            .getLogger(SearchZipDescartesContents.class);

    private List<DescartesZipContentProxy> contentList = new ArrayList<DescartesZipContentProxy>();

    public SearchZipDescartesContents(String path) {
        super(path);
    }

    public List<DescartesZipContentProxy> getDescartesZipContentProxyListNames() {
        try {
            Files.walk(Paths.get(getPath()))
                    .filter(p -> p.toString().endsWith(".zip"))
                    .forEach(this::setContentList);

        } catch (Exception e) {
            log.error(
                    "Error al obtener el nombre de los ficheros .zip de Descartes en la ruta de origen",
                    e);
        }

        return contentList;
    }

    private void setContentList(Path path) {
        DescartesZipContentProxy DescartesZipContentProxy = new DescartesZipContentProxy(
                path);
        contentList.add(DescartesZipContentProxy);
    }
}
