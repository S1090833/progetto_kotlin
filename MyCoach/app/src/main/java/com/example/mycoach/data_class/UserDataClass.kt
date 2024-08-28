package com.example.mycoach.data_class

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import java.io.Serializable
import kotlin.collections.ArrayList



data class UserDataClass(
    val nome: String? = null,
    val cognome: String? = null,
    val email: String? = null,
    val telefono: String? = null,
    val username: String? = null,
    val peso: String? = null,
    val altezza: String? = null,
    val bmi: String? = null,
    val liv: String? = null,
)