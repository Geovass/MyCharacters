package com.example.mycharacters.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycharacters.ui.adapters.CharactersAdapter
import com.example.mycharacters.R
import com.example.mycharacters.databinding.ActivityMainBinding
import com.example.mycharacters.model.Personaje
import com.example.mycharacters.network.CharactersApi
import com.example.mycharacters.network.RetrofitService
import com.example.mycharacters.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val call = RetrofitService.getRetrofit()
            .create(CharactersApi::class.java)
            .getCharacters("characters/characters_list")

        call.enqueue(object: Callback<ArrayList<Personaje>>{
            override fun onResponse(
                call: Call<ArrayList<Personaje>>,
                response: Response<ArrayList<Personaje>>
            ) {
                binding.pbConexion.visibility = View.INVISIBLE

                Log.d(Constants.LOGTAG, getString(R.string.respuesta_servidor, response.toString()))
                Log.d(Constants.LOGTAG, getString(R.string.datos, response.body().toString()))

                val charactersAdapter = CharactersAdapter(response.body()!!){personaje ->
                    // Click en cada personaje
                    Toast.makeText(this@MainActivity,
                        getString(R.string.click_personaje, personaje.fullName), Toast.LENGTH_SHORT).show()

                    val bundle = bundleOf(
                        "id" to personaje.id
                    )

                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                binding.rvMenu.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
                binding.rvMenu.adapter = charactersAdapter

            }

            override fun onFailure(call: Call<ArrayList<Personaje>>, t: Throwable) {
                binding.pbConexion.visibility = View.INVISIBLE
                Toast.makeText(this@MainActivity,
                    getString(R.string.no_conexion),
                    Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}