package com.consultjl.webcrawler;

import com.consultjl.webcrawler.models.Site;
import com.consultjl.webcrawler.repositories.SiteRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * A web crawler which is based on collecting pricing information. This crawler is configurable by defining X-Paths in
 * application.properties
 */
@SpringBootApplication
public class WebcrawlerApplication {

	public static void main(String[] args) {
		String brand = "milwaukee";
		int productMatchPerc = 70;

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.consultjl.webcrawler");
		context.refresh();
		SiteRepository siteRepository = context.getBean(SiteRepository.class);

		int i = 1;
		for (Site s : siteRepository.findAll()) {
			System.out.println(">> Processing " + s.getUrl() + " Thread: " + i);
			CrawlerThread crawlerThread = new CrawlerThread("webcrawler-" + i, brand, productMatchPerc, s.getUrl(), context);
			crawlerThread.start();
		}
	}
}

