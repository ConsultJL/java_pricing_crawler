package com.consultjl.webcrawler.saveResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CsvResultSave implements SaveResult {
    /**
     * showHeader Boolean Show header in CSV
     */
    public Boolean showHeader = true;

    /**
     * @param crawlData Data from the crawler
     * @return Boolean
     */
    @Override
    public Boolean saveResult(ArrayList<Map<String, String>> crawlData, String fileName) throws IOException {
        if (crawlData.size() == 0) {
            return false;
        }
        if (fileName.isEmpty()) {
            return false;
        }
        try {
            FileWriter csvWriter = new FileWriter(fileName + ".csv");
            int i = 1;
            if (showHeader) {
                for (String key : crawlData.get(0).keySet()) {
                    csvWriter.append(key);
                    if (i < crawlData.get(0).size()) {
                        csvWriter.append(",");
                    }
                    i++;
                }
                csvWriter.append("\n");
            }
            for (Map<String, String> dataPoint : crawlData) {
                i = 1;
                for (Map.Entry result : dataPoint.entrySet()) {
                    csvWriter.append(result.getValue().toString());
                    if (i < dataPoint.size()) {
                        csvWriter.append(",");
                    }
                    i++;
                }
                csvWriter.append("\r\n");
            }
            csvWriter.flush();
            csvWriter.close();
            return true;
        } catch (IOException e) {
            throw e;
        }
    }
}
