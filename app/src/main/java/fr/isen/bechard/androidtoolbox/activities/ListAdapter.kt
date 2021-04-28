package fr.isen.bechard.androidtoolbox.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.bechard.androidtoolbox.R
import fr.isen.bechard.androidtoolbox.dataClass.ProductSample
import kotlinx.android.synthetic.main.product_cell.view.*

class ListAdapter(private val list: List<ProductSample>)
    : RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val product: ProductSample = list[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = list.size

}

class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.compare, parent, false)) {
    private var mProductBarcode: TextView? = null
    private var mProductQte: TextView? = null


    init {
        mProductBarcode = itemView.product_barcode
        mProductQte = itemView.product_qte
    }

    fun bind(product: ProductSample) {
        mProductBarcode?.text = product.code_barre
        mProductQte?.text = product.qte.toString()
    }

}