package com.consultjl.webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public interface SaveResult {
    Boolean saveResult(ArrayList<Map<String, String>> crawlData) throws IOException;
}
