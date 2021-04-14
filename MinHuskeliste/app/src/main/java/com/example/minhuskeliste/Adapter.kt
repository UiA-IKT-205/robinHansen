package com.example.minhuskeliste

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.liste.view.*
import java.io.File
import java.io.FileOutputStream


class Adapter(val huskelister: MutableList<Huskeliste>) :
    RecyclerView.Adapter<Adapter.HuskelisteViewHolder>() {

//Larger oppgaver som er lagt til listen opp til Firestore

    override fun onBindViewHolder(holder: HuskelisteViewHolder, position: Int) {
        val DenneListen = huskelister[position]
        holder.itemView.apply {
            ListeNavn.text = DenneListen.HListeNavn
            FerdigSlettBoks.isChecked = DenneListen.Slett
            FerdigSlettBoks.setOnCheckedChangeListener { _, isChecked ->
                DenneListen.Slett = !DenneListen.Slett
                SlettListe()

                var fileName = "ToDoList"
                val path = context?.getExternalFilesDir(null)
                if (path != null && fileName.isNotEmpty()) {
                    fileName = "$fileName.Lists"
                    File(path, fileName).delete()
                    val FilePlacement = File(path, fileName)
                    FileOutputStream(File(path, fileName), true).bufferedWriter().use { writer ->
                        huskelister.forEach {
                            writer.write("${it.HListeNavn}\n")
                        }
                        Log.d(TAG, "Lastet opp fil $FilePlacement")

                        val ref =
                            FirebaseStorage.getInstance().reference.child("Lists/${FilePlacement.toUri().lastPathSegment}")
                        var LastOppOppgave = ref.putFile(FilePlacement.toUri())

                        LastOppOppgave.addOnSuccessListener {
                            Log.d(TAG, "Lastet opp fil")
                        }.addOnFailureListener {
                            Log.d(TAG, "Error, fil ikke lastet opp")
                        }
                    }
                }

            }
        }
    }

    class HuskelisteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val TAG = "Adapter"
    //Legger til ny oppgave til listen
    fun LeggTil(huskeliste: Huskeliste) {
        huskelister.add(huskeliste)
        notifyItemInserted(huskelister.size - 1)
    }

    // FInner størrelsen på listen
    override fun getItemCount(): Int {
        return huskelister.size
    }

    //Sletter/Hukker av en liste, den gjør begge sammtidig
    fun SlettListe() {
        huskelister.removeAll { todo ->
            todo.Slett
        }
        try {
            notifyDataSetChanged()
        } catch (e: Exception) {
            Log.d(TAG + "Error", e.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HuskelisteViewHolder {
        return HuskelisteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.liste, parent, false)
        )
    }
}


















