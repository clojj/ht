import com.google.gson.GsonBuilder
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.io.writeStringUtf8
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.util.concurrent.Executors

@KtorExperimentalAPI
fun main() {

    val gson = GsonBuilder().disableHtmlEscaping().create()

    val exec = Executors.newCachedThreadPool()
    val selector = ActorSelectorManager(exec.asCoroutineDispatcher())

    runBlocking {

        val socket: Socket= aSocket(selector).tcp().connect(InetSocketAddress("127.0.0.1", 4123))
        println("socket created: $socket")
        val input: ByteReadChannel = socket.openReadChannel()
        val output: ByteWriteChannel = socket.openWriteChannel(autoFlush = true)

        val addPackagesCommand = AddPackagesCommand(addedPathes = listOf("./src", "./app"))
        output.writeStringUtf8(gson.toJson(addPackagesCommand))

        println("Server said: '${input.readUTF8Line()}'")
        println("Server said: '${input.readUTF8Line()}'")
        println("Server said: '${input.readUTF8Line()}'")

        val refactoringCommand = RefactoringCommand(
            refactoring = "ExtractBinding",
            editorSelection = "38:45-38:94",
            modulePath = "DepTree.hs",
            details = listOf("newFunABC'")
        )
        output.writeStringUtf8(gson.toJson(refactoringCommand))

        println("Server said: '${input.readUTF8Line()}'")

    }

}