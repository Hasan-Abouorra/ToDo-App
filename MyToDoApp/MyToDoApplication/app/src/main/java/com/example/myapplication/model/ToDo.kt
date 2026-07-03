package com.example.myapplication.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ToDo(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("text")
    val text: String,

    @SerializedName("completed")
    val completed: Boolean = false
) {
    // Eindeutige stabile ID für UI-Zwecke
    val stableId: String
        get() = id ?: UUID.randomUUID().toString()
}