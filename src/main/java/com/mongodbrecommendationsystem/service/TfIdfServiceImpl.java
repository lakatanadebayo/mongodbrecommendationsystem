package com.mongodbrecommendationsystem.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TfIdfServiceImpl implements TfIdfService {

    private final Map<String, Integer> vocabulary = new HashMap<>();
    private final List<String> corpus = new ArrayList<>();

    @Override
    public double[] vectorizeTFIDF(String text) {
        // Tokenization simple (à améliorer selon besoins)
        String[] tokens = text.toLowerCase().split("\\W+");

        // Construire le vocabulaire si nécessaire
        for (String token : tokens) {
            if (!vocabulary.containsKey(token)) {
                vocabulary.put(token, vocabulary.size());
            }
        }

        double[] vector = new double[vocabulary.size()];

        // Calcul TF
        Map<String, Integer> tf = new HashMap<>();
        for (String token : tokens) {
            tf.put(token, tf.getOrDefault(token, 0) + 1);
        }

        // Calcul TF-IDF simple (IDF basé sur corpus)
        for (Map.Entry<String, Integer> entry : tf.entrySet()) {
            String term = entry.getKey();
            int freq = entry.getValue();
            int index = vocabulary.get(term);

            double idf = Math.log((double) (corpus.size() + 1) / (1 + countDocsContaining(term))) + 1.0;
            vector[index] = freq * idf;
        }

        // Ajouter le texte au corpus
        corpus.add(text);

        return vector;
    }

    private int countDocsContaining(String term) {
        int count = 0;
        for (String doc : corpus) {
            if (doc.toLowerCase().contains(term)) count++;
        }
        return count;
    }
}
