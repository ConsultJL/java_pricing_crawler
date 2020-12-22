package com.consultjl.webcrawler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class WebcrawlerApplication {

	public static void main(String[] args) {
//		SpringApplication.run(WebcrawlerApplication.class, args);
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
