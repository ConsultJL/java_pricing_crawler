package com.consultjl.webcrawler;

import com.jauntium.Browser;
import com.jauntium.Elements;

import java.util.Map;

public interface Crawler {

    public Browser getBrowser();

    public Map<String, String> getXPaths();

    public Elements getOffers();

    public void executeCrawler(String url, String crawlerName);
}
