package com.example.mycoach.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mycoach.R
import com.example.mycoach.data_class.eserciziDataclass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class esecuzioneFragment : Fragment() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var resetButton: ImageButton
    private lateinit var dynamicLayout: LinearLayout
    private lateinit var esercizio: eserciziDataclass
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var timerRunning: Boolean = false
    private var startTimeInMillis: Long = 0
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_esecuzione, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timerTextView = view.findViewById(R.id.timerTextView)
        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        resetButton = view.findViewById(R.id.resetButton)
        dynamicLayout = view.findViewById(R.id.dynamicLayout)

        esercizio = arguments?.getParcelable("esercizio")!!

        val riposo = esercizio.riposo
        startTimeInMillis = parseTimeToMillis(riposo.toString())
        timeLeftInMillis = startTimeInMillis

        startButton.setOnClickListener {
            if (!timerRunning) {
                startTimer()
            }
        }

        stopButton.setOnClickListener {
            if (timerRunning) {
                stopTimer()
            }
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        updateTimer()
        createDynamicFields(esercizio.serie)
    }

    private fun parseTimeToMillis(time: String): Long {
        val parts = time.split(":")
        val minutes = parts[0].toInt()
        val seconds = parts[1].toInt()
        return (minutes * 60 + seconds) * 1000L
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                timerRunning = false
                updateButtons()
            }
        }.start()

        timerRunning = true
        updateButtons()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        timerRunning = false
        updateButtons()
    }

    private fun resetTimer() {
        timeLeftInMillis = startTimeInMillis
        updateTimer()
        updateButtons()
    }

    private fun updateTimer() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = timeFormatted
    }

    private fun updateButtons() {
        if (timerRunning) {
            startButton.visibility = View.INVISIBLE
            stopButton.visibility = View.VISIBLE
            resetButton.visibility = View.INVISIBLE
        } else {
            startButton.visibility = View.VISIBLE
            stopButton.visibility = View.INVISIBLE

            if (timeLeftInMillis < 1000) {
                startButton.visibility = View.INVISIBLE
            } else {
                startButton.visibility = View.VISIBLE
            }

            if (timeLeftInMillis < startTimeInMillis) {
                resetButton.visibility = View.VISIBLE
            } else {
                resetButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun createDynamicFields(serie: Int) {
        for (i in 1..serie) {
            val linearLayout = LinearLayout(requireContext())
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linearLayout.orientation = LinearLayout.HORIZONTAL

            // Aggiungi un TextView per visualizzare il numero della serie
            val serieTextView = TextView(requireContext())
            serieTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            serieTextView.text = "Serie $i"
            linearLayout.addView(serieTextView)

            val repetitionsEditText = EditText(requireContext())
            repetitionsEditText.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            repetitionsEditText.hint = "Repetizioni"
            repetitionsEditText.tag = "repetitions_$i"
            linearLayout.addView(repetitionsEditText)

            val weightEditText = EditText(requireContext())
            weightEditText.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            weightEditText.hint = "Carico"
            weightEditText.tag = "weight_$i"
            linearLayout.addView(weightEditText)

            dynamicLayout.addView(linearLayout)
        }

        // Add a save button
        val saveButton = Button(requireContext())
        saveButton.text = "Salva"
        saveButton.setOnClickListener {
            salvaEsercizio(serie)
        }
        dynamicLayout.addView(saveButton)
    }

    private fun salvaEsercizio(serie: Int) {
        val listaEsercizio = hashMapOf<String, Any>()

        for (i in 1..serie) {
            val ripetizioniEditText = dynamicLayout.findViewWithTag<EditText>("repetitions_$i")
            val caricoEditText = dynamicLayout.findViewWithTag<EditText>("weight_$i")

            val ripetizioni = ripetizioniEditText?.text.toString()
            val carico = caricoEditText?.text.toString()

            listaEsercizio["set_${i}_ripetizioni"] = ripetizioni
            listaEsercizio["set_${i}_carico"] = carico
        }

        val currentUser = auth.currentUser
        val email = currentUser?.email
        val nomeEsercizio = esercizio.nome
        val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        listaEsercizio["email"] = email ?: ""
        listaEsercizio["nome_esercizio"] = nomeEsercizio ?: ""
        listaEsercizio["data"] = data

        db.collection("EserciziSalvati").add(listaEsercizio)
            .addOnSuccessListener {
                Toast.makeText(context, "Esercizi salvati con successo", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Errore nel salvataggio degli esercizi", Toast.LENGTH_SHORT).show()
            }
    }
}