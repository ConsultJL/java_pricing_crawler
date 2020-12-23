package com.consultjl.webcrawler.postProcessing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcessSiteMapXML implements PostProcessing {
    @Override
    public ArrayList<Map<String, String>> postProcess(String processText) {
        ArrayList<Map<String, String>> allCrawlData = new ArrayList<>();
        HashMap<String, String> crawlData = null;

        Document doc = Jsoup.parse(processText);
        for (Element loc : doc.select("loc")) {
            crawlData = new HashMap<String, String>();
            crawlData.put("loc", loc.text());
            allCrawlData.add(crawlData);
        }

        return allCrawlData;
    }
}
