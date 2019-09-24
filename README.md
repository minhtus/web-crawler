# WebCrawler
Simple Java web crawler that iterate through pages of a website to crawl and extract data.

## Building
In order to build this module, maven is used.
``` shell=
mvn package
```
Output jar is at`$PROJECCT_DIR/target/web-crawler-$version.jar`

## Using
Create an instance of `WebCrawler` using `WebCrawlerFactory`
Define and add an `Extractor` to crawler using `addExtractor`
Start crawling using by calling `startCrawler`

``` java=
WebCrawler webCrawler = WebCrawlerFactory.newCrawler("URL", maxPagesToVisit)
                .addExtractor(document -> {
                // do extracting data here
                    NodeList list = document.getElementsByTagName("sometag");
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
```

## References
[Using "State machine" to transform HTML into well-formed XML without hard code by `KieuTrongKhanh`](http://www.kieutrongkhanh.net/2018/12/su-dung-mo-hinh-may-trang-thai-state.html)