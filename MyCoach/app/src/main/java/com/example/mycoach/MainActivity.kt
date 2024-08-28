package com.example.mycoach

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.example.mycoach.activity.loginActivity
import androidx.fragment.app.Fragment
import com.example.mycoach.data_class.UserDataClass
import com.example.mycoach.fragment.addAllievoFragment
import com.example.mycoach.fragment.allenamento2Fragment
import com.example.mycoach.fragment.allenamentoFragment
import com.example.mycoach.fragment.storicoFragment
import com.example.mycoach.fragment.storicocoachfragment
import com.example.mycoach.fragment.utente2Fragment
import com.example.mycoach.fragment.utente3Fragment
import com.example.mycoach.fragment.utenteFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var currentUser: UserDataClass
    }
    private var mAuth: FirebaseAuth = Firebase.auth
    private var livello: Int = 1 // livello di default

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            sendToLogin()
        }
    }

    private fun sendToLogin() {
        val loginIntent = Intent(this@MainActivity, loginActivity::class.java)
        startActivity(loginIntent)
        finish() // Chiudi mainactivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val auth = Firebase.auth
        val db = Firebase.firestore
        val navigation: BottomNavigationView = findViewById(R.id.navigation)
        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)
        // Nascondi la navigazione fino a quando non carichi i dati dell'utente
        navigation.visibility = View.GONE
        fragmentContainer.visibility = View.GONE
        // Se un utente all'avvio è già registrato otteniamo informazioni su di esso
        if (auth.currentUser != null) {
            db.collection("Utente").document(auth.currentUser!!.email.toString()).get().addOnSuccessListener { doc ->
                val nome = doc.getString("Nome")
                val cognome = doc.getString("Cognome")
                val email = doc.getString("email")
                val telefono = doc.getString("Telefono")
                val username = doc.getString("Username")
                val peso = doc.getString("peso")
                val altezza = doc.getString("altezza")
                val bmi = doc.getLong("bmi")
                val liv = doc.getLong("liv")

                if (nome != null && cognome != null && email != null && telefono != null && username != null && peso != null && altezza != null && bmi != null && liv != null) {
                    currentUser = UserDataClass(nome, cognome, email, telefono, username, peso, altezza, bmi.toString(), liv.toString())
                    livello = liv.toInt()
                    Log.d(ContentValues.TAG,"ppppppppppppppppppppppppppppppppppp"+ livello)
                    navigation.visibility = View.VISIBLE
                    fragmentContainer.visibility = View.VISIBLE
                    setInitialFragment()
                } else {
                    Log.e("MainActivity", "Dati mancanti nel documento Utente")
                }
            }.addOnFailureListener { exception ->
                Log.e("MainActivity", "Errore durante il recupero dei dati utente: $exception")
            }
        }






        val utenteFragment = utenteFragment() // scheda allievo (Principale)
        val allenamentoFragment = allenamentoFragment ()// allenamenti
        val allenamento2Fragment = allenamento2Fragment ()// allenamenti
        val utente2Fragment = utente2Fragment()
        val utente3Fragment = utente3Fragment() // Fragment del wallet
        val storicocoachfragment = storicocoachfragment()


            navigation.visibility = View.VISIBLE
            fragmentContainer.visibility = View.VISIBLE
            navigation.selectedItemId = R.id.utente
            setCurrentFragment(utenteFragment)

        // Listener che imposta il fragment in base al click del BottomNavigationView

        navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.allenamento -> {
                    if (livello == 1)
                        setCurrentFragment(allenamentoFragment)
                    else if (livello == 2)
                        setCurrentFragment(utente3Fragment)
                }
                R.id.utente -> {
                    if (livello == 1)
                        setCurrentFragment(utenteFragment)
                    else if (livello == 2)
                        setCurrentFragment(utente2Fragment)
                }
                R.id.storico -> {
                    if (livello == 1)
                        setCurrentFragment(storicocoachfragment)
                    else if (livello == 2)
                        setCurrentFragment(allenamento2Fragment)
                }
            }
            true
        }


    }

    private fun setInitialFragment() {
        val initialFragment: Fragment = if (livello == 1) {
            utenteFragment()
        } else {
            utente2Fragment()
        }
        setCurrentFragment(initialFragment)
    }

    // Funzione che inserisce il fragment passato nel fragment container presente nell'activity_main
    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.fragment_container,fragment)
        commit()

    }

    fun replaceFragment(fragment: Fragment) {
        // Sostituisci il fragment corrente con il nuovo fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}