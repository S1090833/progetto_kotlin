package com.example.mycoach.fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoach.R
import com.example.mycoach.adapter.EserciziAdapter
import com.example.mycoach.data_class.eserciziDataclass
import com.google.firebase.firestore.FirebaseFirestore


class storicoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eserciziList: ArrayList<eserciziDataclass>
    private lateinit var adapter: EserciziAdapter
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.exerciseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        eserciziList = arrayListOf()
        adapter = EserciziAdapter(eserciziList)
        recyclerView.adapter = adapter

        val email = arguments?.getString("email")
        val giorno = arguments?.getString("giorno")
        Log.d(ContentValues.TAG,"////////////////////////////////"+email)
        Log.d(ContentValues.TAG,"//////////////////////////////////"+giorno)
        if (email != null && giorno != null) {
            loadData(email, giorno)

            val addExerciseButton = view.findViewById<Button>(R.id.addExerciseButton)
            addExerciseButton.setOnClickListener {
                showAddExerciseDialog(email, giorno)
            }
        } else {
            Log.e("EserciziFragment", "Email or giorno is null")
        }

    }

    private fun loadData(email: String, giorno: String) {
        db.collection("esercizi")
            .whereEqualTo("email", email)
            .whereEqualTo("giorno", giorno)
            .get()
            .addOnSuccessListener { documents ->
                eserciziList.clear()
                for (document in documents) {
                    val esercizio = document.toObject(eserciziDataclass::class.java)
                    eserciziList.add(esercizio)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("EserciziFragment", "Errore durante il recupero dei dati: $exception")
            }
    }

    private fun showAddExerciseDialog(email: String, giorno: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_esercizio_dialog, null)
        val exerciseNameEditText = dialogView.findViewById<EditText>(R.id.exerciseNameEditText)
        val seriesEditText = dialogView.findViewById<EditText>(R.id.seriesEditText)
        val repsEditText = dialogView.findViewById<EditText>(R.id.repsEditText)
        val restEditText = dialogView.findViewById<EditText>(R.id.restEditText)
        val esecuzioneSpinner = dialogView.findViewById<Spinner>(R.id.esecuzioneSpinner)

        // Settaggio del menù a tendina
        val options = arrayOf("Buffer", "Top Set", "Cedimento")
        val adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        esecuzioneSpinner.adapter = adapterSpinner

        restEditText.setOnClickListener {
            showTimePickerDialog(restEditText)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Aggiungi Esercizio")
            .setView(dialogView)
            .setPositiveButton("Aggiungi") { dialog, _ ->
                val nome = exerciseNameEditText.text.toString()
                val serie = seriesEditText.text.toString().toIntOrNull()
                val ripetizioni = repsEditText.text.toString().toIntOrNull()
                val riposo = restEditText.text.toString()
                val esecuzione = esecuzioneSpinner.selectedItem.toString()

                if (nome.isNotEmpty() && serie != null && ripetizioni != null && riposo != null && esecuzione != null && giorno!=null ) {
                    saveExercise(nome, serie, ripetizioni, riposo, esecuzione,email,giorno)
                } else {
                    Toast.makeText(requireContext(), "Inserisci tutti i campi", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showTimePickerDialog(editText: EditText) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_timer, null)
        val minuti = dialogView.findViewById<NumberPicker>(R.id.minuti)
        val secondi = dialogView.findViewById<NumberPicker>(R.id.secondi)

        // Configura NumberPickers
        minuti.minValue = 0
        minuti.maxValue = 59
        secondi.minValue = 0
        secondi.maxValue = 59

        AlertDialog.Builder(requireContext())
            .setTitle("Seleziona Minuti e Secondi")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val minutiSelezionati = minuti.value
                val secondiSelezionati = secondi.value
                editText.setText(String.format("%02d:%02d", minutiSelezionati, secondiSelezionati))
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun saveExercise(name: String, serie: Int, ripetizioni: Int, riposo: String, esecuzione: String, email: String, giorno: String) {
        val esercizio = eserciziDataclass(name, serie, ripetizioni, riposo, esecuzione, email, giorno)
        db.collection("esercizi").add(esercizio)
            .addOnSuccessListener {
                eserciziList.add(esercizio)
                adapter.notifyItemInserted(eserciziList.size - 1)
            }
            .addOnFailureListener { exception ->
                Log.e("EserciziFragment", "Errore durante il salvataggio: $exception")
            }
    }
}