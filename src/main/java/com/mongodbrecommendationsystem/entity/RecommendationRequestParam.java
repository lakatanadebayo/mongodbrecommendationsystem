package com.mongodbrecommendationsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecommendationRequestParam {
    private String domain;
    private String level;
    private String language;
}
