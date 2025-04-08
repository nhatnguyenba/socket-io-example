package com.nhatnguyenba.chatapp

data class Message(
    val senderId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSent: Boolean // true: tin nhắn gửi, false: tin nhắn nhận
)