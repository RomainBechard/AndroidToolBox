package fr.isen.bechard.androidtoolbox

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.bechard.androidtoolbox.dataClass.RandomPokemonData
import kotlinx.android.synthetic.main.activity_web_services.*

class WebServicesActivity : AppCompatActivity() {

    private fun displayRandomUsers() {
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://randomuser.me/api/?inc=name,picture&results=15&noinfo&nat=fr&format=pretty"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val randomUsersObj =
                    Gson().fromJson(response.toString(), RandomPokemonData::class.java)

                Log.d("RESPONSE API", response)
                RandomPokemonDataRecyclerView.layoutManager = LinearLayoutManager(this)
                RandomPokemonDataRecyclerView.adapter = RandomUserAdapter(randomUsersObj)
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "Error during GET request", Toast.LENGTH_SHORT).show()
            })

        queue.add(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_services)

        displayRandomUsers()

        Log.d("STATUS", "onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.d("STATUS", "onResume")
    }
}
