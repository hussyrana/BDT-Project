package com.example.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlphaVantageApiClient {
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private Producer producer;
    
    public AlphaVantageApiClient(Producer producer){
    	this.producer = producer;
    }

    public void fetchData() {
        try {
            String symbol = "AAPL"; // Replace with your desired symbol
            String apiUrl = BASE_URL + "?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=1min&apikey=" + API_KEY;

            // Create a URL object
            URL url = new URL(apiUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                producer.send(response.toString());

                reader.close();

                // Process the response here
                String responseData = response.toString();
                System.out.println("Response Data:\n" + responseData);
            } else {
                System.err.println("HTTP Request Failed with Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

