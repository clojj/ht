data class RefactoringCommand(
    val tag: String = "PerformRefactoring",
    val refactoring: String,
    val modulePath: String,
    val editorSelection: String,
    val details: List<String>,
    val shutdownAfter: Boolean = false,
    val diffMode: Boolean = false
)

