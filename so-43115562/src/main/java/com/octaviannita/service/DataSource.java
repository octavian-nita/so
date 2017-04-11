package com.octaviannita.service;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Apr 11, 2017
 */
public class DataSource {

    private final DocumentBuilder builder;

    public DataSource() {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("cannot create a data source", e);
        }
    }

    public Document getData(String content) {
        try {
            return content == null ? null : builder.parse(new ByteArrayInputStream(content.trim().getBytes("UTF-8")));
        } catch (SAXException | IOException e) {
            throw new RuntimeException("cannot load data from content", e);
        }
    }

    public Document getTestData() {
        return getData(//@fmt:off
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
            "<racine> " +
            "  <noeud-1> " +
            "    <noeud-1-1>valeur-1-1</noeud-1-1> " +
            "  </noeud-1> " +
            "  <noeud-2 /> " +
            "  <noeud-3> " +
            "    <noeud-3-4/> " +
            "  </noeud-3> " +
            "</racine>"
        );//@fmt:on
    }
}
