package com.consultjl.webcrawler;

import com.consultjl.webcrawler.postProcessing.PostProcessing;
import com.consultjl.webcrawler.postProcessing.ProcessAmazonHtml;
import com.consultjl.webcrawler.saveResult.CsvResultSave;
import com.consultjl.webcrawler.saveResult.JsonResultSave;
import com.consultjl.webcrawler.saveResult.Saveable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Value("${htmlproc}")
    public String htmlproc;
    @Value("${saveproc}")
    public String saveproc;

    public PostProcessing getProcess() {
        switch(htmlproc) {
            case "amazon":
            default:
                return new ProcessAmazonHtml();
        }
    }

    public Saveable getSaveResult() {
        switch(saveproc) {
            case "json":
                return new JsonResultSave();
            case "csv":
            default:
                return new CsvResultSave();
        }
    }
}