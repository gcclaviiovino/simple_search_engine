package org.microservice
import org.springframework.stereotype.Repository
import java.io.File
import kotlin.lazy

@Repository
class DatasetRepository: EntryInterface {
    // Loading entries from dataset
    private val entries : MutableList<Entry> by lazy {
        loadDataFromFile().toMutableList()
    }

    // Functions for search queries
    override fun findAll(): List<Entry> {
        return entries
    }

    override fun findById(id: Int): Entry? {
        return entries.find { it.id == id }
    }

    // Building Entry list from data file
    private fun loadDataFromFile(): List<Entry> {
        val inputStream = javaClass.classLoader.getResourceAsStream("dataset.txt")
            ?: throw IllegalStateException("Data file dataset.txt not found in resources.")

        return inputStream.bufferedReader().useLines { lines ->
            lines.mapIndexed { index, line ->
                val words = line.split("\\s+".toRegex()).filter { it.isNotBlank() }
                Entry(id = index, content = words)
            }.toList()
        }
    }

    // Triggered on upload data request
    fun saveData(text: String) : Result<Entry?> {
        if (text.isBlank()) return Result.failure(IllegalStateException("Text cannot be blank"))
        val newId = entries.size
        val newWords = text.split("\\s+".toRegex()).filter { it.isNotBlank() }
        val newEntry = Entry(id= newId, content= newWords)

        entries.add(newEntry)
        return Result.success(newEntry)
    }
}