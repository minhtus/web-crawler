package com.tunguyen.runner;

import com.tunguyen.webcrawler.WebCrawler;
import com.tunguyen.webcrawler.WebCrawlerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        WebCrawler webCrawler = WebCrawlerFactory.newCrawler("https://simsodep.com/", 50);
        try {
            webCrawler.startCrawler();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
