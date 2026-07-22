package org.microservice

/*
    @brief: Utility functions for class Entry.
 */

fun List<String>.formatListOfStrings(): String {
    return this.toString()
        .replace(",", "")
        .replace("[", "")
        .replace("]", "")
}
