data class Entry(val id: Int?, val content: List<String>)

enum class SearchCondition(val value: String) {
    ALL("ALL"), ANY("ANY"), NONE("NONE"), ERROR("ERROR")
}

fun getVals( inputStrings: List<String> ): List<Entry> {
    return inputStrings.mapIndexed { i, stringElement ->
        Entry(i, stringElement.split(' '))
    }
}

fun Iterable<String>.contains(element: String, ignoreCase: Boolean): Boolean {
    return this.any { it.equals(element, ignoreCase) }
}

fun formatListOfStrings( inputStrings: List<String> ): String {
    return inputStrings.toString()
        .replace(",", "")
        .replace("[", "")
        .replace("]", "")
}

fun getData(): List<Entry> {
    println("Enter number of rows:")
    val rows = readlnOrNull()?.toIntOrNull() ?: return emptyList()
    val inputRows = mutableListOf<String>()
    for (i in 0 until rows) {
        val line = readlnOrNull() ?: break
        inputRows.add(line)
    }
    return getVals(inputRows)
}

fun displayMenu(): Int? {
    println("1. Search in dataset\n" +
            "2. Print full dataset\n" +
            "0. Exit")
    return readlnOrNull()?.toIntOrNull()
}

fun List<Entry>.rowsEmpty(): Boolean {
    return this.any { entry ->
        entry.content.isEmpty() || entry.content.any { word -> word.isBlank() }
    }
}

fun getMappedValues(dataset: List<Entry>): Map<String, Set<Int>> {
    val indexedMap = mutableMapOf<String, MutableSet<Int>>()
    for (entry in dataset) {
        val newId = entry.id ?: continue
        for (word in entry.content) {
            val newWord = word.lowercase()
            if (!indexedMap.containsKey(newWord)) {
                indexedMap[newWord] = mutableSetOf()
            }
            indexedMap[newWord]?.add(newId)
        }
    }
    return indexedMap
}