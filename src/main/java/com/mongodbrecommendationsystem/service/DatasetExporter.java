package com.mongodbrecommendationsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodbrecommendationsystem.entity.LearningPath;

import java.io.File;
import java.util.List;

public class DatasetExporter {
    public void export(List<LearningPath> dataset, String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), dataset);
    }
}
