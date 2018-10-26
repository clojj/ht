import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.cio.write
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.util.concurrent.Executors

class ExtractBindingAction : AnAction("ht ExtractBinding") {

    @KtorExperimentalAPI
    override fun actionPerformed(event: AnActionEvent) {

        // TODO on init
        val exec = Executors.newCachedThreadPool()
        val selector = ActorSelectorManager(exec.asCoroutineDispatcher())

        runBlocking {

            // TODO on init
            val socket = aSocket(selector).tcp().connect(InetSocketAddress("127.0.0.1", 4123))
            println("socket = $socket")
            val input : ByteReadChannel = socket.openReadChannel()
            val output: ByteWriteChannel = socket.openWriteChannel(autoFlush = true)


            // TODO get editor's selected range and user-input for the name
            output.write("ExtractBinding DepTree.hs 31:23-31:60 name\r\n")

            val response = input.readUTF8Line()
            println("Server said: '$response'")
            // TODO get diff / change document etc.

        }


    }
}
