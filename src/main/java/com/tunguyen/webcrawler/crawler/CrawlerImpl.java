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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CrawlerImpl implements Crawler{
    private static final Logger LOGGER = Logger.getLogger(CrawlerImpl.class.getName());
    private static final String USER_AGENT = "User-Agent";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    private URLConnection urlConnection;
    private Map<String, String> headers;

    public Crawler connect(String url) throws IOException {
        urlConnection = new URL(url).openConnection();
        return this;
    }

    public Crawler setUserAgent(String userAgent) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(USER_AGENT, userAgent);
        return this;
    }

    public Crawler setRequestHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    @Override
    public Document executeRequest() throws IOException, SAXException, ParserConfigurationException {
        if (urlConnection == null) {
            System.out.println("URL null");
        }
        urlConnection.setRequestProperty(USER_AGENT, DEFAULT_USER_AGENT);
        if (headers != null) {
            headers.forEach((key, value) -> urlConnection.setRequestProperty(key, value));
        }
        try (Reader reader = new InputStreamReader(urlConnection.getInputStream())) {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer, 0, buffer.length)) > -1) {
                builder.append(buffer, 0, bytesRead);
            }
            String body = WellFormedUtils.wellFormedHtml(builder.toString());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setNamespaceAware(false);

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(body)));
            document.normalizeDocument();
            return document;
        }
    }


}
