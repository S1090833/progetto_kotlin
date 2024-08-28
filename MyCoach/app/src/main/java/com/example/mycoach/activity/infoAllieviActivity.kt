package com.example.mycoach.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.mycoach.R
import com.google.firebase.firestore.FirebaseFirestore

class infoAllieviActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_allievi)
        val db=FirebaseFirestore.getInstance()

        val email = intent.getStringExtra("email")

        Log.d(ContentValues.TAG,"-------------"+email)



        val nomeAll = findViewById<TextView>(R.id.nomeAll)
        val cognomeAll = findViewById<TextView>(R.id.cognomeAll)
        val numeroAll = findViewById<TextView>(R.id.numeroAll)

        if (email != null) {
            db.collection("Utente").document(email).get().addOnSuccessListener { document ->
                val nome = document.getString("Nome")
                val cognome = document.getString("Cognome")
                val numero = document.getString("Telefono")
                Log.d(ContentValues.TAG,"-------------"+nome)
                Log.d(ContentValues.TAG,"-------------"+numero)



                nomeAll.text = nome
                cognomeAll.text = cognome
                numeroAll.text = numero

            }.addOnFailureListener { exception ->
                // Gestire eventuali errori
                Log.e(ContentValues.TAG, "Errore durante il recupero dei dati: $exception")
            }
        }
    }


}