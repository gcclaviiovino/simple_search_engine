package org.microservice
import jakarta.persistence.*

/*
    @brief: Entry entity, holding id - string list data pairs.
 */

@Entity
@Table(name = "entries")
class Entry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "entry_content", joinColumns = [JoinColumn(name = "entry_id")])
    var content: List<String> = emptyList()
) {
    val rawContent = content.formatListOfStrings()
}