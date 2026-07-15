import kotlin.collections.fold

fun findPerson(dataset: List<Entry>, indexedDataset: Map<String, Set<Int>>) {
    var filter = displayConditions()
    while (filter == SearchCondition.ERROR) {
        println("ERROR: unknown filter. Please try again. ")
        filter = displayConditions()
    }

    println("Enter search query:")
    val query = readlnOrNull()
    val cleanQueryWords = query?.lowercase()?.split(" ")
    val wordIds: List<Set<Int>>? = cleanQueryWords?.map { word ->
        indexedDataset[word] ?: emptySet()
    }

    val match = filterListOfStrings(dataset, wordIds, filter)
    if (match.isEmpty()) {
        println("No match found.")
        return
    }
    for (entry in match) {
        println(formatListOfStrings(entry.content))
    }
}

fun printPeople(dataset: List<Entry>) {
    for (entry in dataset) {
        println(formatListOfStrings(entry.content))
    }
}

fun displayConditions(): SearchCondition {
    println("Define search filter (if not specified, ALL filter will be assumed):\n" +
        "ALL (i.e. match lines containing every single query word)\n" +
        "ANY (i.e. match lines containing at least one query word)\n" +
        "NONE (i.e. match lines that do not contain any of the query words)\n"
    )
    println("${SearchCondition.entries.toTypedArray()}")
    val choice = readlnOrNull()
    when(choice) {
        "ALL" -> return SearchCondition.ALL
        "ANY" -> return SearchCondition.ANY
        "NONE" -> return SearchCondition.NONE
        "" -> return SearchCondition.ALL
        else -> return SearchCondition.ERROR
    }
}

fun filterListOfStrings(
    dataset: List<Entry>,
    wordIds: List<Set<Int>>?,
    filter: SearchCondition
): List<Entry> {
    val matchResults: Set<Int> = when (filter) {
        SearchCondition.ALL -> {
            if (wordIds.isNullOrEmpty() || wordIds.any { it.isEmpty() }) {
                emptySet()
            } else {
                wordIds.reduce { acc, set -> acc intersect set }
            }
        }

        SearchCondition.ANY -> {
            if (wordIds.isNullOrEmpty()) {
                emptySet()
            } else {
                wordIds.reduce { acc, set -> acc union set }
            }
        }

        SearchCondition.NONE -> {
            val allIds = dataset.mapNotNull { it.id }.toSet()
            val excludedIds = wordIds?.fold(emptySet<Int>()) { acc, set ->
                acc union set
            } ?: emptySet()

            allIds subtract excludedIds
        }

        SearchCondition.ERROR -> {
            println("ERROR: something went wrong. Please try again. ")
            return emptyList()
        }
    }

    return dataset.filter { entry ->
        entry.id in matchResults
    }
}