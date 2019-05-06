package ch.zuehlke.hatch.sailingserver.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    public static final String COLLECTION_NAME_EVENTS = "events";

    @Bean
    public MongoDatabase sailingEventStoreDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost");
        return mongoClient.getDatabase("sailing-event-store");
    }
}
