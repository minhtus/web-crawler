package com.tunguyen.webcrawler.crawler;

import com.tunguyen.xmlparser.WellFormedUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class Crawler {
    private static final Logger LOGGER = Logger.getLogger(Crawler.class.getName());

    public Document connect(String url) throws IOException, SAXException, ParserConfigurationException {
        Document document;
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setRequestProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
        try (Reader reader = new InputStreamReader(urlConnection.getInputStream())) {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer, 0, buffer.length)) > -1) {
                builder.append(buffer, 0, bytesRead);
            }
            String body = WellFormedUtils.wellFormedHtml(builder.toString());
            LOGGER.config(body);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setNamespaceAware(false);

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(new InputSource(new StringReader(body)));
            document.normalizeDocument();
        }
        return document;
    }


}
