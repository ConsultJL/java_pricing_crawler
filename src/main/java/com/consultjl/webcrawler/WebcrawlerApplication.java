package com.consultjl.webcrawler;

import com.consultjl.webcrawler.models.Product;
import com.consultjl.webcrawler.repositories.ProductRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;

/**
 * A web crawler which is based on collecting pricing information. This crawler is configurable by defining X-Paths in
 * application.properties
 */
@SpringBootApplication
public class WebcrawlerApplication {

	public static void main(String[] args) {
		String url = "http://agstreet.com";
		String brand = "milwaukee";
		int productMatchPerc = 95;

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.consultjl.webcrawler");
		context.refresh();
		BrowserConfig browserConfig = context.getBean(BrowserConfig.class);
		ProductRepository productRepository = context.getBean(ProductRepository.class);

		SiteMapDiscovery siteMapDiscovery = new SiteMapDiscovery(browserConfig);

		siteMapDiscovery.showSites = false;
		String siteMapUrl = siteMapDiscovery.findSiteMapURL(url);
		System.out.println(">> Processing Site Map: " + siteMapUrl);
		ArrayList<String> sitesFromLookup = siteMapDiscovery.findCategoryFromSiteMap(siteMapUrl);

		for (Product p : productRepository.findAll()) {
			String title = p.getTitle();
			String sku = p.getSku();

			System.out.println("Starting search...");
			System.out.println(">>> Brand: " + brand);
			System.out.println(">>> Title: " + title);
			System.out.println(">>> SKU: " + sku);

			siteMapDiscovery.findProductURLs(brand, title, sku, sitesFromLookup, productMatchPerc);
		}
		siteMapDiscovery.cleanUp();

		context.close();
	}
}
