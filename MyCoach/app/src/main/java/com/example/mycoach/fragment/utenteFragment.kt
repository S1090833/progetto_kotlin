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


class utenteFragment : Fragment(){

    //private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView

    private lateinit var utentiList : ArrayList<UserDataClass>






    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_utente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val auth: FirebaseAuth = Firebase.auth
        val signin = view.findViewById<Button>(R.id.button2)
        val logout = view.findViewById<ImageButton>(R.id.button3)
        recyclerView = view.findViewById(R.id.listAllievi)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        utentiList = arrayListOf()

        aggiorbautenti()






        signin.setOnClickListener {
            val addAllievoFragment = addAllievoFragment()
            (activity as MainActivity).replaceFragment(addAllievoFragment)
        }

        // Click per effettuare il logout
        logout.setOnClickListener {
            auth.signOut()
            // Dopo il logout, reindirizza all'attività di login
            val intent = Intent(view.context, loginActivity::class.java)
            startActivity(intent)

        }
    }
    private fun aggiorbautenti(){

        utentiList.clear()

        val db = FirebaseFirestore.getInstance()
        db.collection("Utente").whereEqualTo("liv", 2 ).get().addOnSuccessListener { documents ->
            for(document in documents){
                val data = document.data
                val nome = data["Nome"] as String
                val cognome = data["Cognome"] as String
                val email = data["email"] as String
                Log.d(TAG,"-------------"+nome)
                Log.d(TAG,"-------------"+cognome)
                Log.d(TAG,"-------------"+email)
                utentiList.add(UserDataClass(nome, cognome ,email))
            }
            val adapter = allieviAdapter(utentiList)
            recyclerView.adapter= adapter
            Log.d(ContentValues.TAG,"aaaaaaaaaaaaaaaaaaaaaaaaaaa"+utentiList)
        }
            .addOnFailureListener { exception ->
                // Gestire eventuali errori
                Log.e(TAG, "Errore durante il recupero dei dati: $exception")
            }

    }

}







