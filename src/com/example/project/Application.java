package com.example.project;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class Application {
	public static void main (String[] args){
		System.out.println("hellowolrd");
		
		AlphaVantageApiClient client = new AlphaVantageApiClient(new Producer());

        // Create a ScheduledExecutorService to fetch data every minute
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        long initialDelay = 0; // Initial delay in milliseconds
        long period = 1; // Period in minutes
        executor.scheduleAtFixedRate(() -> client.fetchData(), initialDelay, period * 60 * 1000, TimeUnit.MILLISECONDS);
        
        long shutdownDelay = 20;
        executor.schedule(() -> {
            System.out.println("Shutting down the executor...");
            executor.shutdown();
        }, shutdownDelay, TimeUnit.MINUTES);
		
	}

}
