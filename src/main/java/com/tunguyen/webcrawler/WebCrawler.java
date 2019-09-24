package com.tunguyen.webcrawler;

import com.tunguyen.webcrawler.extractor.Extractor;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface WebCrawler {
    /**
     * Add Extractor to crawler for extracting necessary data
     * @param extractor @see com.tunguyen.webcrawler.extractor.Extractor
     */
    WebCrawler addExtractor(Extractor extractor);

    /**
     * Init the crawling process
     */
    void startCrawler() throws ParserConfigurationException, SAXException, IOException;
}
