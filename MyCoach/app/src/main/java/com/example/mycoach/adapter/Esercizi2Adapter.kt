package com.example.mycoach.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.data_class.eserciziDataclass
import com.example.mycoach.fragment.Dettaglio2Fragment
import com.example.mycoach.fragment.esecuzioneFragment

class Esercizi2Adapter(
    private val eserciziList: ArrayList<eserciziDataclass>
) : RecyclerView.Adapter<Esercizi2Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeEsercizio: TextView = itemView.findViewById(R.id.nome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_esercizi2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val esercizio = eserciziList[position]
        holder.nomeEsercizio.text = esercizio.nome

        holder.itemView.setOnClickListener {
            val esecuzioneFragment = Dettaglio2Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable("esercizio", esercizio)
                }
            }

            val fragmentActivity = it.context as AppCompatActivity
            fragmentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, esecuzioneFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return eserciziList.size
    }
}