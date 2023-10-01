package com.example.project;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.hadoop.hbase.util.Bytes;

import com.example.project.models.StockData;
import com.example.project.models.TimeSeriesEntry;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.TableName;

public class KafkaSparkHbase {
	private static final String TABLE_NAME = "stock_market";

	public static void main(String[] args) throws InterruptedException {

		SparkConf sparkConf = new SparkConf().setAppName(
				"KafkaStreamingExample").setMaster("local[*]");
		JavaStreamingContext streamingContext = new JavaStreamingContext(
				sparkConf, Durations.seconds(10));

		// Kafka configuration
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", "localhost:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class.getName());
		kafkaParams.put("value.deserializer",
				StringDeserializer.class.getName());
		kafkaParams.put("group.id", "my-consumer-group");
		kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		Set<String> topics = Collections.singleton("project-topic");

		// Create a Kafka input DStream
		JavaInputDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtils
				.createDirectStream(streamingContext,
						LocationStrategies.PreferConsistent(),
						ConsumerStrategies.Subscribe(topics, kafkaParams));

		// Hbase Configs

		
		ObjectMapper objectMapper = new ObjectMapper();

		// Process the Kafka data as needed
		kafkaStream.foreachRDD((rdd, time) -> {
			rdd.foreachPartition(records -> {
				
				Configuration config = HBaseConfiguration.create();
				Connection connection = ConnectionFactory.createConnection(config);
				TableName tableName = TableName.valueOf(TABLE_NAME);
				Table table = connection.getTable(tableName);
				
				while (records.hasNext()) {
					ConsumerRecord<String, String> record = records.next();
					// Process the Kafka message
					StockData stockData = objectMapper.readValue(
							record.value(), StockData.class);
					for (String key : stockData.getData().keySet()) {
						System.out.println(stockData.getData().get(key)
								.toString());
						saveToHbase(key, stockData.getData().get(key),
								table);

					}
					connection.close();
					table.close();
					// System.out.println("Received message: " +
					// stockData.getMetaData().toString());
				}
			});
		});

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Stopping the streaming context...");
				streamingContext.stop(true, true); // Stop gracefully with
													// shutdown hooks
			}
			 }, 1200000);
//		}, 100000);

		streamingContext.start();
		streamingContext.awaitTermination();
	}

	public static void saveToHbase(String key, TimeSeriesEntry entry,
			Table table) {
		Put put = new Put(Bytes.toBytes(key));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("open"),
				Bytes.toBytes(entry.getOpen()));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("high"),
				Bytes.toBytes(entry.getHigh()));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("low"),
				Bytes.toBytes(entry.getLow()));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("close"),
				Bytes.toBytes(entry.getClose()));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("volume"),
				Bytes.toBytes(entry.getVolume()));
		try {
			table.put(put);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
