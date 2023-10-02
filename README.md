# BDT-Project
There are two applications in the project one is for the recieving the real time data from alpha vantage api which provides the real time streaming data of the stock market and there is a second application that process the data using spark streaming and store it to Hbase.
## Application1
Application one has a class named as Kafka producer that is responsible to publish the data to kafka topic, there is another class names as AlphavantageApi that fetch the data from the api every minutes and use the first class to send that data to kafka topic. And there is a main class to run the application.

## ApplicationProd
This application use the spark streaming client with kafka and spark streaming connector, it will recive the data from kafka consumer and put it to spark streaming to store or process the data with spark, then this application also has the hbase client library to store the recieved stock market data into the hbasetable, there are some model classes in it that are just to map the data to process it and string it to hbase.

## Note
There will be two branches for this repo one will be main for application1 and the other one will be for the application prod.

## Video or Presentation links
Watch the first link first then the second.

https://mum0-my.sharepoint.com/personal/azewelday_miu_edu/_layouts/15/stream.aspx?id=%2Fpersonal%2Fazewelday_miu_edu%2FDocuments%2Fbandicam+2023-10-01+21-03-37-908.mp4&referrer=OfficeHome.Web&referrerScenario=UPLOAD&referrer=Teams.TEAMS-ELECTRON&referrerScenario=p2p_ns-bim&web=11


https://mum0-my.sharepoint.com/personal/azewelday_miu_edu/_layouts/15/stream.aspx?id=%2Fpersonal%2Fazewelday_miu_edu%2FDocuments%2Fbandicam+2023-10-01+21-14-43-201.mp4&referrer=OfficeHome.Web&referrerScenario=UPLOAD&referrer=Teams.TEAMS-ELECTRON&referrerScenario=p2p_ns-bim&web=1



