package com.consultjl.webcrawler;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Get all browser configuration options and define them for later use
 */
@Configuration
@PropertySource("classpath:application.properties")
public class BrowserConfig {
    @Value("${browser.chromeDriverPath}")
    public String chromeDriverPath;
    @Value("${browser.useHeadless}")
    public boolean useHeadless;
}
