package com.consultjl.webcrawler.htmlProcessing;

import java.util.ArrayList;
import java.util.Map;

public interface HtmlProcessor {
    ArrayList<Map<String, String>> processHtml(String html);
}
