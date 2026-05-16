package com.mongodbrecommendationsystem.repository;

import java.util.List;

public interface LearningPathRepositoryCustom {
    List<String> findAllDistinctDomains();
    List<String> findAllDistinctLevels();
    List<String> findAllDistinctLanguages();
}
