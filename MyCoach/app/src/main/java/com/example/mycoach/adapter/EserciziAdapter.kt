package com.example.mycoach.adapter

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.data_class.UserDataClass
import com.example.mycoach.R
import com.example.mycoach.data_class.eserciziDataclass
import com.google.firebase.firestore.FirebaseFirestore

class EserciziAdapter(private val eserciziList: ArrayList<eserciziDataclass>) : RecyclerView.Adapter<EserciziAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.nome)
        val serie: TextView = itemView.findViewById(R.id.serie)
        val reps: TextView = itemView.findViewById(R.id.reps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_esercizi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val esercizio = eserciziList[position]
        holder.nome.text = esercizio.nome
        holder.serie.text = esercizio.serie.toString()
        holder.reps.text = esercizio.ripetizioni.toString()

    }

    override fun getItemCount(): Int {
        return eserciziList.size
    }
}