package xyz.androidrey.composepagination.domain.entity

data class ChatMessage(
    val id: String,
    val text: String,
    val timestamp: Long,
    val isUserMessage: Boolean
)
