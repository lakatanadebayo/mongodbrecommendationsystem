package com.mongodbrecommendationsystem.entity;

import com.mongodbrecommendationsystem.service.DatasetExporter;
import com.mongodbrecommendationsystem.service.LearningPathGenerator;

import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {
        /*LocalDate dateTime = LocalDate.of(2025, 7, 7);
        System.out.println(dateTime.plusDays(188));*/

        LearningPathGenerator generator = new LearningPathGenerator();
        DatasetExporter export = new DatasetExporter();
        // 20 par niveau par domaine
        List<LearningPath> dataset = generator.generate(20);

        export.export(dataset, "C:\\Users\\PROBOOK\\OneDrive\\Desktop\\donnees_simmulees\\massive_learningpaths.json");

        System.out.println("Dataset généré avec succès !");
        System.out.println("Total : " + dataset.size() + " LearningPaths");
    }
}
