package org.microservice
interface EntryInterface {
    fun findAll(): List<Entry>
    fun findById(id: Int): Entry?
}