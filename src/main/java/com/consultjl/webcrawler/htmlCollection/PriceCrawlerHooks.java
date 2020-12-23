package com.consultjl.webcrawler.htmlCollection;

import com.jauntium.Browser;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PriceCrawlerHooks implements CrawlerHooks {

    private String md5String(String textToHash) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(textToHash.getBytes(StandardCharsets.UTF_8));

        return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
    }

    @Override
    public ChromeOptions beforeBrowserSpinUp(ChromeOptions options) {
        return options;
    }

    @Override
    public String beforeVisit(String url) {
        // We do not want to cache files that are trying to be accessed
        if (!url.startsWith("file://")) {
            String cacheName = md5String(url) + ".html";
            File cacheFile = new File("cache/" + cacheName);
            if (cacheFile.exists()) {
                Path file = Paths.get("cache/" + cacheName);
                try {
                    BasicFileAttributes attr =
                            Files.readAttributes(file, BasicFileAttributes.class);
                    // Cache the file for 5 minutes
                    if ((System.currentTimeMillis() - attr.lastModifiedTime().toMillis()) > 300000) {
                        return url;
                    }
                } catch (IOException e) {
                    return url;
                }
                return "file://" + cacheFile.getAbsolutePath();
            }
        }
        return url;
    }

    @Override
    public Browser afterVisit(Browser browser) {
        if (!browser.getLocation().startsWith("file://")) {
            String cacheName = md5String(browser.getLocation()) + ".html";
            try {
                FileWriter cacheWrite = new FileWriter("cache/" + cacheName);
                cacheWrite.append(browser.getSource());
                cacheWrite.flush();
                cacheWrite.close();
            } catch (IOException e) {
                System.out.println("Failed to cache " + browser.getLocation());
                e.printStackTrace();
            }
        }
        return browser;
    }

    @Override
    public void beforeBrowserQuit(Browser browser) {

    }
}
