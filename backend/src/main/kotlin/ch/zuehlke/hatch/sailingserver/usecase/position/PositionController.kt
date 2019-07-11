package ch.zuehlke.hatch.sailingserver.usecase.position

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/position")
class PositionController(
        val positionUseCase: PositionUseCase
) {

    @GetMapping(path = ["10years"])
    fun getLast10Years() = positionUseCase.getPositionsOfLast10Years()

    @GetMapping(path = ["today"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getToday() = positionUseCase.getTodayLivePositions()

    @GetMapping(path = ["byDate"])
    fun getForDate(@RequestParam("date")
                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                   date: LocalDate) = positionUseCase.getPositionsOfDate(date)
}

