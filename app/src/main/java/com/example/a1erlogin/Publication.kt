package com.example.a1erlogin

import com.google.firebase.Timestamp

data class Publication(
    val id: String = "",
    val content: String = "",
    val author: String = "",
    val timestamp: Timestamp? = null,
    val likes: List<String> = emptyList()
)
