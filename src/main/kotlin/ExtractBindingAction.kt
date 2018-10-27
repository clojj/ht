import com.google.gson.GsonBuilder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.io.writeStringUtf8
import kotlinx.coroutines.launch

class ExtractBindingAction : AnAction("ht ExtractBinding") {

    private val gson = GsonBuilder().disableHtmlEscaping().create()

    @KtorExperimentalAPI
    override fun actionPerformed(event: AnActionEvent) {

        GlobalScope.launch {

            // TODO get editor's selected range and user-input for the name

            // TODO only add packages once !?
            val addPackagesCommand = AddPackagesCommand(addedPathes = listOf("./src", "./app"))
            HtProjectComponentImpl.output.writeStringUtf8(gson.toJson(addPackagesCommand))

            println("Server said: '${HtProjectComponentImpl.input.readUTF8Line()}'")
            println("Server said: '${HtProjectComponentImpl.input.readUTF8Line()}'")
            println("Server said: '${HtProjectComponentImpl.input.readUTF8Line()}'")

            val refactoringCommand = RefactoringCommand(refactoring = "ExtractBinding", editorSelection = "38:45-38:94", modulePath = "DepTree.hs", details = listOf("newFun'"))
            HtProjectComponentImpl.output.writeStringUtf8(gson.toJson(refactoringCommand))

            println("Server said: '${HtProjectComponentImpl.input.readUTF8Line()}'")
            // TODO get diff / change document etc.

        }
    }
}
