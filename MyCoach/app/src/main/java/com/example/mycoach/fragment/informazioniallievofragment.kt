package com.example.mycoach.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mycoach.R
import com.google.firebase.firestore.FirebaseFirestore


class informazioniallievofragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_informazioni_allievo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nomeAll = view.findViewById<TextView>(R.id.nomeAll)
        val cognomeAll = view.findViewById<TextView>(R.id.cognomeAll)
        val emailAll = view.findViewById<TextView>(R.id.email)
        val numeroall = view.findViewById<TextView>(R.id.numeroAll)
        val altezzaall = view.findViewById<TextView>(R.id.altezza)
        val pesoall = view.findViewById<TextView>(R.id.peso)



        val email = arguments?.getString("email")
        Log.d(ContentValues.TAG,"vvvvvvvvvvvvvvvvvvvvvvvvvvvv"+email)

        if (email != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("Utente").document(email).get().addOnSuccessListener { document ->
                val nome = document.getString("Nome")
                val cognome = document.getString("Cognome")
                val email = document.getString("email")
                val numero = document.getString("Telefono")
                val altezza = document.getString("altezza")
                val peso = document.getString("peso")

                Log.d(ContentValues.TAG,"+++++++++++++++++++++++"+nome)
                Log.d(ContentValues.TAG,"++++++++++++++++++++++"+email)
                nomeAll.text = nome
                cognomeAll.text = cognome
                emailAll.text = email
                numeroall.text = numero
                altezzaall.text = altezza
                pesoall.text = peso
            }.addOnFailureListener { exception ->
                Log.e("infoAllievofragment", "Errore durante il recupero dei dati: $exception")
            }

        }
    }


}