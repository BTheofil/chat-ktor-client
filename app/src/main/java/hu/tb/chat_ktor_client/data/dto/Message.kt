package hu.tb.chat_ktor_client.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val sender: String,
    val message: String,
    val timestamp: Long,
    val sessionId: String
)
