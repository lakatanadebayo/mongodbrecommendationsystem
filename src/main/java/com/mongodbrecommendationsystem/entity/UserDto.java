package com.mongodbrecommendationsystem.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String id;
    private String username;
    private String password;
    private String level;
    private String domain;
    private String language;
    private List<LearningPath> completedLearningPath;
}
