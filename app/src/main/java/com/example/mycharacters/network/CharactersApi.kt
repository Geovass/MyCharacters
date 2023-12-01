package com.example.mycharacters.network

import com.example.mycharacters.model.Personaje
import com.example.mycharacters.model.PersonajeDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CharactersApi {
    @GET
    fun getCharacters(
        @Url url: String?
    ): Call<ArrayList<Personaje>>

    @GET("characters/character_detail")
    fun getCharacterDetail(
        @Query("id") id: String?
    ): Call<PersonajeDetail>

    // /characters/character_detail/0
    @GET("characters/character_detail/{id}")
    fun getCharacterDetailApiary(
        @Path("id") id: String?
    ): Call<PersonajeDetail>

}