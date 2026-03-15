package com.mongodbrecommendationsystem.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String level;
    private String domain;
    private String language;
    private List<String> completedCoursesTags;
    private List<String> completedCoursesIds;
}
