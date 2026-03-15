package com.mongodbrecommendationsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VectorisationServiceImpl implements VectorisationService {

    private final TfIdfService tfIdfService;
    private final SbertService sbertService;

    @Override
    public double[] vectorizeTFIDF(String text) {
        return tfIdfService.vectorizeTFIDF(text);
    }

    @Override
    public double[] vectorizeSBERT(String text) {
        return sbertService.encode(text);
    }

    @Override
    public double cosineSimilarity(double[] v1, double[] v2) {

        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;

        int size = Math.min(v1.length, v2.length);
        for (int i = 0; i < size; i++) {
            dot += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        return (norm1 == 0 || norm2 == 0) ? 0.0 : dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @Override
    public double hybridSimilarity(String text1, String text2, double alpha) {
        double tfidfSim = cosineSimilarity(vectorizeTFIDF(text1), vectorizeTFIDF(text2));
        double sbertSim = cosineSimilarity(vectorizeSBERT(text1), vectorizeSBERT(text2));
        double hybridSimilarity = alpha * tfidfSim + (1 - alpha) * sbertSim;
        System.out.println("corpus user : "+text1+",\ncorpus ressource : "+text2+",\ntfidfSim : "+tfidfSim+",\nsbertSim : "+sbertSim+",\nhybridSimilarity : "+hybridSimilarity);
        return hybridSimilarity;
    }
}
