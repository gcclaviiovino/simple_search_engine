package org.microservice
import ch.qos.logback.core.model.Model
import org.apache.coyote.Response
import org.springframework.boot.autoconfigure.condition.SearchStrategy
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/search")
class SearchController(private val searchService: SearchService) {
    @GetMapping("/all")
    fun searchAll() : List<Entry> {
        return searchService.findAll()
    }
    @GetMapping("/filtered")
    fun searchFiltered(
        @RequestParam query: String,
        @RequestParam(defaultValue = "ALL") strategy: SearchCondition
    ): List<Entry> {
        if (query.isBlank()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Query cannot be blank"
            )
        }

        if (strategy == SearchCondition.ERROR) {
            throw ResponseStatusException (
                HttpStatus.BAD_REQUEST,
                "Invalid search filter."
            )
        }

        return searchService.findPerson(query, strategy)
    }
}
