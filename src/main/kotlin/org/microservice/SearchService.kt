package org.microservice
import org.springframework.stereotype.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Service
class SearchService (
    private val entryInterface: EntryInterface
) {
    private var indexedDataset: Map<String, Set<Int>> = getMappedValues()

    // Returns dataset as an inverted index map of values
    private fun getMappedValues(): Map<String, Set<Int>> {
        //  Convert dataset from string to List of Entry objects
        val dataset = entryInterface.findAll()
        val indexedDataset = mutableMapOf<String, MutableSet<Int>>()
        // Map list of entries to build inverted index map
        for (entry in dataset) {
            val newId = entry.id ?: continue
            for (word in entry.content) {
                // Turn all words to lowercase for consistency and search efficiency
                val newWord = word.lowercase()
                if (!indexedDataset.containsKey(newWord)) {
                    indexedDataset[newWord] = mutableSetOf()
                }
                indexedDataset[newWord]?.add(newId)
            }
        }
        return indexedDataset
    }

    // Returns list of Entry objects that meet query requirements
    fun findPerson(query: String, filter: SearchCondition, ): List<Entry> {
        val cleanQueryWords = query.lowercase().split(" ")
        if (cleanQueryWords.isEmpty()) return emptyList<Entry>()

        // Create a list of ids where query word/s can be found
        val wordIds: List<Set<Int>> = cleanQueryWords.map { word ->
            this.indexedDataset[word] ?: emptySet<Int>()
        }

        // Define result based on query condition
        val matchResults: Set<Int> = when (filter) {
            // Only objects matching ALL query terms
            SearchCondition.ALL -> {
                if (wordIds.isNullOrEmpty() || wordIds.any { it.isEmpty() }) {
                    emptySet()
                } else {
                    wordIds.reduce { acc, set -> acc intersect set }
                }
            }

            // ANY object matching any term in query
            SearchCondition.ANY -> {
                cleanQueryWords
                    .flatMap { word -> indexedDataset[word] ?: emptySet() }
                    .toSet()
            }

            // All objects where NONE of query terms are included
            SearchCondition.NONE -> {
                val allIds = entryInterface.findAll().mapNotNull { it.id }.toSet()
                val excludedIds = wordIds.fold(mutableSetOf<Int>()) { acc, set ->
                    acc.apply { addAll(set) }
                }

                allIds subtract excludedIds
            }

            // Error triggered by incorrect condition
            SearchCondition.ERROR -> {
                return emptyList()
            }
        }

        // Return entries with ids included in result list
        return entryInterface.findAll().filter { entry ->
            entry.id in matchResults
        }
    }

    // Return all entries, for search endpoint with no query terms
    fun findAll() : List<Entry> {
        return entryInterface.findAll()
    }

    // Rebuilds internal mapped version of dataset during program execution,
    // triggered on successful data upload call (i.e. when new data is provided)
    suspend fun rebuildIndex() = withContext(Dispatchers.Default) {
        indexedDataset = getMappedValues()
    }
}
