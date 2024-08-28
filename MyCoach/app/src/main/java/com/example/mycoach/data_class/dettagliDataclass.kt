package com.example.mycoach.data_class

data class dettagliDataClass(
    val data: String = "",
    val email: String = "",
    val nome_esercizio: String = "",
    val sets: Map<String, String> = emptyMap()
) {
    fun setCount(): Int {
        return sets.keys.count { it.contains("ripetizioni") }
    }

    fun getRipetizioni(setNumber: Int): String {
        return sets["set_${setNumber}_ripetizioni"] ?: "0"
    }

    fun getCarico(setNumber: Int): String {
        return sets["set_${setNumber}_carico"] ?: "0"
    }
}