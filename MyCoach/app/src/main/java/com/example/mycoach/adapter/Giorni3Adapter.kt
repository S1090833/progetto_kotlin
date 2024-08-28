package com.example.mycoach.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.data_class.GiorniDataClass
import com.example.mycoach.fragment.storico2Fragment
import com.example.mycoach.fragment.storico3Fragment
import com.example.mycoach.fragment.storicoFragment

class Giorni3Adapter(
    private val listGiorni: ArrayList<GiorniDataClass>
) : RecyclerView.Adapter<Giorni3Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val giorno: TextView = itemView.findViewById(R.id.giorno)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_giorni, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val giornoData = listGiorni[position]
        holder.giorno.text = giornoData.giorno

        holder.itemView.setOnClickListener {
            val giorno = giornoData.giorno
            val email = giornoData.email
            val storicoFragment = storico3Fragment().apply {
                arguments = Bundle().apply {
                    putString("giorno", giorno)
                    putString("email", email)
                }
            }

            val fragmentActivity = it.context as AppCompatActivity
            fragmentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, storicoFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return listGiorni.size
    }
}