package org.microservice
import org.springframework.stereotype.Repository
import java.io.File
import kotlin.lazy

@Repository
class DatasetRepository: EntryInterface {
    private val entries : MutableList<Entry> by lazy {
        loadDataFromFile().toMutableList()
    }

    override fun findAll(): List<Entry> {
        return entries
    }

    override fun findById(id: Int): Entry? {
        return entries.find { it.id == id }
    }

    private fun loadDataFromFile(): List<Entry> {
        val file = File("names.txt")
        if (!file.exists()) {
            throw IllegalStateException("Data file names.txt not found at: ${file.absolutePath}")
        }

        return file.readLines()
            .mapIndexed { index, line ->
                val words = line.split("\\s+".toRegex()).filter { it.isNotBlank() }
                Entry(id = index, content = words)
            }
    }

    fun saveData(text: String) : Entry? {
        val newId = entries.size
        val newWords = text.split("\\s+".toRegex()).filter { it.isNotBlank() }
        val newEntry = Entry(id= newId, content= newWords)

        entries.add(newEntry)
        return newEntry
    }
}