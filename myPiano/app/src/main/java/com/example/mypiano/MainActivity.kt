package com.example.mypiano

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.example.mypiano.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    private val TAG:String = "MyPiano:MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var piano:PianoLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        signInAnonymously()
        piano = supportFragmentManager.findFragmentById(binding.piano.id) as PianoLayout

        piano.onSave = {
            this.upload(it)
        }
    }

    private fun upload(file:Uri){

        val ref = FirebaseStorage.getInstance().reference.child("melodies/${file.lastPathSegment}")
        var uploadTask = ref.putFile(file)
        uploadTask.addOnSuccessListener {
            Log.d(TAG, "Lagret fil ${it.toString()}")
        }.addOnFailureListener {
            Log.e(TAG, "Error i lagring", it)
        }
        //T
    }

    private fun signInAnonymously(){
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Login success ${it.user.toString()}")
        }.addOnFailureListener {
            Log.e(TAG, "Login failed", it)
        }
    }
}