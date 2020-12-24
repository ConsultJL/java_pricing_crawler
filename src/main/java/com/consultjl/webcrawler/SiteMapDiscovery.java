package com.consultjl.webcrawler;

import com.consultjl.webcrawler.htmlCollection.HeadlessPriceCrawler;
import com.consultjl.webcrawler.models.Product;
import com.consultjl.webcrawler.postProcessing.PostProcessing;
import com.consultjl.webcrawler.postProcessing.ProcessSiteMapXML;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.ArrayList;
import java.util.Map;

public class SiteMapDiscovery {
    private BrowserConfig browserConfig;
    private HeadlessPriceCrawler headlessPriceCrawler;

    public Boolean showSites = false;

    public SiteMapDiscovery(BrowserConfig browserConfig) {
        this.browserConfig = browserConfig;
        HeadlessPriceCrawler headlessPriceCrawler = new HeadlessPriceCrawler(browserConfig);
        this.headlessPriceCrawler = headlessPriceCrawler;
    }

    public String findSiteMapURL(String url) {
        String html = headlessPriceCrawler.executeCrawler(url+"/robots.txt");
        if (html.contains("Sitemap:")) {
            String sitemapUrl = html.substring(html.indexOf("Sitemap: ") + 9);
            sitemapUrl = sitemapUrl.replace("</pre>", "").replace("</body>", "").replace("</html>", "").trim();
            if (sitemapUrl.isBlank() || sitemapUrl.isEmpty()) {
                return url + "/sitemap.xml";
            }
        }
        return url + "/sitemap.xml";
    }

    public ArrayList<Map<String, String>> parseSiteMap(String url) {
        PostProcessing postProcessing = new ProcessSiteMapXML();
        String siteMapXml = headlessPriceCrawler.executeCrawler(url);

        return postProcessing.postProcess(siteMapXml);
    }

    public ArrayList<String> findCategoryFromSiteMap(String url) {
        ArrayList<Map<String, String>> sitemapArr = parseSiteMap(url);
        ArrayList<String> urlToReturn = new ArrayList<String>();

        for (Map<String, String> crawlData : sitemapArr) {
            String urlToProcess = crawlData.get("loc");
            if (urlToProcess.contains(".xml")) {
                urlToReturn.addAll(findCategoryFromSiteMap(urlToProcess));
            } else {
                urlToReturn.add(urlToProcess);
            }
        }

        return urlToReturn;
    }

    public void findProductURLs(String brand, String productTitle, String productSku, ArrayList<String> sitesFromLookup, int productMatchPerc) {
        ArrayList<String> brandSites = new ArrayList<>();

        for (String site : sitesFromLookup) {
            if (FuzzySearch.partialRatio(brand.toUpperCase(), site.toUpperCase()) > 75) {
                if (showSites) {
                    System.out.println(site);
                }
                brandSites.add(site);
            }
        }

        System.out.println(">> Checking " + brandSites.size());

        for (String site: brandSites) {
            if (FuzzySearch.partialRatio(productTitle.toUpperCase(), site.toUpperCase()) > productMatchPerc) {
                System.out.println(">> Found Product");
                System.out.println(">>>For Title: " + productTitle);
                System.out.println(">>>Site: " + site);
                System.out.println(">>>Match Precent: " + FuzzySearch.partialRatio(productTitle.toUpperCase(), site.toUpperCase()) + "\r\n");
            }
        }

        for (String site: brandSites) {
            if (FuzzySearch.partialRatio(productSku.toUpperCase(), site.toUpperCase()) > productMatchPerc) {
                System.out.println(">> Found Product");
                System.out.println(">>>For SKU: " + productSku);
                System.out.println(">>>Site: " + site);
                System.out.println(">>>Match Precent: " + FuzzySearch.partialRatio(productSku.toUpperCase(), site.toUpperCase()) + "\r\n");
            }
        }
    }
}
