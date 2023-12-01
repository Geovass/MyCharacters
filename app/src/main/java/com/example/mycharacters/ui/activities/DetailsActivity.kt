package com.example.mycharacters.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mycharacters.R
import com.example.mycharacters.databinding.ActivityDetailsBinding
import com.example.mycharacters.model.PersonajeDetail
import com.example.mycharacters.network.CharactersApi
import com.example.mycharacters.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val id = bundle?.getString("id","0")

        val call = RetrofitService.getRetrofit()
            .create(CharactersApi::class.java)
            .getCharacterDetail(id)

        call.enqueue(object: Callback<PersonajeDetail>{
            override fun onResponse(call: Call<PersonajeDetail>, response: Response<PersonajeDetail>) {

                with(binding) {

                    pbConexion.visibility = View.INVISIBLE

                    textFullName.text = response.body()?.fullName
                    Glide.with(this@DetailsActivity)
                        .load(response.body()?.imageUrl)
                        .into(ivPersonaje)
                    textFirstName.text = response.body()?.firstName
                    textLastName.text = response.body()?.lastName
                    textTitle.text = response.body()?.title
                    textFamily.text = response.body()?.family
                }
            }

            override fun onFailure(call: Call<PersonajeDetail>, t: Throwable) {
                binding.pbConexion.visibility = View.INVISIBLE
                Toast.makeText(this@DetailsActivity,
                    getString(R.string.no_conexion),
                    Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}