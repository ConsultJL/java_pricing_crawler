package com.consultjl.webcrawler.saveResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public interface Saveable {
    Boolean saveResult(ArrayList<Map<String, String>> crawlData, String fileName) throws IOException;
}
