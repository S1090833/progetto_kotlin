package com.example.mycoach.adapter

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.data_class.GiorniDataClass
import com.example.mycoach.data_class.UserDataClass
import com.example.mycoach.fragment.infoAllievofragment
import com.example.mycoach.fragment.storico3Fragment
import com.example.mycoach.fragment.storicoFragment
import com.google.firebase.firestore.FirebaseFirestore

class GiorniAdapter(
    private val listGiorni: ArrayList<GiorniDataClass>,
    ): RecyclerView.Adapter<GiorniAdapter.ViewHolder>() {
    class ViewHolder(itemView: View, ) : RecyclerView.ViewHolder(itemView) {
        val giorno: TextView = itemView.findViewById(R.id.giorno)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.list_giorni, parent, false)
        val holder = ViewHolder(view)

        holder.itemView.setOnClickListener {
            val clickedPosition = holder.adapterPosition
            val giorno = listGiorni[clickedPosition].giorno
            val email = listGiorni[clickedPosition].email
            // Creare un nuovo Fragment e passare l'email come argomento
            val storicoFragment = storicoFragment().apply {
                arguments = Bundle().apply {
                    putString("giorno", giorno)
                    putString("email", email)
                }
            }

            // Ottenere il FragmentManager dal contesto dell'Adapter
            val fragmentActivity = parent.context as AppCompatActivity
            fragmentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, storicoFragment)
                .addToBackStack(null)
                .commit()
        }


        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.giorno.text = listGiorni[position].giorno
    }

    override fun getItemCount(): Int {
        return listGiorni.size
    }
}