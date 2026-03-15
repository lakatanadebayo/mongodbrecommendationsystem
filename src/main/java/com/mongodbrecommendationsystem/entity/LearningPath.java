package com.mongodbrecommendationsystem.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "learningPaths")
@Data
public class LearningPath {
    @Id
    private String id;
    private String slug;
    private String title;
    private String level;
    private String domain;
    private String language;
    private Integer orderIndex;
    private List<String> prerequisiteCourseIds;
    private List<String> objectives;
    private List<String> content;
    private List<Resource> resources;
    private List<String> tags;

    /*@Data
    public static class Resource {
        private String name;
        private String url;
        private String type;
    }*/
}
