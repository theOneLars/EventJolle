package ch.zuehlke.hatch.sailingserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class SailingServerApplication

fun main(args: Array<String>) {
    runApplication<SailingServerApplication>(*args)
}

