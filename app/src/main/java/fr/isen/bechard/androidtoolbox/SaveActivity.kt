package fr.isen.bechard.androidtoolbox

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.media.CamcorderProfile.get
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_save.*
import org.json.JSONObject
import java.io.File
import java.lang.reflect.Array.get
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level.parse


class SaveActivity : AppCompatActivity() {

    lateinit var prenom: String
    lateinit var nom: String
    lateinit var dateDeNaissance: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
        Toast.makeText(applicationContext, "Veuillez entrer vos informations", Toast.LENGTH_SHORT)
            .show()

        var cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd.MM.yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                DateDeNaissance.text = sdf.format(cal.time)
            }

        DateDeNaissance.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        boutonValiderData.setOnClickListener {
            prenom = Prenom.text.toString()
            nom = Nom.text.toString()
            dateDeNaissance = DateDeNaissance.text.toString()

            if (prenom == "" || nom == "") {
                Toast.makeText(
                    applicationContext,
                    "Veuillez compléter toutes les cases",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val info = JSONObject()
                info.put("prenom", prenom)
                info.put("nom", nom)
                info.put("dateDeNaissance", dateDeNaissance)
                val json = info.toString()
                File("${cacheDir}/sauveguarde.json").writeText(json)
                Toast.makeText(
                    applicationContext,
                    "Vos informations on été sauveguardés",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        boutonLecture.setOnClickListener {
            val json = JSONObject(File("$cacheDir/sauveguarde.json").readText())
            val prenomLu = json.getString("prenom")
            val nomLu = json.getString("prenom")
            val DateDeNaissanceLu = json.getString("dateDeNaissance")

            if (prenomLu == "" || nomLu =="" || DateDeNaissanceLu == ""){
                Toast.makeText(this, "Le fichier est vide ou incomplet, Veuillez entrer des données" , Toast.LENGTH_LONG).show()
            }else {
                val parsedDate = DateDeNaissanceLu.removeRange(0..5)
                val age = 2020-parsedDate.toInt()
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle("Lecture du fichier")
                //set message for alert dialog
                builder.setMessage("Prenom : ${prenomLu} \nNom : ${nomLu} \nDate De Naissance : ${DateDeNaissanceLu}\nAge : ${age}")
                builder.setPositiveButton("OK", null);
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }
    }
}
