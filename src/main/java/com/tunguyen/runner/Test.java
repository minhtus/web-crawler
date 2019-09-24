package com.tunguyen.runner;

import com.tunguyen.webcrawler.WebCrawler;
import com.tunguyen.webcrawler.WebCrawlerFactory;
import com.tunguyen.webcrawler.extractor.Extractor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        WebCrawler webCrawler = WebCrawlerFactory.newCrawler("https://simthanglong.vn/", 50)
                .addExtractor(document -> {
                    NodeList list = document.getElementsByTagName("input");
                    for (int i = 0; i < list.getLength(); i++) {
                        NamedNodeMap attributes = list.item(i).getAttributes();
                        Node name = attributes.getNamedItem("name");
                        Node value = attributes.getNamedItem("value");
                        System.out.printf("%s: %s\n", name != null ? name.getNodeValue() : "", value != null ? value.getNodeValue() : "");
                    }
                });
        try {
            webCrawler.startCrawler();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
