package com.consultjl.webcrawler.htmlCollection;

import com.jauntium.Browser;
import org.openqa.selenium.chrome.ChromeOptions;

public interface CrawlerHooks {
    ChromeOptions beforeBrowserSpinUp(ChromeOptions options);

    String beforeVisit(String url);

    Browser afterVisit(Browser browser);

    void beforeBrowserQuit(Browser browser);
}
