fun findPerson(dataset: List<Entry>, indexedDataset: Map<String, Set<Int>>) {
    println("Enter search query:")
    val query = readlnOrNull()
    val cleanQuery = query?.lowercase()
    val match = indexedDataset[cleanQuery]

    if (match.isNullOrEmpty() || cleanQuery == null) {
        println("No match found.")
        return
    }

    match.forEach { id ->
        val dataRow = dataset.find { it.id == id }
        if (dataRow != null) {
            println(formatListOfStrings(dataRow.content))
        }
    }
}

fun printPeople(dataset: List<Entry>) {
    for (entry in dataset) {
        println(formatListOfStrings(entry.content))
    }
}