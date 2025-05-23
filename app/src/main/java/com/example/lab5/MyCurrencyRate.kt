package com.example.lab5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class MyCurrencyRate(
    val currency: String,
    val buyRate: Double,
    val sellRate: Double
)

class CurrencyRateAdapter(
    private var rates: List<MyCurrencyRate>,
    private val onItemClick: (MyCurrencyRate) -> Unit
): RecyclerView.Adapter<CurrencyRateAdapter.CurrencyRateViewHolder>() {

    inner class CurrencyRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val currencyNameTextView: TextView = itemView.findViewById(R.id.currencyNameTextView)
        val currencyRateBuyTextView: TextView = itemView.findViewById(R.id.currencyRateBuyTextView)
        val currencyRateSellTextView: TextView = itemView.findViewById(R.id.currencyRateSellTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency_rate, parent, false)
        return CurrencyRateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) {
        val item = rates[position]

        val buyRateFormatted = String.format("%.4f", item.buyRate)
        val sellRateFormatted = String.format("%.4f", item.sellRate)
        holder.currencyNameTextView.text = item.currency
        holder.currencyRateBuyTextView.text = buyRateFormatted
        holder.currencyRateSellTextView.text = sellRateFormatted

        val flagResId = when (item.currency) {
            "USD" -> R.drawable.ic_united_states_flag
            "EUR" -> R.drawable.ic_europe_flag
            else -> R.drawable.ic_icon
        }
        holder.itemView.findViewById<ImageView>(R.id.currencyFlagImageView).setImageResource(flagResId)
        holder.itemView.setOnClickListener{
            onItemClick(item)
        }

    }

    override fun getItemCount(): Int = rates.size

}
