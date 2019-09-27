package com.tunguyen.webcrawler.crawler;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface Crawler {
    Crawler connect(String url) throws IOException;

    Crawler setUserAgent(String userAgent);

    Crawler setRequestHeader(String key, String value);

    Document executeRequest() throws IOException, SAXException, ParserConfigurationException;
}
