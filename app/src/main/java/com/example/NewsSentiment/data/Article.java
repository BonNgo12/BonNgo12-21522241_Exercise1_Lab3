package com.example.NewsSentiment.data;

import android.content.Context;

import com.example.NewsSentiment.utils.TextClassifier;

import java.util.Random;

public class Article {

    // Instance variables to hold the article data
    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;
    private String category;
    private Sentiment sentiment;
    private static final Random random = new Random(); // Random number generator for adding randomness to sentiment score

    // Sentiment class to represent the positive and negative sentiment scores
    public static class Sentiment {
        private float positive; // Positive sentiment score
        private float negative; // Negative sentiment score

        // Constructor to initialize positive and negative sentiment scores
        public Sentiment(float positive, float negative) {
            this.positive = positive;
            this.negative = negative;
        }

        // Getters for positive and negative scores
        public float getPositive() { return positive; }
        public float getNegative() { return negative; }

        // Method to determine if the sentiment is positive based on a threshold
        public boolean isPositive() {
            return positive > 0.7f; // If the positive score is greater than 0.7, it's considered positive
        }

        // Override toString method to return a string representing the sentiment
        @Override
        public String toString() {
            return isPositive() ? "Positive" : "Negative"; // Return "Positive" or "Negative" based on the sentiment
        }
    }

    // Method to classify the sentiment of the article's content
    public void classifyContent(Context context, TextClassifier classifier) {
        try {
            // Combine title, description, and content to form the text to classify
            String textToClassify = (title != null ? title : "") + " " +
                    (description != null ? description : "") + " " +
                    (content != null ? content : "");

            // For simplicity, we use a heuristic to calculate positive/negative sentiment
            float positiveScore = calculatePositiveScore(textToClassify); // Calculate positive score
            float negativeScore = 1.0f - positiveScore; // Negative score is the complement of the positive score

            // Set the sentiment and category based on the scores
            this.sentiment = new Sentiment(positiveScore, negativeScore);
            this.category = sentiment.isPositive() ? "Positive" : "Negative"; // Set the category as "Positive" or "Negative"
        } catch (Exception e) {
            e.printStackTrace(); // Catch any exceptions that occur
            // Default fallback sentiment if classification fails
            this.sentiment = new Sentiment(0.5f, 0.5f); // Neutral sentiment
            this.category = "Neutral"; // Neutral category
        }
    }

    // Helper method to calculate the positive sentiment score of a given text
    private float calculatePositiveScore(String text) {
        if (text == null || text.isEmpty()) {
            return 0.5f; // If the text is empty, return a neutral score
        }

        String lowerText = text.toLowerCase(); // Convert text to lowercase for case-insensitive comparison

        // Simple word lists for demo purposes
        String[] positiveWords = {"good", "great", "excellent", "amazing", "happy", "positive",
                "success", "beautiful", "love", "best", "triumph"};
        String[] negativeWords = {"bad", "terrible", "awful", "hate", "negative", "sad",
                "fail", "poor", "worst", "problem", "disaster"};

        // Count occurrences of positive and negative words in the text
        int positiveCount = 0;
        for (String word : positiveWords) {
            if (lowerText.contains(word)) {
                positiveCount++; // Increment the positive count if the word is found
            }
        }

        int negativeCount = 0;
        for (String word : negativeWords) {
            if (lowerText.contains(word)) {
                negativeCount++; // Increment the negative count if the word is found
            }
        }

        if (positiveCount == 0 && negativeCount == 0) {
            // If no positive or negative words are found, return a random score between 0.4 and 1.0
            return 0.4f + random.nextFloat() * 0.6f;
        }

        float totalWords = positiveCount + negativeCount; // Total number of positive and negative words found
        float score = (float) positiveCount / totalWords; // Calculate the score based on positive word count

        // Add some randomness to the score to avoid identical scores
        score = Math.min(1.0f, Math.max(0.0f, score + (random.nextFloat() * 0.2f - 0.1f)));

        return score; // Return the calculated sentiment score
    }

    // Getters and setters for the article properties
    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUrlToImage() { return urlToImage; }
    public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }

    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Sentiment getSentiment() { return sentiment; }
    public void setSentiment(Sentiment sentiment) { this.sentiment = sentiment; }

    // Helper method to check if the article is positive
    public boolean isPositive() {
        return sentiment != null && sentiment.isPositive(); // Return true if the sentiment is positive
    }

    // Method to return the formatted sentiment text for display
    public String getSentimentText() {
        if (sentiment == null) {
            return "Unknown"; // If sentiment is not available, return "Unknown"
        }
        // Return sentiment as "Positive" or "Negative" along with the positive score percentage
        return sentiment.toString() + " (" + String.format("%.1f", sentiment.getPositive() * 100) + "%)";
    }
}
