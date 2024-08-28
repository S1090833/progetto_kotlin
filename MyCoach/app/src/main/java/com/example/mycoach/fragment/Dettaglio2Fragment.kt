package com.example.mycoach.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.adapter.dettaglioAdapter
import com.example.mycoach.data_class.dettagliDataClass
import com.example.mycoach.data_class.eserciziDataclass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Dettaglio2Fragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var completedExercisesList: ArrayList<dettagliDataClass>
    private lateinit var adapter: dettaglioAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dettaglio2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.completedExercisesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        completedExercisesList = arrayListOf()
        adapter = dettaglioAdapter(completedExercisesList)
        recyclerView.adapter = adapter

        val esercizio: eserciziDataclass? = arguments?.getParcelable("esercizio")
        if (esercizio != null) {
            val email = esercizio.email
            val nome = esercizio.nome
            val serie = esercizio.serie

            if (nome != null && email != null && serie != null) {
                SalvaEsercizio(nome, email, serie)
            } else {
                Log.e(TAG, "Nome, email o serie dell'esercizio è nullo")
            }
        } else {
            Log.e(TAG, "Esercizio è nullo")
        }
    }

    private fun SalvaEsercizio(nome: String, email: String, serie: Int) {
        db.collection("EserciziSalvati")
            .whereEqualTo("nome_esercizio", nome)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                completedExercisesList.clear()
                for (document in documents) {
                    val data = document.getString("data") ?: ""
                    val email = document.getString("email") ?: ""
                    val NomeEsercizio = document.getString("nome_esercizio") ?: ""

                    val setsMap = mutableMapOf<String, String>()
                    for (i in 1..serie) {
                        val ripetizioni = document.getString("set_${i}_ripetizioni") ?: "0"
                        val carico = document.getString("set_${i}_carico") ?: "0"
                        setsMap["set_${i}_ripetizioni"] = ripetizioni
                        setsMap["set_${i}_carico"] = carico
                    }

                    val dettagli = dettagliDataClass(
                        data = data,
                        email = email,
                        nome_esercizio = NomeEsercizio,
                        sets = setsMap
                    )

                    completedExercisesList.add(dettagli)
                    Log.d(TAG, "Aggiunto: $dettagli")
                }
                completedExercisesList.sortByDescending { it.data }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Errore durante il recupero dei dati: $exception")
            }
    }
}