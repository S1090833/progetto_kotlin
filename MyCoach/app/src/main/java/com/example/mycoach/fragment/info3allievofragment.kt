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
import com.example.mycoach.adapter.Giorni3Adapter
import com.example.mycoach.adapter.GiorniAdapter
import com.example.mycoach.data_class.GiorniDataClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class info3allievofragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var giorniList: ArrayList<GiorniDataClass>
    private lateinit var adapter: GiorniAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info2_allievo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.listGiorni)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        giorniList = arrayListOf()
        adapter = GiorniAdapter(giorniList)
        recyclerView.adapter = adapter
        val email = arguments?.getString("email")
        loadData(email)
    }

    private fun loadData(email: String?) {

        if (email != null) {
            db.collection("allenamento").whereEqualTo("email", email).get()
                .addOnSuccessListener { documents ->
                    giorniList.clear()
                    for (document in documents) {
                        val giorno = document.getLong("giorno")
                        if (giorno != null) {
                            giorniList.add(GiorniDataClass(giorno.toString(), email))
                        } else {
                            Log.e(ContentValues.TAG, "Unexpected null giorno value")
                        }
                    }
                    giorniList.sortBy { it.giorno?.toInt() }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.e(ContentValues.TAG, "Errore durante il recupero dei dati: $exception")
                }
        }
    }
}
