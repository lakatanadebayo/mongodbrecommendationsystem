package com.mongodbrecommendationsystem.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resources")
@Data
public class Resource {
    private String name;
    private String url;
    private String type;
}
