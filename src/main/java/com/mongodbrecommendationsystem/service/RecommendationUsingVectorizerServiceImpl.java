package com.mongodbrecommendationsystem.service;

import com.mongodbrecommendationsystem.entity.LearningPath;
import com.mongodbrecommendationsystem.entity.LearningPathScore;
import com.mongodbrecommendationsystem.entity.User;
import com.mongodbrecommendationsystem.repository.LearningPathRepository;
import com.mongodbrecommendationsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationUsingVectorizerServiceImpl implements RecommendationUsingVectorizerService {

    private final UserRepository userRepository;
    private final LearningPathRepository learningPathRepository;
    private final VectorisationService vectorisationService;

    @Override
    public List<LearningPath> recommendCoursesForUserUsingVectorizer(String username) {

        Optional<User> OptionalUser = userRepository.findByUsername(username);

        if (OptionalUser.isEmpty()) {
            return List.of();
        }

        User user = OptionalUser.get();

        List<LearningPath> allCourses = learningPathRepository.findByLevelAndDomainAndLanguage(user.getLevel(), user.getDomain(), user.getLanguage());

        if (allCourses.isEmpty()) {
            return List.of();
        }

        // 2. Construire le profil textuel de l'utilisateur à partir des cours déjà complétés
        String userProfileText = allCourses.stream()
                .filter(c -> user.getCompletedCoursesIds().contains(c.getSlug()))
                .map(c -> String.join(" ", c.getTitle(), String.join(" ", c.getContent())))
                .collect(Collectors.joining(" "));

        double[] userVector = vectorisationService.vectorizeTFIDF(userProfileText);

        // 3. Filtrer les cours éligibles (prérequis terminés)
        List<LearningPath> eligibleCourses = allCourses.stream()
                .filter(c -> user.getCompletedCoursesIds().containsAll(c.getPrerequisiteCourseIds())
                        && !user.getCompletedCoursesIds().contains(c.getSlug())) // pas déjà fait
                .collect(Collectors.toList());

        // 4. Calculer similarité TF-IDF et SBERT
        List<LearningPathScore> scoredCourses = new ArrayList<>();
        for (LearningPath course : eligibleCourses) {
            String courseText = course.getTitle() + " " + String.join(" ", course.getContent());
            double similarity = vectorisationService.hybridSimilarity(userProfileText, courseText, 0.2);
            scoredCourses.add(new LearningPathScore(course, similarity));
        }

        // 5. Trier par similarité décroissante, puis par orderIndex croissant
        scoredCourses.sort(Comparator
                .comparingDouble(LearningPathScore::getScore).reversed()
                .thenComparingInt(c -> c.getCourse().getOrderIndex() == null ? Integer.MAX_VALUE : c.getCourse().getOrderIndex())
        );

        return scoredCourses.stream().map(LearningPathScore::getCourse).collect(Collectors.toList());
    }
}
