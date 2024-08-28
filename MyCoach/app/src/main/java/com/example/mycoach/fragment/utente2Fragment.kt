package com.example.mycoach.fragment

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.MainActivity
import com.example.mycoach.R
import com.example.mycoach.activity.loginActivity
import com.example.mycoach.adapter.allieviAdapter
import com.example.mycoach.data_class.UserDataClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class utente2Fragment : Fragment(){

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var nomeTextView: TextView
    private lateinit var cognomeTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var telefonoTextView: TextView
    private lateinit var pesoTextView: TextView
    private lateinit var altezzaTextView: TextView
    private lateinit var bmiTextView: TextView






    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_utente2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Firebase Authentication and Firestore initialization
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Reference to UI components
        nomeTextView = view.findViewById(R.id.nomeTextView)
        cognomeTextView = view.findViewById(R.id.cognomeTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        telefonoTextView = view.findViewById(R.id.telefonoTextView)
        pesoTextView = view.findViewById(R.id.pesoTextView)
        altezzaTextView = view.findViewById(R.id.altezzaTextView)
        bmiTextView = view.findViewById(R.id.bmiTextView)

        val logout = view.findViewById<ImageButton>(R.id.button3)

        // Handle logout
        logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(view.context, loginActivity::class.java)
            startActivity(intent)
        }

        // Load user data
        loadUserDataByEmail()
    }

    private fun loadUserDataByEmail() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userEmail = user.email

            // Query Firestore for a document with the user's email
            db.collection("Utente")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.first()  // Assuming email is unique and returns only one document
                        nomeTextView.text = document.getString("Nome")
                        cognomeTextView.text = document.getString("Cognome")
                        emailTextView.text = userEmail ?: "Email non disponibile"
                        telefonoTextView.text = document.getString("Telefono")
                        pesoTextView.text = document.getString("peso")
                        altezzaTextView.text = document.getString("altezza")
                        bmiTextView.text = document.getLong("bmi").toString()

                    } else {
                        Log.d("Utente2Fragment", "Nessun documento trovato")
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("Utente2Fragment", "Errore: ", e)
                }
        }
    }

}
