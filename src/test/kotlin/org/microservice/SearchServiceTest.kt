package org.microservice

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchServiceTest {
    private val datasetRepository: DatasetRepository = mockk()
    private lateinit var searchService: SearchService

    private val initialMockEntries = listOf(
        Entry(id = 0, content = listOf("John", "Doe")),
        Entry(id = 1, content = listOf("Jane", "Smith"))
    )

    @BeforeEach
    fun setUp() {
        every { datasetRepository.findAll() } returns initialMockEntries
        searchService = SearchService(datasetRepository)
    }

    @Test
    fun `search with ALL strategy returns correct match`() {
        val results = searchService.findPerson("John", SearchCondition.ALL)

        assertEquals(1, results.size)
        assertEquals(0, results[0].id)
        assertTrue(results[0].content.contains("John"))
    }

    @Test
    fun `search with NONE strategy excludes matching entry`() {
        val results = searchService.findPerson("John", SearchCondition.NONE)

        assertEquals(1, results.size)
        assertEquals(1, results[0].id)
        assertFalse(results[0].content.contains("John"))
    }

    @Test
    fun `rebuildIndex updates search results when repository data changes`() = runTest {
        val updatedMockEntries = initialMockEntries + Entry(
            id = 2,
            content = listOf("Alice", "Wonderland", "alice@gmail.com")
        )

        every { datasetRepository.findAll() } returns updatedMockEntries

        searchService.rebuildIndex()
        val results = searchService.findPerson("Alice", SearchCondition.ALL)

        assertEquals(1, results.size)
        assertEquals(2, results[0].id)

        verify { datasetRepository.findAll() }
    }
}