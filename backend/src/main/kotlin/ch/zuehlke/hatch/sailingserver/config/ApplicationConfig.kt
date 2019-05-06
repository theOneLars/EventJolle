package ch.zuehlke.hatch.sailingserver.config

import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {

    @Bean
    fun sailingEventStoreDatabase(): MongoDatabase {
        val mongoClient = MongoClients.create("mongodb://localhost");
        return mongoClient.getDatabase("sailing-event-store");
    }

    companion object {
        val COLLECTION_NAME_EVENTS = "events";
    }
}
