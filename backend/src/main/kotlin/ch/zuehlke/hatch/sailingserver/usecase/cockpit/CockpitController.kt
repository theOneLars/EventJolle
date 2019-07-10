package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cockpit")
class CockpitController(val cockpitUseCase: CockpitUseCase) {

    @GetMapping(path = [""], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getCockpit() = cockpitUseCase.getCockpit()
}

