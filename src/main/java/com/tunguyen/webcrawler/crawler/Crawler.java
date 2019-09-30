package com.tunguyen.webcrawler.crawler;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public abstract class Crawler {
    public static Crawler connect(String url) throws IOException {
        return new CrawlerImpl(url);
    }

    public abstract Crawler setRequestHeader(String key, String value);

    public abstract Document executeRequest() throws IOException, SAXException, ParserConfigurationException;
}
