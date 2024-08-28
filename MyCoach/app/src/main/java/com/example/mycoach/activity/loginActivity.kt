package com.example.mycoach.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mycoach.MainActivity
import com.example.mycoach.R
import com.example.mycoach.data_class.UserDataClass
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class loginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val auth = Firebase.auth
        val db = Firebase.firestore


        // Verifica se l'utente è già autenticato
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // L'utente è già autenticato, reindirizza all'attività principale
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Chiudi l'attività di login
        }


        val email = findViewById<EditText>(R.id.email_edit)
        val password = findViewById<EditText>(R.id.pass_edit)
        val accedi = findViewById<Button>(R.id.button3)






        // Click per effettuare l'accesso
        accedi.setOnClickListener()
        {
            if (email.text.toString() != "" && password.text.toString() != "")
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            db.collection("Utente").document(email.text.toString()).get()
                                .addOnSuccessListener { doc ->
                                    MainActivity.currentUser = UserDataClass(
                                        doc["Nome"].toString(),
                                        doc["Cognome"].toString(),
                                        doc["Telefono"].toString(),
                                        doc["email"].toString(),
                                        doc["Username"].toString(),
                                        doc["peso"].toString(),
                                        doc["altezza"].toString(),
                                        doc["bmi"].toString(),
                                    )
                                    val main = Intent(this@loginActivity, MainActivity::class.java)
                                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    Toast.makeText(this@loginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                                    startActivity(main)
                                    finish()
                                }
                        } else {

                            Toast.makeText(this@loginActivity, "Email o Password errati, riprova", Toast.LENGTH_SHORT).show()

                        }
                    }
            else {

                Toast.makeText(this@loginActivity, "compila tutti i campi", Toast.LENGTH_SHORT).show()
            }

        }
    }
}