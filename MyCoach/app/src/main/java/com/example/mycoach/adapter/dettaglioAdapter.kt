package com.example.mycoach.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.data_class.dettagliDataClass

class dettaglioAdapter(
    private val DettagliList: ArrayList<dettagliDataClass>
) : RecyclerView.Adapter<dettaglioAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataTextView: TextView = itemView.findViewById(R.id.dataTextView)
        val setsLayout: LinearLayout = itemView.findViewById(R.id.eserciziLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_storicoesercizi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Esercizi = DettagliList[position]
        holder.dataTextView.text = Esercizi.data

        holder.setsLayout.removeAllViews()
        for (i in 1..Esercizi.setCount()) {
            val setLayout = LayoutInflater.from(holder.itemView.context).inflate(R.layout.list_set, holder.setsLayout, false)
            val RipetizioniTextView = setLayout.findViewById<TextView>(R.id.setRepsTextView)
            val CaricoTextView = setLayout.findViewById<TextView>(R.id.setWeightTextView)

            RipetizioniTextView.text = "Serie$i Ripetizioni: ${Esercizi.getRipetizioni(i)}"
            CaricoTextView.text = "Carico: ${Esercizi.getCarico(i)} Kg"
            holder.setsLayout.addView(setLayout)
        }
    }

    override fun getItemCount(): Int {
        return DettagliList.size
    }
}