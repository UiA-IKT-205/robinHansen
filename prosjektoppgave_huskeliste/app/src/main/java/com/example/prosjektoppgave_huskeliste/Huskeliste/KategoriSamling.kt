package com.example.prosjektoppgave_huskeliste.Huskeliste

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prosjektoppgave_huskeliste.Huskeliste.Lokaldata.Kategori
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.kategori_layout.view.*
import kotlinx.android.synthetic.main.kategori_detaljer_layout.view.*

import com.example.prosjektoppgave_huskeliste.databinding.KategoriDetaljerLayoutBinding

class CatCollectionAdapter(private var cats:List<Kategori>, private val onCatClicked:(Kategori) -> Unit) : RecyclerView.Adapter<CatCollectionAdapter.ViewHolder>(){

    class ViewHolder(private val binding:KategoriDetaljerLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Kategori, onCatClicked:(Kategori) -> Unit) {
            binding.title.text = cat.category
            //binding.progressBar.progress = 66

            val TAG = "CatCollectionAdapter"
            val db = Firebase.firestore

            db.collection("Progress")
                .document(cat.category)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: ${snapshot.data}")
                        val progress = snapshot.data.toString().replace("{progress=", "")
                        val formattedProgress = progress.replace("}", "")
                        binding.progressBar.progress = formattedProgress.toInt()
                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }

            binding.card.setOnClickListener {
                onCatClicked(cat)
            }
        }
    }

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = cats[position]
        holder.bind(cat,onCatClicked)

        holder.itemView.apply {
            title.text = cat.category

            deleteBt.setOnClickListener {
                val TAG = "ToDoListTasks"

                val db = Firebase.firestore

                // Deletes category from Firestore //
                // --------------------------------------------------------------------------------------- //
                db.collection("Categories")
                    .document(title.text as String)
                    .delete()
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                // --------------------------------------------------------------------------------------- //
                val remove = Kategori(title.text as String)
                CatDepositoryManager.instance.removeCat(remove)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(KategoriDetaljerLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun updateCollection(newCats:List<Kategori>){
        cats = newCats
        notifyDataSetChanged()
    }


}