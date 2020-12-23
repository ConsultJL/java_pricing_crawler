package com.consultjl.webcrawler.postProcessing;

import java.util.ArrayList;
import java.util.Map;

public interface PostProcessing {
    ArrayList<Map<String, String>> postProcess(String processText);
}
