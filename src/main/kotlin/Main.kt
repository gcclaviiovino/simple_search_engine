fun main() {
    println("Enter number of rows:")
    val rows = readlnOrNull()?.toIntOrNull() ?: return
    val inputRows = mutableListOf<String>()
    for (i in 0 until rows) {
        val line = readlnOrNull() ?: break
        inputRows.add(line)
    }
    val dataset = getVals(inputRows)

    println("Enter search query:")
    val query = readlnOrNull()
    val searchResult = dataset.find{ it.content.contains("$query", ignoreCase = true) }

    if (searchResult != null) {
        println(formatListOfStrings(searchResult.content))
    } else {
        println("Not found")
    }
}

