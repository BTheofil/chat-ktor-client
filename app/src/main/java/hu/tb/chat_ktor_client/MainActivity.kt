package hu.tb.chat_ktor_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.tb.chat_ktor_client.presentation.MainViewModel
import hu.tb.chat_ktor_client.ui.theme.ChatktorclientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm = MainViewModel()
            val state by vm.state.collectAsState()

            ChatktorclientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        Text(text = "hello")
                        Button(onClick = { vm.sendMessage() }) {
                            Text(text = "Send message")
                        }
                        LazyColumn(
                        ) {
                            items(
                                items = state.messageList
                            ) { message ->
                                ListItem(headlineContent = {
                                    Text(text = message.message)
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}