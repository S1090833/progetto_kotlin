package com.example.mycoach.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.adapter.Esercizi2Adapter
import com.example.mycoach.adapter.Esercizi3Adapter
import com.example.mycoach.data_class.eserciziDataclass
import com.google.firebase.firestore.FirebaseFirestore

class storico2Fragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eserciziList: ArrayList<eserciziDataclass>
    private lateinit var adapter: Esercizi2Adapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storico2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.exerciseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        eserciziList = arrayListOf()
        adapter = Esercizi2Adapter(eserciziList)
        recyclerView.adapter = adapter

        val email = arguments?.getString("email")
        val giorno = arguments?.getString("giorno")
        if (email != null && giorno != null) {
            loadData(email, giorno)
        }
    }

    private fun loadData(email: String, giorno: String) {
        db.collection("esercizi").whereEqualTo("email", email).whereEqualTo("giorno", giorno).get()
            .addOnSuccessListener { documents ->
                eserciziList.clear()
                for (document in documents) {
                    val esercizio = document.toObject(eserciziDataclass::class.java)
                    eserciziList.add(esercizio)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("storicoFragment", "Errore durante il recupero dei dati: $exception")
            }
    }
}