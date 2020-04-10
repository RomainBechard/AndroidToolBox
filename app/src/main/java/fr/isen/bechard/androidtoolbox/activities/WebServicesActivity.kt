package fr.isen.bechard.androidtoolbox.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.bechard.androidtoolbox.R
import fr.isen.bechard.androidtoolbox.dataClass.RandomUser
import kotlinx.android.synthetic.main.activity_web_services.*

class WebServicesActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var arrayUsr : ArrayList<RandomUser> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_services)

        val recyclerView = RandomUserDataRecyclerView
        val layoutManager = LinearLayoutManager(this)
        val mDividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )
        recyclerView.addItemDecoration(mDividerItemDecoration)


        requestRandomUser(30, RandomUserDataRecyclerView)
    }

    private fun requestRandomUser(number: Int, recycler : RecyclerView) : ArrayList<RandomUser> {

        val queue = Volley.newRequestQueue(this)
        val url = "https://randomuser.me/api/1.1/?results=$number&nat=fr"
        val arrayRdnUser = ArrayList<RandomUser>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->

                val jsonArray = response.getJSONArray("results")

                for (i in 0 until jsonArray.length()) {
                    val rdnUser = Gson().fromJson(jsonArray[i].toString(), RandomUser::class.java)
                    arrayRdnUser.add(rdnUser)
                }

                Log.d("RandomUser", arrayRdnUser.toString())

                viewManager = LinearLayoutManager(this)
                viewAdapter = RandomUserAdapter(arrayRdnUser)

                recycler.apply {
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                Log.d("RandomUser", "ERROR Json : $error \nGo to https://stackoverflow.com/search?q=$error \nOr please, change user and try again :)")
            }
        )
        queue.add(jsonObjectRequest)
        return arrayRdnUser
    }
}