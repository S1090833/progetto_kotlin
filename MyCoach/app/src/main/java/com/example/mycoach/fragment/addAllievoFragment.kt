package com.example.mycoach.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mycoach.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth




class addAllievoFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_crea_allievo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        val auth: FirebaseAuth = Firebase.auth

        val nome = view.findViewById<EditText>(R.id.nome)
        val cognome = view.findViewById<EditText>(R.id.cognome)
        val username = view.findViewById<EditText>(R.id.username)
        val telefono = view.findViewById<EditText>(R.id.telefono)
        val password = view.findViewById<EditText>(R.id.password)
        val email = view.findViewById<EditText>(R.id.email)
        val peso = view.findViewById<EditText>(R.id.peso)
        val altezza = view.findViewById<EditText>(R.id.altezza)
        val bmi = view.findViewById<EditText>(R.id.bmi)//sarebbe giorni
        val registrati = view.findViewById<Button>(R.id.registrati)
        val db = Firebase.firestore
        val dialog = Dialog(view.context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Click che registra un nuovo utente se ha inserito correttamente tutti i campi
        registrati.setOnClickListener {
            dialog.show()
            if (nome.text.toString() != "" && cognome.text.toString() != "" &&
                username.text.toString() != "" && telefono.text.toString() != "" &&
                password.text.toString() != "" && email.text.toString() != "" &&
                peso.text.toString() != "" && altezza.text.toString() != "" && bmi.text.toString() != "") {

                if (password.text.toString().length >= 6) {
                    val bmiValue = bmi.text.toString().toIntOrNull() ?: 0

                    if (bmiValue in 1..5) {
                        val user = hashMapOf(
                            "Nome" to nome.text.toString(),
                            "Cognome" to cognome.text.toString(),
                            "email" to email.text.toString(),
                            "Telefono" to telefono.text.toString(),
                            "Username" to username.text.toString(),
                            "Password" to password.text.toString(),
                            "peso" to peso.text.toString(),
                            "altezza" to altezza.text.toString(),
                            "bmi" to bmiValue,
                            "liv" to 2,
                        )

                        db.collection("Utente").document(email.text.toString()).set(user).addOnSuccessListener {
                            auth.createUserWithEmailAndPassword(user["email"].toString(), user["Password"].toString()).addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    val allenamentoCollection = db.collection("allenamento")

                                    // Per ogni valore fino al BMI, aggiungi un documento alla collezione "allenamento"
                                    for (i in 1..bmiValue) {
                                        val allenamentoData = hashMapOf(
                                            "giorno" to i,
                                            "email" to email.text.toString()
                                        )
                                        // Aggiungi il documento "allenamento" per il giorno corrente add crea automaticamente una chiave univoca
                                        allenamentoCollection.add(allenamentoData)
                                            .addOnFailureListener { exception ->
                                                // Gestire eventuali errori
                                                Toast.makeText(context, "Errore durante la creazione del documento allenamento: $exception", Toast.LENGTH_SHORT).show()
                                            }
                                    }

                                    dialog.dismiss()
                                    Toast.makeText(context, "Utente creato correttamente", Toast.LENGTH_SHORT).show()
                                } else {
                                    dialog.dismiss()
                                    Toast.makeText(context, "Campi non validi", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }.addOnFailureListener {
                            dialog.dismiss()
                            Toast.makeText(context, "Errore di comunicazione con il database, riprova", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(context, "Il massimo dei giorni è 5", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    dialog.dismiss()
                    Toast.makeText(context, "La password deve contenere almeno 6 lettere", Toast.LENGTH_SHORT).show()
                }
            } else {
                dialog.dismiss()
                Toast.makeText(context, "Inserisci tutti i dati!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}