package ch.zuehlke.hatch.sailingserver.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration

@Configuration
class DatabaseConfig(val databaseConfigProperties: DatabaseConfigProperties) : AbstractR2dbcConfiguration() {


    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(databaseConfigProperties.host)
                        .port(databaseConfigProperties.port.toInt())
                        .username(databaseConfigProperties.username)
                        .password(databaseConfigProperties.password)
                        .database(databaseConfigProperties.databaseName)
                        .build())
    }
}
