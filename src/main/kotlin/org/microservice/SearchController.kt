package org.microservice
import ch.qos.logback.core.model.Model
import org.apache.coyote.Response
import org.springframework.boot.autoconfigure.condition.SearchStrategy
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api")
class SearchController(private val searchService: SearchService, private val datasetRepository: DatasetRepository) {
    @GetMapping("/search-all")
    fun searchAll() : List<Entry> {
        return searchService.findAll()
    }
    @GetMapping("/search-filtered")
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

    @PostMapping("/upload")
    suspend fun uploadData(
        @RequestBody dataRequest: DataRequest
    ) : ResponseEntity<Map<String, String>> {
        if (dataRequest.text.isBlank()) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Text content cannot be empty."))
        }
        return datasetRepository.saveData(dataRequest.text).fold(
            onSuccess = { newEntry ->
                searchService.rebuildIndex()

                ResponseEntity.ok(mapOf(
                    "message" to "Document added successfully",
                    "id" to newEntry?.id.toString()
                ))
            },
            onFailure = { error ->
                ResponseEntity.badRequest().body(mapOf(
                    "error" to (error.message ?: "Invalid request content")
                ))
            }
        )
    }
}
