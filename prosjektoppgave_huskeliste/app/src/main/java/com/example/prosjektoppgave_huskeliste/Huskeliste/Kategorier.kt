package com.example.prosjektoppgave_huskeliste.Huskeliste

import android.content.Context
import com.example.prosjektoppgave_huskeliste.Huskeliste.Lokaldata.Kategori
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class KategoriDepot {
    private lateinit var Kategorisamlinger: MutableList<Kategori>

    var onKat: ((List<Kategori>) -> Unit)? = null
    var onKatUpdate: ((cat: Kategori) -> Unit)? = null

    fun load(context: Context) {

        Kategorisamlinger = mutableListOf()

        val TAG = "Huskeliste kategorier"
        val db = Firebase.firestore

        //Importerer kategorier fra Firestore.
        db.collection("Kategorier")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val bookFirebase = Kategori(document.id)
                    nyKat(bookFirebase)
                }
            }

        onKat?.invoke(Kategorisamlinger)
    }

    fun nyKat(cat: Kategori) {
        Kategorisamlinger.add(cat)
        onKat?.invoke(Kategorisamlinger)
    }

    fun slettKat(cat: Kategori) {
        Kategorisamlinger.remove(cat)
        onKat?.invoke(Kategorisamlinger)
    }

    companion object {
        val instance = KategoriDepot()
    }
}