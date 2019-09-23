package com.tunguyen.webcrawler.extractor;

import org.w3c.dom.Document;

public interface Extractor {
    void extract(Document document);
}
