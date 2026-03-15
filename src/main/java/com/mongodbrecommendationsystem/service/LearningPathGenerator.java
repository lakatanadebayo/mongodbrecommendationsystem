package com.mongodbrecommendationsystem.service;

import com.mongodbrecommendationsystem.entity.LearningPath;
import com.mongodbrecommendationsystem.entity.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class LearningPathGenerator {

    private static final List<String> LEVELS = List.of("Debutant", "Intermediaire", "Avance");

    private static final List<String> DOMAINS = List.of(
            "Programmation",
            "Spring Boot",
            "Angular",
            "DevOps",
            "Bases de donnees",
            "Web",
            "Securite",
            "Architecture logicielle",
            "Tests & Qualite"
    );

    private static final List<String> LANGUAGES = List.of("fr", "en");

    public List<LearningPath> generate(int totalPerDomain) {

        List<LearningPath> dataset = new ArrayList<>();

        for (String language : LANGUAGES) {
            for (String domain : DOMAINS) {
                for (String level : LEVELS) {

                    String previousSlug = null;

                    for (int index = 1; index <= totalPerDomain; index++) {

                        LearningPath lp = buildLearningPath(
                                domain, level, language, index, previousSlug
                        );

                        dataset.add(lp);
                        previousSlug = lp.getSlug();
                    }
                }
            }
        }

        return dataset;
    }

    private LearningPath buildLearningPath(String domain, String level, String language, int index, String previousSlug) {

        String slug = generateSlug(domain, level, index);

        LearningPath lp = new LearningPath();
        lp.setSlug(slug);
        lp.setTitle(domain + " - " + level + " " + index);
        lp.setLevel(level);
        lp.setDomain(domain);
        lp.setLanguage(language);
        lp.setOrderIndex(index);

        lp.setPrerequisiteCourseIds(
                previousSlug == null ? List.of() : List.of(previousSlug)
        );

        lp.setObjectives(List.of(
                "Maîtriser les concepts clés de " + domain,
                "Appliquer les bonnes pratiques",
                "Réaliser un projet pratique"
        ));

        lp.setContent(List.of(
                "Concepts fondamentaux",
                "Approfondissement",
                "Exercices pratiques",
                "Projet guidé"
        ));

        Resource resource = new Resource();
        resource.setName(domain + " Guide");
        resource.setUrl("https://example.com");
        resource.setType("Documentation");

        lp.setResources(List.of(resource));
        lp.setTags(List.of(domain, level, "engineering"));

        return lp;
    }

    private String generateSlug(String domain, String level, int index) {
        return normalize(domain) + "-" +
                normalize(level) + "-" +
                String.format("%02d", index);
    }

    private String normalize(String text) {
        return text.toLowerCase()
                .replace(" ", "-")
                .replace("&", "et")
                .replace("é", "e")
                .replace("è", "e");
    }
}
