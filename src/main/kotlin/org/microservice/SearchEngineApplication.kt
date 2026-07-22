package org.microservice
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

/*
    @brief: Spring Boot application, starting point for program execution.
 */

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class SearchEngineApplication
fun main(args: Array<String>) {
    runApplication<SearchEngineApplication>(*args)
}