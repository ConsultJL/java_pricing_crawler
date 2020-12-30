package com.consultjl.webcrawler;

import com.consultjl.webcrawler.models.BrandSite;
import com.consultjl.webcrawler.models.Product;
import com.consultjl.webcrawler.repositories.BrandSiteRepository;
import com.consultjl.webcrawler.repositories.ProductRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;

public class CrawlerThread implements Runnable {
    private Thread t;

    private AnnotationConfigApplicationContext context;
    private final String threadName;
    private final String brand;
    private final String site;
    private final int productMatchPerc;

    CrawlerThread(String name, String brand, int productMatchPerc, String site, AnnotationConfigApplicationContext context) {
        threadName = name;
        this.brand = brand;
        this.productMatchPerc = productMatchPerc;
        this.site = site;
        this.context = context;
    }

    @Override
    public void run() {
        BrowserConfig browserConfig = context.getBean(BrowserConfig.class);
        ProductRepository productRepository = context.getBean(ProductRepository.class);
        BrandSiteRepository brandSiteRepository = context.getBean(BrandSiteRepository.class);

        SiteMapDiscovery siteMapDiscovery = new SiteMapDiscovery(browserConfig);
        siteMapDiscovery.showSites = true;

        String siteMapUrl = siteMapDiscovery.findSiteMapURL(site);
        if (!site.equals("")) {
            ArrayList<String> sitesFromLookup = siteMapDiscovery.findCategoryFromSiteMap(siteMapUrl);

            if (sitesFromLookup.size() > 0) {
                ArrayList<String> productUrls = new ArrayList<>();
                for (Product p : productRepository.findAll()) {
                    String title = p.getTitle();
                    String sku = p.getSku();
                    int pid = p.getId();
                    productUrls.addAll(siteMapDiscovery.findProductURLs(brand, title, sku, sitesFromLookup, productMatchPerc, pid));
                }

                for (String productInfo : productUrls) {
                    BrandSite brandSite = new BrandSite();
                    brandSite.setUrl(productInfo);
                    brandSiteRepository.save(brandSite);
                }
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
