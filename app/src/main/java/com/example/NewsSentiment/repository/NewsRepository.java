package com.example.NewsSentiment.repository;

import com.example.NewsSentiment.api.NewsApiService;
import com.example.NewsSentiment.api.RetrofitInstance;
import com.example.NewsSentiment.data.NewsResponse;

import retrofit2.Call;

public class NewsRepository {

    // The API service instance for making network requests
    private final NewsApiService apiService;

    // Constructor to initialize the apiService by creating an instance from Retrofit
    public NewsRepository() {
        // RetrofitInstance is a class that manages the Retrofit configuration (e.g., base URL, converter factory)
        // The apiService is initialized using the Retrofit instance to perform network requests
        apiService = RetrofitInstance.getRetrofitInstance().create(NewsApiService.class);
    }

    public Call<NewsResponse> getTopHeadlines(String country, String apiKey) {
        // Makes a network request to fetch top headlines from the News API
        return apiService.getTopHeadlines(country, apiKey);
    }
}
