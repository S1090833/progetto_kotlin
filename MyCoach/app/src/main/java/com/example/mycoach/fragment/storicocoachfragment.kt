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
import com.example.mycoach.adapter.storicoalladapter
import com.example.mycoach.data_class.UserDataClass
import com.google.firebase.firestore.FirebaseFirestore


class storicocoachfragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private lateinit var utentiList : ArrayList<UserDataClass>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_allenamento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.listAllievi)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        utentiList = arrayListOf()

        aggiorbautenti()



    }

    private fun aggiorbautenti(){

        utentiList.clear()

        val db = FirebaseFirestore.getInstance()
        db.collection("Utente").whereEqualTo("liv", 2 ).get().addOnSuccessListener { documents ->
            for(documet in documents){
                val data = documet.data
                val nome = data["Nome"] as String
                val cognome = data["Cognome"] as String
                val email = data["email"] as String
                Log.d(ContentValues.TAG,"-------------"+nome)
                Log.d(ContentValues.TAG,"-------------"+cognome)
                Log.d(ContentValues.TAG,"-------------"+email)
                utentiList.add(UserDataClass(nome, cognome ,email))
            }
            val adapter = storicoalladapter(utentiList)
            recyclerView.adapter= adapter
            Log.d(ContentValues.TAG,"aaaaaaaaaaaaaaaaaaaaaaaaaaa"+utentiList)
        }
            .addOnFailureListener { exception ->
                // Gestire eventuali errori
                Log.e(ContentValues.TAG, "Errore durante il recupero dei dati: $exception")
            }

    }
}