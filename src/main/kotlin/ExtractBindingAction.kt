import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.io.writeStringUtf8
import kotlinx.coroutines.launch

class ExtractBindingAction : AnAction("ht ExtractBinding") {

    @KtorExperimentalAPI
    override fun actionPerformed(event: AnActionEvent) {

        GlobalScope.launch {

            // TODO get editor's selected range and user-input for the name
            HtProjectComponentImpl.output.writeStringUtf8("ExtractBinding DepTree.hs 31:23-31:60 name\r\n")
            val response = HtProjectComponentImpl.input.readUTF8Line(1000)
            println("Server said: '$response'")
            // TODO get diff / change document etc.

        }
    }
}
