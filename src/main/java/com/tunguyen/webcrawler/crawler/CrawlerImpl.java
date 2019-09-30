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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class CrawlerImpl extends Crawler {
    private static final Logger LOGGER = Logger.getLogger(CrawlerImpl.class.getName());
    private static final String USER_AGENT = "User-Agent";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    private String url;
    private HttpURLConnection connection;

    CrawlerImpl(String url) throws IOException {
        this.url = url;
        connection = createConnection();
    }

    public Crawler setRequestHeader(String key, String value) {
        connection.addRequestProperty(key, value);
        return this;
    }

    @Override
    public Document executeRequest() throws IOException, SAXException, ParserConfigurationException {
        try (Reader reader = new InputStreamReader(connection.getInputStream())) {
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

    private HttpURLConnection createConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.addRequestProperty(USER_AGENT, DEFAULT_USER_AGENT);
        return connection;
    }


}
