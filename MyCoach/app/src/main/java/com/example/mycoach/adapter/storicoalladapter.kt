package com.example.mycoach.adapter

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.data_class.UserDataClass
import com.example.mycoach.fragment.info2allievofragment
import com.example.mycoach.fragment.info3allievofragment
import com.google.firebase.firestore.FirebaseFirestore

class storicoalladapter(
    private val utentiList: ArrayList<UserDataClass>,
    ): RecyclerView.Adapter<storicoalladapter.ViewHolder>() {
    class ViewHolder(itemView: View, ) : RecyclerView.ViewHolder(itemView) {

        val nome: TextView = itemView.findViewById(R.id.nome)
        val cognome: TextView = itemView.findViewById(R.id.cognome)


    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.list_recipe2, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val clickedPosition = holder.adapterPosition
            val email = utentiList[clickedPosition].email

            // Creare un nuovo Fragment e passare l'email come argomento
            val info2AllievoFragment = info2allievofragment().apply {
                arguments = Bundle().apply {
                    putString("email", email)
                }
            }

            // Ottenere il FragmentManager dal contesto dell'Adapter
            val fragmentActivity = parent.context as AppCompatActivity
            fragmentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, info2AllievoFragment)
                .addToBackStack(null)
                .commit()
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