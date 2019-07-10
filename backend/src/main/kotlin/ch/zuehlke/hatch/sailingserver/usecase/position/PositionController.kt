package ch.zuehlke.hatch.sailingserver.usecase.position

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/position")
class PositionController(val positionUseCase: PositionUseCase) {

    @GetMapping(path = ["10years"])
    fun getLast10Years() = positionUseCase.getPositionsOfLast10Years()

}

