package org.microservice

enum class SearchCondition(val value: String) {
    ALL("ALL"), ANY("ANY"), NONE("NONE"), ERROR("ERROR")
}