package com.tunguyen.webcrawler;

import com.tunguyen.webcrawler.extractor.Extractor;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface WebCrawler {
    void startCrawler() throws ParserConfigurationException, SAXException, IOException;

    WebCrawler addExtractor(Extractor extractor);
}
