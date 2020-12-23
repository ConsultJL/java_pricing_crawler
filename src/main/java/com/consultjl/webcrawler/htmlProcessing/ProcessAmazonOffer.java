package com.consultjl.webcrawler.htmlProcessing;

import com.consultjl.webcrawler.XpathConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcessAmazonOffer implements HtmlProcessor{
    private String crawlerName = "amazon";
    private XpathConfig xpathConfig;

    public ProcessAmazonOffer(XpathConfig xpathConfig) {
        this.xpathConfig = xpathConfig;
    }

    @Override
    public ArrayList<Map<String, String>> processHtml(String html) {
        Map<String, String> crawlerXPaths = this.xpathConfig.xpaths.get(this.crawlerName);
        HashMap<String, String> crawlData = null;
        Document doc = Jsoup.parse(html);
        ArrayList<Map<String, String>> allCrawlData = new ArrayList<>();

        try {
            Elements offers = doc.select("div.olpOffer");

            // Loop through each offer
            for (Element offer : offers) {
                String condition = offer.selectFirst("span.olpCondition").text();
                if (condition.equals("New")) {
                    String price = offer.selectFirst("span.olpOfferPrice").html();
                    String seller = offer.selectFirst("h3.olpSellerName").text();

                    // If seller is empty it's Amazon
                    if (seller.equals("")) {
                        seller = "Amazon";
                    }

                    crawlData = new HashMap<String, String>();
                    crawlData.put("price", price);
                    crawlData.put("condition", condition);
                    crawlData.put("seller", seller);
                    allCrawlData.add(crawlData);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return allCrawlData;
    }
}
