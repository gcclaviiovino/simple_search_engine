data class Entry(val id: Int?, val content: List<String>)

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