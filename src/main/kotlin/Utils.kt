data class Entry(val id: Int?, val content: List<String>)

enum class Option() {
    FIND, PRINT, EXIT
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
    println("1. Find a person\n" +
            "2. Print all people\n" +
            "0. Exit")
    return readlnOrNull()?.toIntOrNull()
}

fun List<Entry>.rowsEmpty(): Boolean {
    return this.any { entry ->
        entry.content.isEmpty() || entry.content.any { word -> word.isBlank() }
    }
}