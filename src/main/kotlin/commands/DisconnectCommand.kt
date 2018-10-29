package commands

data class DisconnectCommand(
    val tag: String = "Disconnect",
    val contents: List<String>
)

