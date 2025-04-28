package com.example.NewsSentiment.data;

import java.util.List;

public class NewsResponse {

    // Instance variables to hold the status of the response, total results, and the list of articles
    private String status;           // Status of the news response (e.g., "ok", "error")
    private int totalResults;        // Total number of results/articles in the response
    private List<Article> articles;  // List of articles returned by the API

    // Getter and setter methods for the status field
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter and setter methods for the totalResults field
    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    // Getter and setter methods for the articles field
    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
