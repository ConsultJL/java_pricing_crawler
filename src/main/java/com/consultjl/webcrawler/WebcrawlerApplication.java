package com.consultjl.webcrawler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * A web crawler which is based on collecting pricing information. This crawler is configurable by defining xpaths in
 * application.properties
 */
@SpringBootApplication
public class WebcrawlerApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.consultjl.webcrawler");
		context.refresh();
		BrowserConfig browserConfig = context.getBean(BrowserConfig.class);
		XpathConfig xpathConfig = context.getBean(XpathConfig.class);

		PriceCrawler priceCrawler = new PriceCrawler(browserConfig, xpathConfig);
		priceCrawler.executeCrawler("https://www.amazon.com/gp/offer-listing/B086542G1M", "amazon");

		context.close();
	}
}
