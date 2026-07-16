package org.microservice
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class SearchEngineApplication
fun main(args: Array<String>) {
    runApplication<SearchEngineApplication>(*args)
}