package org.microservice

/*
    @brief: SearchCondition enum with respective String value definition.
 */

enum class SearchCondition(val value: String) {
    ALL("ALL"), ANY("ANY"), NONE("NONE"), ERROR("ERROR")
}