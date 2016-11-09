package com.emergya.descartes.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.emergya.descartes.content.DescartesContentProxy;

public class SearchDescartesContents extends BaseSearchDescartesContents {

    private static final Logger log = Logger
            .getLogger(SearchDescartesContents.class);

    private List<DescartesContentProxy> contentList = new ArrayList<DescartesContentProxy>();

    public SearchDescartesContents(String path) {
        super(path);
    }

    public List<DescartesContentProxy> getDescartesContentProxyListNames() {
        try {
            File[] folders = new File(getPath()).listFiles();
            for (File folder : folders) {
                if (folder.isDirectory()) {
                    setContentList(folder);
                }
            }

        } catch (Exception e) {
            log.error(
                    "Error al obtener el nombre de los contenidos de Descartes en la ruta de origen",
                    e);
        }

        return contentList;
    }

    private void setContentList(File path) {
        DescartesContentProxy DescartesContentProxy = new DescartesContentProxy();
        DescartesContentProxy.setLocalCopy(path);
        contentList.add(DescartesContentProxy);
    }
}
