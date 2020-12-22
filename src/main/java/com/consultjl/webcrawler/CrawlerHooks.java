package com.consultjl.webcrawler;

import com.jauntium.Browser;
import com.jauntium.Element;
import com.jauntium.Elements;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

public interface CrawlerHooks {
    ChromeOptions beforeBrowserSpinUp(ChromeOptions options);

    String beforeVisit(String url);

    Browser afterVisit(Browser browser);

    Elements afterParseOffers(Elements offers);

    Element beforeParseElements(Element offer);

    HashMap<String, String> beforeDataSave(HashMap<String, String> crawlData);

    Element afterParseElements(Element offer);

    void beforeBrowserQuit(Browser browser);
}
