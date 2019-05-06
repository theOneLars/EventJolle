package ch.zuehlke.hatch.sailingserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SailingServerApplication

fun main(args: Array<String>) {
    runApplication<SailingServerApplication>(*args)
}

