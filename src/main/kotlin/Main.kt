fun main() {
    println("Welcome, please load your data before you start.")
    val dataset = getData()
    if (dataset.isEmpty() || dataset.rowsEmpty()) {
        println("Dataset can't be empty or incomplete. Please restart.")
        return
    }

    val indexedDataset = getMappedValues(dataset)

    println("Dataset loaded, you can perform any of the following actions:")
    var exit = false
    while (!exit) {
        when(displayMenu()) {
            1 -> findPerson(dataset, indexedDataset)
            2 -> printPeople(dataset)
            0 -> exit = true
            else -> {
                println("Incorrect option. Please restart.")
                exit = true
            }
        }
    }
}