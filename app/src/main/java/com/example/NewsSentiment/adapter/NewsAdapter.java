package com.example.NewsSentiment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.NewsSentiment.R;
import com.example.NewsSentiment.data.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    // List to store articles to be displayed
    private List<Article> articles;

    // Date format for parsing and displaying date in different formats
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
    private SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

    // Constructor to initialize the adapter with the article list
    public NewsAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each article item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view); // Return a new ViewHolder with the inflated view
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the article at the current position
        Article article = articles.get(position);

        // Set the source name, or display "Unknown Source" if null
        if (article.getSource() != null) {
            holder.sourceName.setText(article.getSource().getName());
        } else {
            holder.sourceName.setText("Unknown Source");
        }

        // Set the article title and description
        holder.title.setText(article.getTitle());
        holder.description.setText(article.getDescription());

        // Try to parse the publication date and format it for display
        try {
            Date date = inputFormat.parse(article.getPublishedAt());
            holder.publishedAt.setText(outputFormat.format(date)); // Display formatted date
        } catch (ParseException e) {
            // If parsing fails, display the raw date string
            holder.publishedAt.setText(article.getPublishedAt());
        }

        // Load the article image with Glide if the URL is not empty
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Glide.with(holder.itemView.getContext()) // Use Glide to load the image
                    .load(article.getUrlToImage()) // Load image from URL
                    .centerCrop() // Crop the image to center it
                    .placeholder(R.drawable.error_image) // Placeholder image while loading
                    .into(holder.articleImage); // Set the image into the ImageView
        } else {
            // If no image URL is provided, set a default placeholder image
            holder.articleImage.setImageResource(R.drawable.error_image);
        }

        // Display sentiment information (positive or negative)
        holder.sentimentText.setText("Sentiment: " + article.getSentimentText());

        // Set the sentiment text color based on whether the article is positive or negative
        if (article.isPositive()) {
            // Green for positive sentiment
            holder.sentimentText.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            // Red for negative sentiment
            holder.sentimentText.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of articles in the list
        return articles.size();
    }

    // ViewHolder class to hold references to the views of each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImage; // Image view to display the article's image
        TextView sourceName; // Text view to display the article's source name
        TextView title; // Text view to display the article's title
        TextView description; // Text view to display the article's description
        TextView publishedAt; // Text view to display the article's published date
        TextView sentimentText; // Text view to display the article's sentiment

        // Constructor for the ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            articleImage = itemView.findViewById(R.id.article_image);
            sourceName = itemView.findViewById(R.id.source_name);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            publishedAt = itemView.findViewById(R.id.published_at);
            sentimentText = itemView.findViewById(R.id.sentiment_text);
        }
    }
}
