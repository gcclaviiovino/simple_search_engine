fun findPerson(dataset: List<Entry>) {
    println("Enter search query:")
    val query = readlnOrNull()
    val searchResult = dataset.find{ it.content.contains("$query", ignoreCase = true) }

    if (searchResult != null) {
        println(formatListOfStrings(searchResult.content))
    } else {
        println("Not found")
    }
}

fun printPeople(dataset: List<Entry>) {
    for (entry in dataset) {
        println(formatListOfStrings(entry.content))
    }
}