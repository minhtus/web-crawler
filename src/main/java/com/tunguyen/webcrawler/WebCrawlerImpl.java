package com.tunguyen.webcrawler;

import com.tunguyen.webcrawler.crawler.Crawler;
import com.tunguyen.webcrawler.crawler.CrawlerImpl;
import com.tunguyen.webcrawler.extractor.Extractor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class WebCrawlerImpl implements WebCrawler {
    private static final Logger LOGGER = Logger.getLogger(WebCrawlerImpl.class.getName());

    private Crawler crawler;
    private Extractor extractor;
    private String rootPage;
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
        this.rootPage = rootPage;
        this.pagesToVisit.add(rootPage);
        return this;
    }


    public WebCrawlerImpl setCrawler(Crawler crawler) {
        this.crawler = crawler;
        return this;
    }

    @Override
    public WebCrawler setUserAgent(String userAgent) {
        crawler.setUserAgent(userAgent);
        return this;
    }

    @Override
    public WebCrawler setRequestHeader(String key, String value) {
        crawler.setRequestHeader(key, value);
        return this;
    }

    @Override
    public void startCrawler() throws IOException {
        if (extractor == null) {
            throw new UnsupportedOperationException("Please implement Extractor and add to crawler using addExtractor(extractor)");
        }
        String url = next();
        while (url != null && !(pagesVisited.size() > maxPages)) {
            LOGGER.info("Visiting " + url);
            try {
                Document document = crawler.connect(url).executeRequest();
                extractor.extract(document);
                Set<String> links = getLinks(document);
                pagesToVisit.addAll(links);
            } catch (SAXException | ParserConfigurationException | FileNotFoundException e) {
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
        NodeList list = document.getElementsByTagName("a");
        Set<String> links = new HashSet<>();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            Node href = node.getAttributes().getNamedItem("href");
            if (href != null) {
                links.add(href.getNodeValue());
            }
        }
        return standardizeURL(links);
    }

    private Set<String> standardizeURL(Set<String> links) {
        return links.parallelStream().map(link -> !link.startsWith("http") ? rootPage + link : link).collect(Collectors.toSet());
    }
}
