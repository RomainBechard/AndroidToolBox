package fr.isen.bechard.androidtoolbox.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.bechard.androidtoolbox.R
import fr.isen.bechard.androidtoolbox.dataClass.ProductSample
import kotlinx.android.synthetic.main.compare.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*

class WebServicesActivity : Fragment() {

    val products: List<ProductSample> =
        ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        readCSV(products)
    }

    private fun readCSV(products: List<ProductSample>) {
        val `is` = resources.openRawResource(R.raw.bdd_code_barre)
        val reader = BufferedReader(
            InputStreamReader(`is`, Charset.forName("UTF-8"))
        )
        var line = ""
        try {
            while (reader.readLine().also { line = it } != null) {
                val tokens = line.split(",").toTypedArray()
                val sample: ProductSample = ProductSample()
                val qte = tokens[1].toInt()
                sample.qte = qte
                sample.code_barre = tokens[2]
                sample.ref_fournisseur = tokens[3]
                sample.designation = tokens[4]
                val prix = tokens[5].toFloat()
                sample.prix_normal = prix
                val prixSoldes = tokens[6].toFloat()
                sample.prix_soldes = prix
                val prixPromo = tokens[7].toFloat()
                sample.prix_promo = prix
                sample.taille = tokens[8]
                sample.couleur = tokens[9]
                products.toMutableList().add(sample)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.compare, container, false)

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
        ProductsView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = ListAdapter(products)
        }
    }

    companion object {
        fun newInstance(): WebServicesActivity = WebServicesActivity()
    }
}