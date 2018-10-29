package commands

data class AddPackagesCommand(
    val tag: String = "AddPackages",
    val addedPathes: List<String>
)

