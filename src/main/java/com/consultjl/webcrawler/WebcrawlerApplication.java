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
		String url = null;
		String crawlerName = null;
		if (args.length > 0) {
			url = args[0];
			crawlerName = args[1];
		} else {
			System.out.println("You must specify both a url and crawlerName when calling this application.");
			System.out.println("Ex: java webCrawler \"https://www.amazon.com/gp/offer-listing/B086542G1M\" amazon");
			System.exit(1);
		}
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.consultjl.webcrawler");
		context.refresh();
		BrowserConfig browserConfig = context.getBean(BrowserConfig.class);
		XpathConfig xpathConfig = context.getBean(XpathConfig.class);

		HeaderlessPriceCrawler headerlessPriceCrawler = new HeaderlessPriceCrawler(browserConfig, xpathConfig);
		headerlessPriceCrawler.executeCrawler(url, crawlerName);

		context.close();
	}
}
