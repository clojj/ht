import com.google.gson.GsonBuilder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.io.writeStringUtf8
import kotlinx.coroutines.launch

class ExtractBindingAction : AnAction("ht ExtractBinding") {

    data class AddPackagesCommand(
        val tag: String = "AddPackages",
        val addedPathes: List<String>
    )
    data class RefactoringCommand(
        val tag: String = "PerformRefactoring",
        val refactoring: String,
        val modulePath: String,
        val editorSelection: String,
        val details: List<String>,
        val shutdownAfter: Boolean = false,
        val diffMode: Boolean = true
    )

    private val gson = GsonBuilder().disableHtmlEscaping().create()

    @KtorExperimentalAPI
    override fun actionPerformed(event: AnActionEvent) {

        GlobalScope.launch {

            // TODO get editor's selected range and user-input for the name

/* TODO only add packages once !?
            val addPackagesCommand = AddPackagesCommand(addedPathes = listOf("/home/jwin/IdeaProjects/plugin-test/src"))
            HtProjectComponentImpl.output.writeStringUtf8(gson.toJson(addPackagesCommand))

            println("Server said: '${HtProjectComponentImpl.input.readUTF8Line()}'")
*/

            val refactoringCommand = RefactoringCommand(refactoring = "ExtractBinding", editorSelection = "31:23-31:60", modulePath = "/home/jwin/IdeaProjects/plugin-test/src/DepTree.hs", details = listOf("nameABC"))
            HtProjectComponentImpl.output.writeStringUtf8(gson.toJson(refactoringCommand))

            println("Server said: '${HtProjectComponentImpl.input.readUTF8Line()}'")
            // TODO get diff / change document etc.

        }
    }
}
