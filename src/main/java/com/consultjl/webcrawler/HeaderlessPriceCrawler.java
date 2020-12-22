package com.consultjl.webcrawler;

import com.consultjl.webcrawler.saveResult.CsvResultSave;
import com.consultjl.webcrawler.saveResult.JsonResultSave;
import com.consultjl.webcrawler.saveResult.SaveResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jauntium.Browser;
import com.jauntium.Element;
import com.jauntium.Elements;
import com.jauntium.NotFound;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.json.Json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Pricing Crawler Implementation
 */
public class HeaderlessPriceCrawler implements Crawler {

    /**
     * crawlerName used to define which xpaths to select
     */
    private String crawlerName;

    /**
     * Used for storing the path to the chromedriver defined in properties
     */
    public String chromeDriverPath;

    /**
     * Should we use headless browser?
     */
    public Boolean useHeadless;

    private PriceCrawlerHooks crawlerHooks = new PriceCrawlerHooks();

    private SaveResult saveResult = new CsvResultSave();

    /**
     * BrowserConfig from application.properties
     */
    private Browser browser;

    /**
     * XpathConfig from application.properties
     */
    private final XpathConfig xpathConfig;

    /**
     * @param browserConfig BrowserConfig class which holds all configuration information for browser
     * @param xpathConfig XpathConfig class which holds all xpaths for a given crawler
     */
    public HeaderlessPriceCrawler(BrowserConfig browserConfig, XpathConfig xpathConfig) {
        // Set all of our variables properly
        this.chromeDriverPath = browserConfig.chromeDriverPath;
        this.useHeadless = browserConfig.useHeadless;
        this.xpathConfig = xpathConfig;
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
     * @return Map All xpaths defined in properties (price, condition, seller, offerList)
     */
    @Override
    public Map<String, String> getXPaths() {
        // Get all xpaths as defined by the crawler name
        return this.xpathConfig.xpaths.get(this.crawlerName);
    }

    /**
     * @param offerXpath A string which is the xpath of the offer list
     * @return Elements An Elements of Element for looping through
     */
    @Override
    public Elements getOffers(String offerXpath) {
        // Find every offer on the page using the xpath from config
        return this.browser.doc.findEvery(offerXpath);
    }

    /**
     * @param url URL of the site to crawl
     * @param crawlerName The crawler name for looking up xpaths
     */
    @Override
    public void executeCrawler(String url, String crawlerName) {
        // Set the crawler name to obtain the proper xpaths from the application.properties file
        this.crawlerName = crawlerName;

        // Direct the browser to visit the defined URL
        url = crawlerHooks.beforeVisit(url);
        if (url.startsWith("file://")) {
            this.browser.open(new File(url.substring(7)));
        } else {
            this.browser.visit(url);
        }
        this.browser = crawlerHooks.afterVisit(this.browser);

        // Load all xpaths from properties
        Map<String, String> crawlerXPaths = getXPaths();

        // Get all offers on the page
        Elements offers = this.getOffers(crawlerXPaths.get("offerList"));
        offers = crawlerHooks.afterParseOffers(offers);

        ArrayList<Map<String, String>> allCrawlData = new ArrayList<>();
        // Loop through each offer
        for (Element offer : offers) {
            offer = crawlerHooks.beforeParseElements(offer);
            try {
                // Only get information on new offers
                if (offer.findFirst(crawlerXPaths.get("condition")).getText().equals("New")) {

                    // Collect price, condition and seller
                    String price = offer.findFirst(crawlerXPaths.get("price")).getText();
                    String condition = offer.findFirst(crawlerXPaths.get("condition")).getText();
                    String seller = offer.findFirst(crawlerXPaths.get("seller")).getText();

                    // If seller is empty it's Amazon
                    if (seller.equals("")) {
                        seller = "Amazon";
                    }

                    HashMap<String, String> crawlData = new HashMap<String, String>();
                    crawlData.put("price", price);
                    crawlData.put("condition", condition);
                    crawlData.put("seller", seller);
                    crawlData = crawlerHooks.beforeDataSave(crawlData);
                    allCrawlData.add(crawlData);
                }
            } catch (NotFound notFound) {
                notFound.printStackTrace();
            }
            offer = crawlerHooks.afterParseElements(offer);
        }

        try {
            if (saveResult.saveResult(allCrawlData, "Testing")) {
                System.out.println("Saved");
            } else {
                System.out.println("Something went wrong");
                System.out.println(allCrawlData.toString());
            }
        } catch (IOException e) {
            System.out.println("We've detected a failure in writing your results. Below is an export of that data in RAW form.");
            System.out.println(allCrawlData.toString());
        }

        crawlerHooks.beforeBrowserQuit(this.browser);
        this.browser.driver.quit();
    }
}
