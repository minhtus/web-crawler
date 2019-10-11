package com.tunguyen.webcrawler.extractor;

import com.tunguyen.webcrawler.WebCrawlerFactory;
import org.w3c.dom.Document;

import java.util.Set;
import java.util.stream.Collectors;

public interface Extractor {
    /**
     * Parsed document receive from crawler
     * Do business data extracting here
     */
    void extractData(String url, Document document);

    /**
     * Define which URL in all href of website to visit and crawl
     */
    Set<String> extractURL(Set<String> links);
//    {
//        return links.parallelStream()
//                .filter(link -> {
//                    if (link.startsWith("http") || link.startsWith("https")) {
//                        return link.startsWith(rootPage);
//                    } else return !link.contains("javascript");
//                })
//                .map(link -> link.startsWith("http") || link.startsWith("https") ? link : WebCrawlerFactory.rootPage + link).collect(Collectors.toSet());
//    }
}
