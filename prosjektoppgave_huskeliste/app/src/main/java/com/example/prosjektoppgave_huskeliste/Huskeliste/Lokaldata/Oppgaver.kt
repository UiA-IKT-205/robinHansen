package com.example.prosjektoppgave_huskeliste.Huskeliste.Lokaldata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tasks(val task:String, var isChecked:Boolean = false):Parcelable
