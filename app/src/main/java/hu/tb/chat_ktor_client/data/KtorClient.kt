package hu.tb.chat_ktor_client.data

import android.util.Log
import hu.tb.chat_ktor_client.data.dto.Message
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class KtorClient {

    companion object {
        val client = HttpClient(CIO) {
            this.install(Logging)
            this.install(WebSockets)
            this.install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    private lateinit var socket: WebSocketSession

    suspend fun connectSession() {
        socket = client.webSocketSession {
            url("ws://192.168.0.170:8080/chat?username=bela")
        }
        if(socket.isActive) {
            Log.d("MYTAG", "alive")
        } else {
            Log.d("MYTAG", "ded")
        }
    }

    fun observeMessages(): Flow<Message> {
        return try {
            socket.incoming
                .receiveAsFlow()
                .filter { it is Frame.Text }
                .map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    Json.decodeFromString<Message>(json)
                }
        } catch(e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    suspend fun sendMessage(){
        socket.send(Frame.Text("wow"))
    }

    suspend fun getMessages(): List<Message>{
        return client.get<List<Message>>("http://192.168.0.170:8080/messages").map { it }
    }
}