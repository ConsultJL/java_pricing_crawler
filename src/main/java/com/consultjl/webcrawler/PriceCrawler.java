package com.consultjl.webcrawler;

import com.jauntium.Browser;
import com.jauntium.Element;
import com.jauntium.Elements;
import com.jauntium.NotFound;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

/**
 * Pricing Crawler Implementation
 */
public class PriceCrawler implements Crawler {

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

    /**
     * BrowserConfig from application.properties
     */
    private final Browser browser;

    /**
     * XpathConfig from application.properties
     */
    private final XpathConfig xpathConfig;

    /**
     * @param browserConfig BrowserConfig class which holds all configuration information for browser
     * @param xpathConfig XpathConfig class which holds all xpaths for a given crawler
     */
    public PriceCrawler(BrowserConfig browserConfig, XpathConfig xpathConfig) {
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
        this.browser.visit(url);

        // Load all xpaths from properties
        Map<String, String> crawlerXPaths = getXPaths();

        // Get all offers on the page
        Elements offers = this.getOffers(crawlerXPaths.get("offerList"));

        // Loop through each offer
        for (Element offer : offers) {
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

                    // Output what we found
                    System.out.println("-------- Amazon Offer ----------");
                    System.out.format("Price: %s\n", price);
                    System.out.format("Condition: %s\n", condition);
                    System.out.format("Seller: %s\n", seller);
                    System.out.println("--------------------------------");
                }
            } catch (NotFound notFound) {
                notFound.printStackTrace();
            }
        }

        browser.driver.quit();
    }
}
