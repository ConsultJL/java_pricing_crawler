package com.consultjl.webcrawler.htmlCollection;

import com.jauntium.Browser;
import com.jauntium.Elements;

import java.util.Map;

public interface Crawler {

    public Browser getBrowser();

    public String executeCrawler(String url);
}
