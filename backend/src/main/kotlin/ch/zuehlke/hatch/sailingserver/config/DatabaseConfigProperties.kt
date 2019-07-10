package ch.zuehlke.hatch.sailingserver.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "eventjolle.db")
class DatabaseConfigProperties {

    lateinit var username: String
    lateinit var host: String
    lateinit var port: String
    lateinit var password: String
    lateinit var databaseName: String
}
