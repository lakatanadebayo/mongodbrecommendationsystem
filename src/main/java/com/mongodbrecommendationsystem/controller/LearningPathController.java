package com.mongodbrecommendationsystem.controller;

import com.mongodbrecommendationsystem.entity.LearningPath;
import com.mongodbrecommendationsystem.repository.LearningPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LearningPathController {

    private final LearningPathRepository learningPathRepository;

    @PostMapping("/save/one")
    public LearningPath saveCourse(@RequestBody LearningPath learningPath) {
        learningPath.setSlug(toSmartSlug(learningPath.getTitle().toLowerCase()));
        return learningPathRepository.save(learningPath);
    }

    @PostMapping("/save/all")
    public ResponseEntity<List<LearningPath>> saveCourses(@RequestBody List<LearningPath> courses) {
        for (LearningPath course :courses ) {
            course.setSlug(toSmartSlug((course.getTitle().toLowerCase())));
            learningPathRepository.save(course);
        }
        return ResponseEntity.ok(learningPathRepository.findAll());
    }

    @GetMapping("/find/all")
    public List<LearningPath> getAllCourses() {
        return learningPathRepository.findAll();
    }

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "au", "aux", "avec", "ce", "ces", "dans", "de", "des", "du", "elle", "en", "et",
            "eux", "il", "je", "la", "le", "leur", "lui", "ma", "mais", "me", "même", "mes",
            "moi", "mon", "ne", "nos", "notre", "nous", "on", "ou", "par", "pas", "pour",
            "qu", "que", "qui", "sa", "se", "ses", "son", "sur", "ta", "te", "tes", "toi",
            "ton", "tu", "un", "une", "vos", "votre", "vous", "c", "d", "j", "l", "à", "m",
            "n", "s", "t", "y", "été", "étée", "étées", "étés", "étant", "suis", "es",
            "est", "sommes", "êtes", "sont", "serai", "seras", "sera", "serons", "serez",
            "seront", "serais", "serait", "serions", "seriez", "seraient", "étais", "était",
            "étions", "étiez", "étaient", "fus", "fut", "fûmes", "fûtes", "furent", "sois",
            "soit", "soyons", "soyez", "soient", "fusse", "fusses", "fût", "fussions",
            "fussiez", "fussent", "ayant", "eu", "eue", "eues", "eus", "ai", "as", "avons",
            "avez", "ont", "aurai", "auras", "aura", "aurons", "aurez", "auront", "aurais",
            "aurait", "aurions", "auriez", "auraient", "avais", "avait", "avions", "aviez",
            "avaient", "eut", "eûmes", "eûtes", "eurent", "aie", "aies", "ait", "ayons",
            "ayez", "aient", "eusse", "eusses", "eût", "eussions", "eussiez", "eussent"
    ));

    private String toSmartSlug(String input) {
        // 1. Mettre en minuscules et supprimer les accents
        String slug = Normalizer.normalize(input.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // 2. Remplacer tous les caractères non alphanumériques par des espaces
        slug = slug.replaceAll("[^a-z0-9]+", " ");

        // 3. Supprimer les stop words
        StringBuilder sb = new StringBuilder();
        for (String word : slug.trim().split("\\s+")) {
            if (!STOP_WORDS.contains(word) && word.length() > 2) {
                if (sb.length() > 0) sb.append("-");
                sb.append(word);
            }
        }
        return sb.toString();
    }
}
