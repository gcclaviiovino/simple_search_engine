package org.microservice

/*
    @brief: Repository Interface for Entry objects.
 */

interface EntryInterface {
    fun findAll(): List<Entry>
    fun findById(id: Int): Entry?
}