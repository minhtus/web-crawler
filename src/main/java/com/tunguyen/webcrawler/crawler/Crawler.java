package com.tunguyen.webcrawler.crawler;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface Crawler {
    Document connect(String url) throws IOException, SAXException, ParserConfigurationException;
}
