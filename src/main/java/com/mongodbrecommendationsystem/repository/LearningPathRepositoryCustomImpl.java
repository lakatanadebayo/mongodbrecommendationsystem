package com.mongodbrecommendationsystem.repository;

import com.mongodbrecommendationsystem.entity.LearningPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class LearningPathRepositoryCustomImpl implements LearningPathRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<String> findAllDistinctDomains() {
        return mongoTemplate.query(LearningPath.class)
                .distinct("domain")
                .as(String.class)
                .all();
    }

    @Override
    public List<String> findAllDistinctLevels() {
        return mongoTemplate.query(LearningPath.class)
                .distinct("level")
                .as(String.class)
                .all();
    }

    @Override
    public List<String> findAllDistinctLanguages() {
        return mongoTemplate.query(LearningPath.class)
                .distinct("language")
                .as(String.class)
                .all();
    }
}
