package com.consultjl.webcrawler;

import java.util.ArrayList;
import java.util.Map;

public interface SaveResult {
    Boolean saveResult(ArrayList<Map<String, String>> crawlData);
}
