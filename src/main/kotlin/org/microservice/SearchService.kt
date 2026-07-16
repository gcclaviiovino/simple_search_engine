package org.microservice
import org.springframework.stereotype.Service

@Service
class SearchService (
    private val entryInterface: EntryInterface
) {
    private val indexedDataset: Map<String, Set<Int>> by lazy {
        getMappedValues(entryInterface.findAll())
    }

    private fun getMappedValues(dataset: List<Entry>): Map<String, Set<Int>> {
        val indexedDataset = mutableMapOf<String, MutableSet<Int>>()
        for (entry in dataset) {
            val newId = entry.id ?: continue
            for (word in entry.content) {
                val newWord = word.lowercase()
                if (!indexedDataset.containsKey(newWord)) {
                    indexedDataset[newWord] = mutableSetOf()
                }
                indexedDataset[newWord]?.add(newId)
            }
        }
        return indexedDataset
    }

    private fun Iterable<String>.contains(element: String, ignoreCase: Boolean): Boolean {
        return this.any { it.equals(element, ignoreCase) }
    }

    private fun formatListOfStrings( inputStrings: List<String> ): String {
        return inputStrings.toString()
            .replace(",", "")
            .replace("[", "")
            .replace("]", "")
    }

    fun findPerson(query: String, filter: SearchCondition, ): List<Entry> {
        val cleanQueryWords = query.lowercase().split(" ")
        if (cleanQueryWords.isEmpty()) return emptyList<Entry>()
        val wordIds: List<Set<Int>> = cleanQueryWords.map { word ->
            this.indexedDataset[word] ?: emptySet<Int>()
        }

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
                val allIds = entryInterface.findAll().mapNotNull { it.id }.toSet()
                val excludedIds = wordIds.fold(emptySet<Int>()) { acc, set ->
                    acc union set
                }

                allIds subtract excludedIds
            }

            SearchCondition.ERROR -> {
                return emptyList()
            }
        }

        return entryInterface.findAll().filter { entry ->
            entry.id in matchResults
        }
    }

    fun findAll() : List<Entry> {
        return entryInterface.findAll()
    }
}
