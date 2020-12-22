package com.consultjl.webcrawler;

import com.jauntium.Browser;
import com.jauntium.Element;
import com.jauntium.Elements;
import com.jauntium.NotFound;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

public class PriceCrawler implements Crawler {

    private String crawlerName;
    public String chromeDriverPath;
    public Boolean useHeadless;
    private final Browser browser;
    private final XpathConfig xpathConfig;

    public PriceCrawler(BrowserConfig browserConfig, XpathConfig xpathConfig) {
        this.chromeDriverPath = browserConfig.chromeDriverPath;
        this.useHeadless = browserConfig.useHeadless;
        this.xpathConfig = xpathConfig;
        this.browser = getBrowser();
    }

    @Override
    public Browser getBrowser() {
        System.out.println("Using chromedriver located at: " + this.chromeDriverPath);
        System.setProperty("webdriver.chrome.driver", this.chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        if (this.useHeadless) {
            options.addArguments("--headless");
        }
        return new Browser(new ChromeDriver(options));
    }

    @Override
    public Map<String, String> getXPaths() {
        return this.xpathConfig.xpaths.get(this.crawlerName);
    }

    @Override
    public Elements getOffers() {
        return this.browser.doc.findEvery("<div class=olpOffer>");
    }

    @Override
    public void executeCrawler(String url, String crawlerName) {
        this.browser.visit(url);
        this.crawlerName = crawlerName;

        Map<String, String> crawlerXPaths = getXPaths();
        Elements offers = this.getOffers();

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
