package com.example.minhuskeliste

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*

import java.io.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var adapter: Adapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var referance: DatabaseReference

    private fun signInAnonymously() {
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Innlogging suksess ${it.user}")
        }
        auth.signInAnonymously().addOnFailureListener {
            Log.e(TAG, "Inlogging feilet", it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        signInAnonymously()
        adapter = Adapter(mutableListOf())
        ListeObjekt.adapter = adapter
        ListeObjekt.layoutManager = LinearLayoutManager(this)
        var fileNameRead = "ToDoLists"
        fileNameRead = "$fileNameRead.Lists"
        val pathRead = getExternalFilesDir(null)
        var myExternalFile: File = File(pathRead, fileNameRead)

        if (File(pathRead, fileNameRead).exists()) {
            myExternalFile = File(pathRead.toString(), fileNameRead)
            var Input = FileInputStream(myExternalFile)
            var InputLeser: InputStreamReader = InputStreamReader(Input)
            val Leser: BufferedReader = BufferedReader(InputLeser)
            val Bygger: StringBuilder = StringBuilder()
            var Tekst: String? = null
            while ({ Tekst = Leser.readLine(); Tekst }() != null) {
                Bygger.append(Tekst)
                val liste = Huskeliste(Bygger.toString())
                adapter.LeggTil(liste)
                Bygger.clear()
            }
            Input.close()
        }

        LeggTil.setOnClickListener {
            val ListeTittel = liste.text.toString()
            if (ListeTittel.isNotEmpty()) {
                val liste = Huskeliste(ListeTittel)
                adapter.LeggTil(liste)
                var FilNavn = "Huskelister"
                val Vei = getExternalFilesDir(null)
                if (FilNavn.isNotEmpty() && Vei != null) {
                    FilNavn = "$FilNavn.Lists"
                    File(Vei, FilNavn).delete()
                    val filPlassering = File(Vei, FilNavn)
                    FileOutputStream(File(Vei, FilNavn), true).bufferedWriter().use { writer ->
                        adapter.huskelister.forEach {
                            writer.write("${it.HListeNavn}\n")
                        }
                        Log.d(TAG, "Lastet opp $filPlassering")
                        val ref =
                            FirebaseStorage.getInstance().reference.child("Lists/${filPlassering.toUri().lastPathSegment}")
                        var uploadtask = ref.putFile(filPlassering.toUri())
                        uploadtask.addOnSuccessListener {
                            Log.d(TAG, "Lastet fil opp")
                        }.addOnFailureListener {
                            Log.d(TAG, "Klarte ikke å laste opp fil")
                        }
                    }
                }
                database = FirebaseDatabase.getInstance()
                referance = database.getReference("Lists")
                val Tittel = ListeTittel
                if (Tittel.isNotEmpty()) {
                    referance.child("Lists").push().setValue(Tittel)
                } else {
                    println("Ingen tittel")
                }
            }
        }
    }
}