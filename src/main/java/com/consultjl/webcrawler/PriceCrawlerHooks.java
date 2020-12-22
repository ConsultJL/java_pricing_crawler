package com.consultjl.webcrawler;

import com.jauntium.Browser;
import com.jauntium.Element;
import com.jauntium.Elements;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

public class PriceCrawlerHooks implements CrawlerHooks {
    @Override
    public ChromeOptions beforeBrowserSpinUp(ChromeOptions options) {
        return options;
    }

    @Override
    public String beforeVisit(String url) {
        return url;
    }

    @Override
    public Browser afterVisit(Browser browser) {
        return browser;
    }

    @Override
    public Elements afterParseOffers(Elements offers) {
        return offers;
    }

    @Override
    public Element beforeParseElements(Element offer) {
        return offer;
    }

    @Override
    public Element afterParseElements(Element offer) {
        return offer;
    }

    @Override
    public void beforeBrowserQuit(Browser browser) {

    }

    @Override
    public HashMap<String, String> beforeDataSave(HashMap<String, String> crawlData) {
        return crawlData;
    }
}
