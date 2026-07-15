import java.io.File

fun main(args: Array<String>) {
    if (!(args.any { word -> word == "--data" })) {
        println("To load data use flag --data before file. Please try again.")
        return
    }
    val fileName = args.lastOrNull()
    if (fileName == null) {
        println("Data file needed to start. Please try again.")
        return
    }

    val dataset = getVals(File(fileName).readLines())
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