package com.tunguyen.webcrawler;

import com.tunguyen.webcrawler.crawler.Crawler;
import com.tunguyen.webcrawler.extractor.Extractor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

class WebCrawlerImpl implements WebCrawler {
    private static final Logger LOGGER = Logger.getLogger(WebCrawlerImpl.class.getName());
    private static final String USER_AGENT = "User-Agent";

    private Map<String, String> headers;
    private Extractor extractor;
    private int maxPages;
    private Set<String> pagesVisited;
    private List<String> pagesToVisit;


    WebCrawlerImpl() {
        this.maxPages = 1;
        pagesVisited = new HashSet<>();
        pagesToVisit = new ArrayList<>();
    }

    WebCrawlerImpl setMaxPages(int maxPages) {
        this.maxPages = maxPages;
        return this;
    }

    WebCrawlerImpl setRootPage(String rootPage) {
        this.pagesToVisit.add(rootPage);
        return this;
    }

    @Override
    public WebCrawler setUserAgent(String userAgent) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(USER_AGENT, userAgent);
        return this;
    }

    @Override
    public WebCrawler setRequestHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    @Override
    public void startCrawler() {
        if (extractor == null) {
            throw new UnsupportedOperationException("Please implement Extractor and add to crawler using addExtractor(extractor)");
        }
        String url = next();
        int visited;
        while (url != null && !( (visited = pagesVisited.size()) > maxPages)) {
            LOGGER.info("Visiting " + visited + " " + url);
            try {
                Crawler crawler = Crawler.connect(url);
                if (headers != null) {
                    headers.forEach(crawler::setRequestHeader);
                }
                Document document = crawler.executeRequest();
                extractor.extractData(document);
                Set<String> links = getLinks(document);
                pagesToVisit.addAll(links);
            } catch (SAXException | ParserConfigurationException | IOException e) {
                LOGGER.severe(e.getMessage());
            }
            url = next();
        }
    }

    @Override
    public WebCrawlerImpl addExtractor(Extractor extractor) {
        this.extractor = extractor;
        return this;
    }

    private String next() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.size() > 0 ? this.pagesToVisit.remove(0) : null;
        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }

    private Set<String> getLinks(Document document) {
        Set<String> links = new HashSet<>();
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            NodeList list = (NodeList) xPath.evaluate("//a[@href]", document, XPathConstants.NODESET);
            for (int i = 0; i < list.getLength(); i++) {
                links.add(list.item(i).getAttributes().getNamedItem("href").getNodeValue());
            }
        } catch (XPathExpressionException e) {
            LOGGER.severe(e.getMessage());
        }
        return extractor.extractURL(links);
    }
}
