package com.consultjl.webcrawler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
public class XpathConfig {
    @Value("#{${xpaths}}")
    public Map<String, Map<String, String>> xpaths;
}

