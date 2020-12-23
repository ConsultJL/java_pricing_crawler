package com.consultjl.webcrawler.saveResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class JsonResultSave implements Saveable {
    @Override
    public Boolean saveResult(ArrayList<Map<String, String>> crawlData, String fileName) throws IOException {
        if (fileName.isEmpty()) {
            return false;
        }
        try {
            FileWriter jsonWriter = new FileWriter(fileName + ".json");
            jsonWriter.append("{\r\n");
            ObjectMapper objectMapper = new ObjectMapper();
            int i = 0;
            for (Map<String, String> dataPoint : crawlData) {
                if (i > 0) {
                    jsonWriter.append(",\r\n");
                }
                try {
                    String json = objectMapper.writeValueAsString(dataPoint);
                    jsonWriter.append(json);
                } catch (JsonProcessingException e) {
                    jsonWriter.append("{'ERROR'}");
                }
                i++;
            }
            jsonWriter.append("\r\n}");
            jsonWriter.flush();
            jsonWriter.close();
        } catch (IOException e) {
            throw e;
        }

        return true;
    }
}
