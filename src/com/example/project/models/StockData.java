package com.example.project.models;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockData {
		@JsonProperty("Meta Data")
		private MetaData metaData;
		@JsonProperty("Time Series (1min)")
		private Map<String, TimeSeriesEntry> data;
		public MetaData getMetaData() {
			return metaData;
		}
		public void setMetaData(MetaData metaData) {
			this.metaData = metaData;
		}
		public Map<String, TimeSeriesEntry> getData() {
			return data;
		}
		public void setData(Map<String, TimeSeriesEntry> data) {
			this.data = data;
		}
		
		
}
