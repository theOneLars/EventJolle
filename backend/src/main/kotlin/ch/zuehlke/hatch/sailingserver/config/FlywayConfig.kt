package ch.zuehlke.hatch.sailingserver.config

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class FlywayConfig(private val databaseConfigProperties: DatabaseConfigProperties) {


    @PostConstruct
    fun migrateFlyway() {

        val flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://${databaseConfigProperties.host}:${databaseConfigProperties.port}/${databaseConfigProperties.databaseName}",
                        databaseConfigProperties.username,
                        databaseConfigProperties.password).load()
        flyway.migrate()

    }
}