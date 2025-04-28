package com.example.NewsSentiment.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NewsSentiment.data.Article;
import com.example.NewsSentiment.adapter.NewsAdapter;
import com.example.NewsSentiment.repository.NewsRepository;
import com.example.NewsSentiment.data.NewsResponse;
import com.example.NewsSentiment.R;
import com.example.NewsSentiment.utils.TextClassifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.graphics.Color;

public class MainActivity extends AppCompatActivity {

    // API Key for accessing the news API
    private static final String API_KEY = "d12996ce9c5e4a199501db9e92d0d053";
    private static final String TAG = "MainActivity"; // Tag for logging

    // UI elements
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private TextClassifier textClassifier; // TFLite classifier to analyze article sentiment
    private List<Article> allArticles = new ArrayList<>(); // List to store all fetched articles
    private Button btnAll, btnPositive, btnNegative; // Buttons for filtering articles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the text classifier (TFLite model)
        textClassifier = new TextClassifier(this);

        // Set up RecyclerView for displaying articles
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(new ArrayList<>()); // Initially empty list
        recyclerView.setAdapter(adapter);

        // Initialize buttons for filtering articles
        btnAll = findViewById(R.id.btnAllArticles);
        btnAll.setBackgroundColor(Color.parseColor("#bab6bb"));

        btnPositive = findViewById(R.id.btnPositiveArticles);
        btnPositive.setBackgroundColor(Color.parseColor("#229832"));

        btnNegative = findViewById(R.id.btnNegativeArticles);
        btnNegative.setBackgroundColor(Color.parseColor("#b52929"));
        // Set up button click listeners
        setupFilterButtons();

        // Fetch the latest news articles
        fetchNews();
    }

    // Setup listeners for the filter buttons
    private void setupFilterButtons() {
        // "All Articles" button listener
        btnAll.setOnClickListener(v -> showAllArticles());

        // "Positive Articles" button listener
        btnPositive.setOnClickListener(v -> {
            // Filter articles to only include positive ones based on sentiment analysis
            List<Article> positiveArticles = allArticles.stream()
                    .filter(Article::isPositive)
                    .collect(Collectors.toList());

            updateArticlesList(positiveArticles);
            Toast.makeText(this, "Showing positive articles", Toast.LENGTH_SHORT).show();
        });

        // "Negative Articles" button listener
        btnNegative.setOnClickListener(v -> {
            // Filter articles to only include negative ones based on sentiment analysis
            List<Article> negativeArticles = allArticles.stream()
                    .filter(article -> !article.isPositive())
                    .collect(Collectors.toList());

            updateArticlesList(negativeArticles);
            Toast.makeText(this, "Showing negative articles", Toast.LENGTH_SHORT).show();
        });
    }

    // Show all articles in the RecyclerView
    private void showAllArticles() {
        updateArticlesList(allArticles); // Show the complete list of articles
        Toast.makeText(this, "Showing all articles", Toast.LENGTH_SHORT).show();
    }

    // Update the article list in the RecyclerView with the given list of articles
    private void updateArticlesList(List<Article> articles) {
        adapter = new NewsAdapter(articles); // Create a new adapter with the filtered articles
        recyclerView.setAdapter(adapter); // Update the RecyclerView's adapter
    }

    // Fetch the latest news articles from the News API
    private void fetchNews() {
        NewsRepository repository = new NewsRepository(); // Create a repository to interact with the API
        repository.getTopHeadlines("us", API_KEY).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                // Handle the response from the API
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Status: " + response.body().getStatus());
                    Log.d(TAG, "Total Results: " + response.body().getTotalResults());

                    // Store all the fetched articles
                    allArticles = response.body().getArticles();

                    // Classify the sentiment of each article
                    for (Article article : allArticles) {
                        article.classifyContent(MainActivity.this, textClassifier);
                    }

                    // Display all articles initially
                    showAllArticles();
                } else {
                    // Log any errors and display a message
                    Log.e(TAG, "Response error: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to fetch news", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Log any network errors and display a message
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
