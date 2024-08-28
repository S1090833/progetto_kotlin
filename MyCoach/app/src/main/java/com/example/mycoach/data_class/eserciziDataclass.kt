package com.example.mycoach.data_class

import android.os.Parcel
import android.os.Parcelable

data class eserciziDataclass(
    val nome: String? = null,
    val serie: Int = 0,
    val ripetizioni: Int = 0,
    val riposo: String? = null,
    val esecuzione: String? = null,
    val email: String? = null,
    val giorno: String? = null,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeInt(serie)
        parcel.writeInt(ripetizioni)
        parcel.writeString(riposo)
        parcel.writeString(esecuzione)
        parcel.writeString(email)
        parcel.writeString(giorno)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<eserciziDataclass> {
        override fun createFromParcel(parcel: Parcel): eserciziDataclass {
            return eserciziDataclass(parcel)
        }

        override fun newArray(size: Int): Array<eserciziDataclass?> {
            return arrayOfNulls(size)
        }
    }
}