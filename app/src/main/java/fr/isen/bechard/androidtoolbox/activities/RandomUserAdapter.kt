package fr.isen.bechard.androidtoolbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.bechard.androidtoolbox.dataClass.RandomPokemonData
import kotlinx.android.synthetic.main.activity_random_user_cell.view.*

class RandomUserAdapter(private val randomPokemon: RandomPokemonData) :
    RecyclerView.Adapter<RandomUserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_random_user_cell, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount()= randomPokemon.results.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.UserNameAndSurname.text = randomPokemon.results[position].name
        holder.UserMail.text = randomPokemon.results[position].mail
        Picasso.get()
            .load(randomPokemon.results[position].picture)
            .into(holder.UserImage)
    }

    class UserViewHolder(contactView: View) : RecyclerView.ViewHolder(contactView) {
        val UserNameAndSurname: TextView = contactView.UserNameAndSurname
        val UserMail: TextView = contactView.RandomUserMail
        val UserImage: ImageView = contactView.PokemonImageView
    }
}