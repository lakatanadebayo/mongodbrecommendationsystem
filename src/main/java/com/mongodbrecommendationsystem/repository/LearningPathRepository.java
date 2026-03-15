package com.mongodbrecommendationsystem.repository;

import com.mongodbrecommendationsystem.entity.LearningPath;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LearningPathRepository extends MongoRepository<LearningPath, String> {
    List<LearningPath> findByLevelAndDomainAndLanguage(String level, String domain, String language);
    List<LearningPath> findByDomainAndLanguage(String domain, String language);
}
