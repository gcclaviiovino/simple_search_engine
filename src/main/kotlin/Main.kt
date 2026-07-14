fun main() {
    val dataset = getVals()
    println("Query a specific index")
    val query = readlnOrNull()
    val ret = dataset.find { it.code == query }

    if (ret != null && ret.id != null) {
        println(ret.id)
    } else {
        println("Not found")
    }
}

fun getVals(): List<Entry> {
    println("Provide a dataset (i.e. series of index-related words, like 'first', separated by a space)")
    val input = readlnOrNull() ?: return emptyList()

    return input.split(' ')
        .filter { it.isNotEmpty() } // Handles accidental double spaces
        .map { word ->
            val cleanWord = word.lowercase()
            val indexValue = ordinalMap.entries.find { it.value == cleanWord }?.key
            Entry(indexValue, word)
        }
}

