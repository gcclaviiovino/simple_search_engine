package org.microservice

fun List<String>.formatListOfStrings(): String {
    return this.toString()
        .replace(",", "")
        .replace("[", "")
        .replace("]", "")
}
