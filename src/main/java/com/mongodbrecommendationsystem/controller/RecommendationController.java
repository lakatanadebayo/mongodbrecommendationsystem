package com.mongodbrecommendationsystem.controller;

import com.mongodbrecommendationsystem.entity.LearningPath;
import com.mongodbrecommendationsystem.service.RecommendationUsingVectorizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationUsingVectorizerService recommendationUsingVectorizerService;

    @GetMapping("/{username}")
    public ResponseEntity<List<LearningPath>> getRecommendations(@PathVariable String username) {
        List<LearningPath> recommendations = recommendationUsingVectorizerService.recommendCoursesForUserUsingVectorizer(username);
        return ResponseEntity.ok(recommendations);
    }
}
