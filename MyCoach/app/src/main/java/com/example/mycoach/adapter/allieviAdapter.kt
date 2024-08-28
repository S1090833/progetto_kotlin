package com.example.mycoach.adapter

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.data_class.UserDataClass
import com.example.mycoach.fragment.infoAllievofragment
import com.example.mycoach.fragment.informazioniallievofragment
import com.google.firebase.firestore.FirebaseFirestore

class allieviAdapter(
    private val utentiList: ArrayList<UserDataClass>,
    ): RecyclerView.Adapter<allieviAdapter.ViewHolder>() {
    class ViewHolder(itemView: View, ) : RecyclerView.ViewHolder(itemView) {

        val nome: TextView = itemView.findViewById(R.id.nome)
        val cognome: TextView = itemView.findViewById(R.id.cognome)
        val elimina: ImageButton = itemView.findViewById(R.id.elimina)

    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.list_recipe, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val clickedPosition = holder.adapterPosition
            val email = utentiList[clickedPosition].email

            // Creare un nuovo Fragment e passare l'email come argomento
            val informazioniallFragment = informazioniallievofragment().apply {
                arguments = Bundle().apply {
                    putString("email", email)
                }
            }

            // Ottenere il FragmentManager dal contesto dell'Adapter
            val fragmentActivity = parent.context as AppCompatActivity
            fragmentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, informazioniallFragment)
                .addToBackStack(null)
                .commit()
        }

        holder.elimina.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val emailToDelete = utentiList[position].email
                deleteUserAndWorkouts(emailToDelete) { success ->
                    if (success) {
                        utentiList.removeAt(position)
                        notifyItemRemoved(position)
                    } else {
                        // Gestisci l'errore, magari mostra un Toast
                    }
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = position
        holder.nome.text = utentiList[position].nome
        holder.cognome.text = utentiList[position].cognome
        Log.d(ContentValues.TAG,"+++++++++++++++++++++++++++++++"+currentItem)


    }

    override fun getItemCount(): Int {
        return utentiList.size
    }

    private fun deleteUserAndWorkouts(emailToDelete: String?, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val utenteRef = db.collection("Utente").document(emailToDelete.toString())
        val allenamentoRef = db.collection("allenamento")

        // Query per ottenere tutti i documenti nella collezione "allenamento" con la stessa email
        allenamentoRef.whereEqualTo("email", emailToDelete).get().addOnSuccessListener { documents ->
            val batch = db.batch()
            for (document in documents) {
                // Aggiungi ogni documento trovato al batch
                batch.delete(document.reference)
            }

            // Elimina il documento nella collezione "Utente"
            batch.delete(utenteRef)

            // Esegui il batch
            batch.commit().addOnSuccessListener {
                callback(true)
            }.addOnFailureListener { exception ->
                callback(false)
            }
        }.addOnFailureListener { exception ->
            callback(false)
        }
    }


}