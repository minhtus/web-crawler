package com.tunguyen.webcrawler;

import com.tunguyen.webcrawler.extractor.Extractor;

public class WebCrawlerFactory {
    public static WebCrawlerImpl newCrawler(final String rootPage, final int maxPages) {
        return new WebCrawlerImpl()
                .setRootPage(rootPage)
                .setMaxPages(maxPages);
    }
}
