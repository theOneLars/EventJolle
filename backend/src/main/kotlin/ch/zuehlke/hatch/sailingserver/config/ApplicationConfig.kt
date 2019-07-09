package ch.zuehlke.hatch.sailingserver.config

import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration

@Configuration
class ApplicationConfig: AbstractR2dbcConfiguration() {

    @Bean
    fun sailingEventStoreDatabase(): MongoDatabase {
        val mongoClient = MongoClients.create("mongodb://localhost:27017");
        return mongoClient.getDatabase("sailing-event-store");
    }

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("127.0.0.1")
                        .port(5432)
                        .username("postgres")
                        .password("admin")
                        .database("sailing-events")
                        .build())
    }
}
