package com.tunguyen.webcrawler;

import com.tunguyen.webcrawler.crawler.CrawlerImpl;

public class WebCrawlerFactory {
    /**
     * Create a WebCrawler to crawl data from specified website
     * @param rootPage the home page of website
     * @param maxPages maximum amount of pages want to crawl
     * @return New Instance of WebCrawler
     */
    public static WebCrawler newCrawler(final String rootPage, final int maxPages) {
        return new WebCrawlerImpl()
                .setRootPage(rootPage)
                .setMaxPages(maxPages)
                .setCrawler(new CrawlerImpl());
    }
}
