package com.example.mypiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mypiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.fullToneKey
import kotlinx.android.synthetic.main.fragment_piano.view.*

class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2", "A2", "B2")
    private val semiTones = listOf("C#", "D#", "F#", "G#", "A#", "C#2", "D#2", "F#2", "G#2", "A#2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()


        fullTones.forEach{
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)

            fullTonePianoKey.onKeyDown = {
                println("White key down $it")
            }
            fullTonePianoKey.onKeyUp = {
                println("White key up $it")
            }

            ft.add(view.fullToneKey.id, fullTonePianoKey, "note_$it")
        }

        semiTones.forEach{
            val semiTonePianoKey = SemiTonePianoKeyFragment.newInstance(it)

            semiTonePianoKey.onKeyDown = {
                println("Semi key down $it")
            }
            semiTonePianoKey.onKeyUp = {
                println("Semi key up $it")
            }

            ft.add(view.semiToneKey.id, semiTonePianoKey, "note_$it")
        }

        ft.commit()
        return inflater.inflate(R.layout.fragment_piano, container, false)
    }

}