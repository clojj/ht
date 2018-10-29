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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.util.concurrent.Executors

object HtProjectComponentImpl : HtProjectComponent {

    lateinit var socket: Socket
    lateinit var input: ByteReadChannel
    lateinit var output: ByteWriteChannel

    override fun projectClosed() {
        super.projectClosed()

        socket.close()
        println("socket CLOSED")
    }

    @KtorExperimentalAPI
    override fun projectOpened() {
        super.projectOpened()

        val exec = Executors.newFixedThreadPool(3)
        val selector = ActorSelectorManager(exec.asCoroutineDispatcher())

        runBlocking {

            socket = aSocket(selector).tcp().connect(InetSocketAddress("127.0.0.1", 4123))
            println("socket created: $socket")
            input = socket.openReadChannel()
            output = socket.openWriteChannel(autoFlush = true)

            launch {
                while (true) {
                    println("Server said: '${input.readUTF8Line()}'")
                }
            }

        }
    }


}
