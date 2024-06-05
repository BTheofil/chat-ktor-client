package hu.tb.chat_ktor_client.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.tb.chat_ktor_client.data.KtorClient
import hu.tb.chat_ktor_client.data.dto.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val ktorClient = KtorClient()

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    data class MainState(
        val messageList: List<Message> = emptyList()
    )

    init {
        getMessages()
        viewModelScope.launch {
            ktorClient.connectSession()
            ktorClient.observeMessages().onEach { message ->
                Log.d("MYTAG", message.toString())

                val newList = state.value.messageList.toMutableList().apply {
                    add(0, message)
                }

                _state.update {
                    it.copy(
                       messageList = newList
                    )
                }


            }.launchIn(viewModelScope)
        }
    }

    fun sendMessage(){
        viewModelScope.launch {
            ktorClient.sendMessage()
        }
    }

    fun getMessages(){
        viewModelScope.launch {
            val messageList = ktorClient.getMessages()
            _state.value = _state.value.copy(
                messageList = messageList
            )
        }
    }

}