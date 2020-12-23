package com.consultjl.webcrawler.htmlCollection;

import com.consultjl.webcrawler.BrowserConfig;
import com.consultjl.webcrawler.XpathConfig;
import com.jauntium.Browser;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

/**
 * Pricing Crawler Implementation
 */
public class HeadlessPriceCrawler implements Crawler {
    /**
     * Used for storing the path to the chromedriver defined in properties
     */
    public String chromeDriverPath;

    /**
     * Should we use headless browser?
     */
    public Boolean useHeadless;

    private PriceCrawlerHooks crawlerHooks = new PriceCrawlerHooks();

    /**
     * BrowserConfig from application.properties
     */
    private Browser browser;

    /**
     * @param browserConfig BrowserConfig class which holds all configuration information for browser
     * @param xpathConfig XpathConfig class which holds all xpaths for a given crawler
     */
    public HeadlessPriceCrawler(BrowserConfig browserConfig) {
        // Set all of our variables properly
        this.chromeDriverPath = browserConfig.chromeDriverPath;
        this.useHeadless = browserConfig.useHeadless;
        this.browser = getBrowser();
    }

    /**
     * @return Browser Setups our browser given the defined options
     */
    @Override
    public Browser getBrowser() {
        // Setup chromedriver and any options we need
        System.out.println("Using chromedriver located at: " + this.chromeDriverPath);
        System.setProperty("webdriver.chrome.driver", this.chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        if (this.useHeadless) {
            options.addArguments("--headless");
        }
        options = crawlerHooks.beforeBrowserSpinUp(options);
        return new Browser(new ChromeDriver(options));
    }

    /**
     * @param url URL of the site to crawl
     * @param crawlerName The crawler name for looking up xpaths
     */
    @Override
    public String executeCrawler(String url) {
        // Direct the browser to visit the defined URL
        url = crawlerHooks.beforeVisit(url);
        if (url.startsWith("file://")) {
            this.browser.open(new File(url.substring(7)));
        } else {
            this.browser.visit(url);
        }
        this.browser = crawlerHooks.afterVisit(this.browser);

        String html = browser.getSource();

        return html;
    }

    public void cleanUp() {
        crawlerHooks.beforeBrowserQuit(this.browser);
        this.browser.driver.quit();
    }
}
