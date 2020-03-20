package fr.isen.bechard.androidtoolbox

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_life_cycle.*

var texteAffiche = ""

class LifeCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        boutonPause.setOnClickListener{
            Toast.makeText(this, "Sortie de pause de l'application", Toast.LENGTH_LONG).show()
            textViewNumberOfCycles.text = texteAffiche
        }

    }

    override fun onPause() {
        super.onPause()
        texteAffiche = "${texteAffiche} \n onPause()"
    }

    override fun onResume() {
        super.onResume()
        texteAffiche = "${texteAffiche} \n onResume()"
        textViewNumberOfCycles.text = texteAffiche
    }

    override fun onDestroy() {
        super.onDestroy()
        texteAffiche = "${texteAffiche} \n onDestroy()"
    }
}


