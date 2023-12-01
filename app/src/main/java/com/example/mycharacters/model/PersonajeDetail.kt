package com.example.mycharacters.model

import com.google.gson.annotations.SerializedName

data class PersonajeDetail(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("family")
    val family: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)
