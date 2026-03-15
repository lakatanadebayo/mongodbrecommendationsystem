package com.mongodbrecommendationsystem.service;

public interface VectorisationService {
    double[] vectorizeTFIDF(String text);
    double[] vectorizeSBERT(String text);
    double cosineSimilarity(double[] v1, double[] v2);
    double hybridSimilarity(String text1, String text2, double alpha);
}
