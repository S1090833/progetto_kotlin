package com.example.mycoach.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.adapter.GiorniAdapter
import com.example.mycoach.adapter.allieviAdapter
import com.example.mycoach.data_class.GiorniDataClass
import com.google.firebase.firestore.FirebaseFirestore


class infoAllievofragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var listGiorni : ArrayList<GiorniDataClass>
    private lateinit var adapter: GiorniAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info_allievofragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nomeAll = view.findViewById<TextView>(R.id.nomeAll)
        val cognomeAll = view.findViewById<TextView>(R.id.cognomeAll)
        recyclerView = view.findViewById(R.id.listGiorni)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        listGiorni = arrayListOf()
        adapter = GiorniAdapter(listGiorni)
        recyclerView.adapter = adapter

        val email = arguments?.getString("email")
        Log.d(ContentValues.TAG,"vvvvvvvvvvvvvvvvvvvvvvvvvvvv"+email)

        if (email != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("Utente").document(email).get().addOnSuccessListener { document ->
                val nome = document.getString("Nome")
                val cognome = document.getString("Cognome")
                val numero = document.getString("email")
                Log.d(ContentValues.TAG,"+++++++++++++++++++++++"+nome)
                Log.d(ContentValues.TAG,"++++++++++++++++++++++"+numero)
                nomeAll.text = nome
                cognomeAll.text = cognome

            }.addOnFailureListener { exception ->
                Log.e("infoAllievofragment", "Errore durante il recupero dei dati: $exception")
            }
            visualizzaGiorni(email)
        }
    }

    private fun visualizzaGiorni(email: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("allenamento").whereEqualTo("email", email).get().addOnSuccessListener { documents ->
            listGiorni.clear() // Assicurati di svuotare la lista prima di aggiungere nuovi dati
            for (document in documents) {
                val giorno = document.getLong("giorno")

                    listGiorni.add(GiorniDataClass(giorno.toString(),email))

            }
            listGiorni.sortBy { it.giorno?.toInt() } // Ordina la lista in ordine crescente
            adapter.notifyDataSetChanged()
            Log.d(ContentValues.TAG,"wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"+listGiorni)

        }.addOnFailureListener { exception ->
            Log.e("InfoAllievoFragment", "Errore durante il recupero dei dati: $exception")
        }
    }
}