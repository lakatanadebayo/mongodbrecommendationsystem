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

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return List.of();
        }

        User user = optionalUser.get();

        List<LearningPath> allCourses =
                learningPathRepository.findByLevelAndDomainAndLanguage(
                        user.getLevel(),
                        user.getDomain(),
                        user.getLanguage()
                );

        System.out.println("Nombre de cours correspondant au niveau, domaine et langue de l'utilisateur : "
                + allCourses.size());

        if (allCourses.isEmpty()) {
            return List.of();
        }

        // Construire le profil utilisateur
        String userProfileText = allCourses.stream()
                .filter(c -> user.getCompletedCoursesIds().contains(c.getId()))
                .map(c -> String.join(" ",
                        c.getTitle(),
                        String.join(" ", c.getContent())))
                .collect(Collectors.joining(" "));

        // Filtrage des cours éligibles
        List<LearningPath> eligibleCourses = allCourses.stream()
                .filter(course -> isCourseEligible(course, allCourses, user))
                .collect(Collectors.toList());

        // Calcul des similarités
        List<LearningPathScore> scoredCourses = new ArrayList<>();

        for (LearningPath course : eligibleCourses) {

            String courseText =
                    course.getTitle() + " " +
                            String.join(" ", course.getContent());

            double similarity = vectorisationService.hybridSimilarity(
                    userProfileText,
                    courseText,
                    0.2
            );

            scoredCourses.add(new LearningPathScore(course, similarity));
        }

        // Tri
        scoredCourses.sort(
                Comparator.comparingDouble(LearningPathScore::getScore)
                        .reversed()
                        .thenComparingInt(c ->
                                c.getCourse().getOrderIndex() == null
                                        ? Integer.MAX_VALUE
                                        : c.getCourse().getOrderIndex())
        );

        return scoredCourses.stream()
                .map(LearningPathScore::getCourse)
                .collect(Collectors.toList());
    }

    private boolean isCourseEligible(
            LearningPath course,
            List<LearningPath> allCourses,
            User user
    ) {

        // Déjà terminé
        if (user.getCompletedCoursesIds().contains(course.getId())) {
            return false;
        }

        // Vérification des prérequis classiques
        if (course.getPrerequisiteCourseIds() != null &&
                !user.getCompletedCoursesIds()
                        .containsAll(course.getPrerequisiteCourseIds())) {
            return false;
        }

        Integer currentOrder = course.getOrderIndex();

        // Si pas d'ordre défini ou premier niveau
        if (currentOrder == null || currentOrder <= 0) {
            return true;
        }

        int previousOrder = currentOrder - 1;

        // Tous les cours de l'ordre précédent
        List<LearningPath> previousCourses = allCourses.stream()
                .filter(c -> c.getOrderIndex() != null
                        && c.getOrderIndex() == previousOrder)
                .toList();

        // Vérifier qu'ils sont tous complétés
        return previousCourses.stream()
                .allMatch(c ->
                        user.getCompletedCoursesIds().contains(c.getId()));
    }
}
