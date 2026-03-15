package com.mongodbrecommendationsystem.service;

import com.mongodbrecommendationsystem.entity.LearningPath;

import java.util.List;

public interface RecommendationUsingVectorizerService {
    List<LearningPath> recommendCoursesForUserUsingVectorizer(String username);
}
