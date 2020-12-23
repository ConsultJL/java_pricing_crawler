package com.consultjl.webcrawler;

import com.consultjl.webcrawler.htmlCollection.HeaderlessPriceCrawler;
import com.consultjl.webcrawler.postProcessing.PostProcessing;
import com.consultjl.webcrawler.saveResult.Saveable;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * A web crawler which is based on collecting pricing information. This crawler is configurable by defining X-Paths in
 * application.properties
 */
@SpringBootApplication
public class WebcrawlerApplication {

	public static void main(String[] args) {
		String url = null;
		if (args.length > 0) {
			url = args[0];
		} else {
			System.out.println("You must specify both a url and crawlerName when calling this application.");
			System.out.println("Ex: java webCrawler \"https://www.amazon.com/gp/offer-listing/B086542G1M\"");
			System.exit(1);
		}
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.consultjl.webcrawler");
		context.refresh();
		BrowserConfig browserConfig = context.getBean(BrowserConfig.class);
		XpathConfig xpathConfig = context.getBean(XpathConfig.class);
		AppConfig appConfig = context.getBean(AppConfig.class);

		PostProcessing postProcessing = appConfig.getProcess();
		Saveable saveResult = appConfig.getSaveResult();

		HeaderlessPriceCrawler headerlessPriceCrawler = new HeaderlessPriceCrawler(browserConfig, xpathConfig);
		String html = headerlessPriceCrawler.executeCrawler(url);
		ArrayList<Map<String, String>> allCrawlData = postProcessing.postProcess(html);

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

		context.close();
	}
}
