package fr.isen.bechard.androidtoolbox.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import fr.isen.bechard.androidtoolbox.R
import fr.isen.bechard.androidtoolbox.dataClass.RandomUser
import kotlinx.android.synthetic.main.activity_user_information.*

class UserInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_information)

        val user: RandomUser = intent.getParcelableExtra("user")

        UserName.text = user.name.toString()
        UserMail.text = user.email
        UserAddress.text = user.location.toString()
        Picasso.get()
            .load(user.picture?.large)
            .fit().centerInside()
            .into(ImageUser)
    }


}
