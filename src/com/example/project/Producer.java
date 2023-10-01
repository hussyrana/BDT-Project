package com.example.project;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Producer {
	
	private static final KafkaProducer<String, String> producer;
	
	static{
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producer = new KafkaProducer<String, String>(props);
	}
	
	public void send(String data) {
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("project-topic", data);
		producer.send(producerRecord);
	}

}
