package com.example.mycharacters.model

import com.google.gson.annotations.SerializedName

data class Personaje(
    @SerializedName("id")
    val id: String?,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)