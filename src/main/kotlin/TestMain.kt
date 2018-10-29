import com.google.gson.Gson
import com.google.gson.GsonBuilder
import commands.AddPackagesCommand
import commands.DisconnectCommand
import commands.RefactoringCommand
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.io.writeStringUtf8
import java.net.InetSocketAddress
import java.util.concurrent.Executors

@KtorExperimentalAPI
fun main() {

    val gson = GsonBuilder().disableHtmlEscaping().create()

    val exec = Executors.newCachedThreadPool()
    val selector = ActorSelectorManager(exec.asCoroutineDispatcher())

    var socket: Socket? = null
    var output: ByteWriteChannel? = null

    Runtime.getRuntime().addShutdownHook(Thread(Runnable {
        exit(output, gson, socket)
    }))

    try {
        runBlocking(newFixedThreadPoolContext(2, "myPool")) {
            socket = aSocket(selector).tcp().connect(InetSocketAddress("127.0.0.1", 4123))
            println("SOCKET CREATED")

            val input: ByteReadChannel? = socket?.openReadChannel()
            launch {
                while (true) {
                    println("Server said: '${input?.readUTF8Line()}'")
                }
            }

            output = socket?.openWriteChannel(autoFlush = true)

            val addPackagesCommand = AddPackagesCommand(addedPathes = listOf("./src", "./app"))
            output?.writeStringUtf8(gson.toJson(addPackagesCommand))

            doRefactoring(output, gson)
            launch {
                while (true) {
                    readLine()
                    doRefactoring(output, gson)
                }
            }

        }

    } finally {
        exit(output, gson, socket)
    }

}

private suspend fun doRefactoring(
    output: ByteWriteChannel?,
    gson: Gson
): Unit? {
    val refactoringCommand = RefactoringCommand(
        refactoring = "ExtractBinding",
        editorSelection = "38:45-38:94",
        modulePath = "DepTree.hs",
        details = listOf("newFunABC'")
    )
    return output?.writeStringUtf8(gson.toJson(refactoringCommand))
}

private fun exit(
    output: ByteWriteChannel?,
    gson: Gson,
    socket: Socket?
) {
    runBlocking {
        val disconnectCommand = DisconnectCommand(contents = emptyList())
        output?.writeStringUtf8(gson.toJson(disconnectCommand))
    }

    socket?.close()
}