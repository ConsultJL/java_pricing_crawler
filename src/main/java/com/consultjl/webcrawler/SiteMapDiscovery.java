package com.consultjl.webcrawler;

import com.consultjl.webcrawler.htmlCollection.HeadlessPriceCrawler;
import com.consultjl.webcrawler.postProcessing.PostProcessing;
import com.consultjl.webcrawler.postProcessing.ProcessSiteMapXML;

import java.util.ArrayList;
import java.util.Map;

public class SiteMapDiscovery {
    private BrowserConfig browserConfig;
    private HeadlessPriceCrawler headlessPriceCrawler;

    public SiteMapDiscovery(BrowserConfig browserConfig) {
        this.browserConfig = browserConfig;
        HeadlessPriceCrawler headlessPriceCrawler = new HeadlessPriceCrawler(browserConfig);
        this.headlessPriceCrawler = headlessPriceCrawler;
    }

    public String findSiteMapURL(String url) {
        String html = headlessPriceCrawler.executeCrawler(url);
        if (html.contains("Sitemap:")) {
            String sitemapUrl = html.substring(html.indexOf("Sitemap: ") + 9);
            sitemapUrl = sitemapUrl.replace("</pre>", "").replace("</body>", "").replace("</html>", "");
            return sitemapUrl.trim();
        }
        return "";
    }

    public ArrayList<Map<String, String>> parseSiteMap(String url) {
        PostProcessing postProcessing = new ProcessSiteMapXML();
        String siteMapXml = headlessPriceCrawler.executeCrawler(url);

        return postProcessing.postProcess(siteMapXml);
    }

    public ArrayList<ArrayList> findSiteLinksFromMap(String url) {
        ArrayList<ArrayList> totalCrawlData = new ArrayList<ArrayList>();
//        ArrayList<String> urlsToReturn = new ArrayList<>();

        ArrayList<Map<String, String>> sitemapArr = parseSiteMap(url);

        totalCrawlData.add(sitemapArr);

        for (Map<String, String> crawlData : sitemapArr) {
            String urlToProcess = crawlData.get("loc");
            if (urlToProcess.endsWith(".xml")) {
                totalCrawlData.add(findSiteLinksFromMap(urlToProcess));
            }
        }

        return totalCrawlData;
    }

    public String findCategoryFromSiteMap(String url) {
        ArrayList<Map<String, String>> sitemapArr = parseSiteMap(url);

        for (Map<String, String> crawlData : sitemapArr) {
            String urlToProcess = crawlData.get("loc");
            System.out.println(">> Processing " + urlToProcess);
            if (urlToProcess.endsWith(".xml") &&
                !(urlToProcess.contains("category") || urlToProcess.contains("collection"))) {
                findCategoryFromSiteMap(urlToProcess);
            } else {
                return urlToProcess;
            }
        }

        return "";
    }

    public void cleanUp() {
        headlessPriceCrawler.cleanUp();
    }
}
