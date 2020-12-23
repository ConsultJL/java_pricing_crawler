package com.consultjl.webcrawler;

import com.consultjl.webcrawler.htmlCollection.HeadlessPriceCrawler;
import com.consultjl.webcrawler.postProcessing.PostProcessing;
import com.consultjl.webcrawler.saveResult.Saveable;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.lang.reflect.Array;
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
		url = "https://www.homedepot.com/sitemap/d/desktop_sitemap.xml";
		/*
		if (args.length > 0) {
			url = args[0];
		} else {
			System.out.println("You must specify a url when calling this application.");
			System.out.println("Ex: java webCrawler \"https://www.amazon.com/gp/offer-listing/B086542G1M\"");
			System.exit(1);
		}
		 */
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.consultjl.webcrawler");
		context.refresh();
		BrowserConfig browserConfig = context.getBean(BrowserConfig.class);
		AppConfig appConfig = context.getBean(AppConfig.class);

		PostProcessing postProcessing = appConfig.getProcess();
		Saveable saveResult = appConfig.getSaveResult();

		HeadlessPriceCrawler headlessPriceCrawler = new HeadlessPriceCrawler(browserConfig);
		String html = headlessPriceCrawler.executeCrawler(url);
		ArrayList<Map<String, String>> allCrawlData = postProcessing.postProcess(html);

		SiteMapDiscovery siteMapDiscovery = new SiteMapDiscovery(browserConfig);
		String siteMapUrl = siteMapDiscovery.findSiteMapURL("https://www.homedepot.com/robots.txt");
		String categorySiteMapUrl = siteMapDiscovery.findCategoryFromSiteMap(siteMapUrl);

		System.out.println(categorySiteMapUrl);

		siteMapDiscovery.cleanUp();
		System.exit(0);
		// TODO: Write a function to check robots.txt to find the sitemap file
		// TODO: Write a function to recursively search for a collection sitemap
		// TODO: Write a function that compares the collections to a specified brand to see if they sell our product
		for(Map<String, String> crawlData : allCrawlData) {
			String siteMapXml = headlessPriceCrawler.executeCrawler(crawlData.get("loc"));
			ArrayList<Map<String, String>> sitemapArr = siteMapDiscovery.parseSiteMap("");
		}

//		ArrayList<ArrayList> totalCrawlData = new ArrayList<ArrayList>();
//
//		for(Map<String, String> crawlData : allCrawlData) {
//			String siteMapXml = headlessPriceCrawler.executeCrawler(crawlData.get("loc"));
//			ArrayList<Map<String, String>> sitemapArr = postProcessing.postProcess(siteMapXml);
//			totalCrawlData.add(sitemapArr);
//		}
//
//		for(ArrayList<Map<String, String>> data : totalCrawlData) {
//			for(Map<String, String> crawlData : data) {
//				headlessPriceCrawler.executeCrawler(crawlData.get("loc"));
//			}
//		}

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

		headlessPriceCrawler.cleanUp();

		context.close();
	}
}
