package com.example.NewsSentiment.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class TextClassifier {
    private static final String TAG = "TextClassifier"; // Log tag for debugging
    private static final String MODEL_PATH = "text-classification.tflite"; // Path to the TensorFlow Lite model
    private static final int MAX_SEQUENCE_LENGTH = 256; // Max length of input text for tokenization

    private Interpreter tflite; // TensorFlow Lite interpreter
    private Map<String, Integer> wordIndex; // Map for storing word-to-index mapping (vocabulary)

    public TextClassifier(Context context) {
        try {
            // Load the model from assets and initialize the interpreter
            tflite = new Interpreter(loadModelFile(context));

            // Initialize word index (you'll need to load it from a file or hard-code it)
            wordIndex = new HashMap<>();
            // Here you could load the word index from a resource file or define it based on your model
        } catch (IOException e) {
            // Log an error if the model can't be loaded
            Log.e(TAG, "Error loading model", e);
        }
    }

    // Loads the TensorFlow Lite model file from the assets directory
    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(MODEL_PATH); // Open the model file
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        // Return a mapped byte buffer for the model file
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Classifies a given text using the loaded TensorFlow Lite model
    public float[] classify(String text) {
        // Preprocess the text (tokenize into words and convert to indices)
        int[] inputSequence = tokenize(text);

        // Prepare input buffer with the tokenized sequence (padding to a fixed length if necessary)
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(MAX_SEQUENCE_LENGTH * 4);
        for (int i = 0; i < MAX_SEQUENCE_LENGTH; i++) {
            if (i < inputSequence.length) {
                inputBuffer.putInt(inputSequence[i]); // Add token to input buffer
            } else {
                inputBuffer.putInt(0); // Padding with zeros for sequences shorter than MAX_SEQUENCE_LENGTH
            }
        }
        inputBuffer.rewind(); // Reset position to the beginning for the TensorFlow Lite model

        // Prepare output buffer to store the prediction (assuming binary classification)
        float[][] outputBuffer = new float[1][1]; // Adjust the size depending on the model's output

        // Run inference with the model
        tflite.run(inputBuffer, outputBuffer);

        // Return the classification result
        return outputBuffer[0];
    }

    // Tokenizes the input text based on the word index (vocabulary)
    private int[] tokenize(String text) {
        // Split the text into words
        String[] words = text.toLowerCase().split("\\s+");
        int[] tokens = new int[words.length];

        // Map each word to an integer index using the word index (vocabulary)
        for (int i = 0; i < words.length; i++) {
            Integer index = wordIndex.get(words[i]);
            tokens[i] = (index != null) ? index : 0; // If the word is unknown, use index 0
        }

        // Return the tokenized sequence
        return tokens;
    }
}
