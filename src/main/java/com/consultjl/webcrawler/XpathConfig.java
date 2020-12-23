package com.consultjl.webcrawler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * Get all xpaths from properties file and define them in a variable for later use
 */
@Configuration
@PropertySource("classpath:application.properties")
public class XpathConfig {
//    @Value("#{${xpaths}}")
//    public Map<String, Map<String, String>> xpaths;
}

