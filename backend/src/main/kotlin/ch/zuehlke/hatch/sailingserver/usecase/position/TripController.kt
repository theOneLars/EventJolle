package ch.zuehlke.hatch.sailingserver.usecase.position

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/trip")
class TripController(
        private val viewHistoricTrips: ViewHistoricTrips
) {

    @GetMapping
    fun getHistoricTrips(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            from: LocalDate,
            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            to: LocalDate
    ) = this.viewHistoricTrips.ofRange(from, to)

    @GetMapping("/tillToday")
    fun getHistoricTripsTillToday(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            from: LocalDate
    ) = this.viewHistoricTrips.tillToday(from)
}