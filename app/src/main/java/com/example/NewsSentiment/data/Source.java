package com.example.NewsSentiment.data;

public class Source {

    // Instance variables to hold the id and name of the source
    private String id;   // The unique identifier for the source (e.g., "bbc-news")
    private String name; // The name of the source (e.g., "BBC News")

    // Getter method for the id field
    public String getId() {
        return id;
    }

    // Setter method for the id field
    public void setId(String id) {
        this.id = id;
    }

    // Getter method for the name field
    public String getName() {
        return name;
    }

    // Setter method for the name field
    public void setName(String name) {
        this.name = name;
    }
}
