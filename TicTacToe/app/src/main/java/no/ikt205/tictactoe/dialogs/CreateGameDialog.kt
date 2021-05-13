package no.ikt205.tictactoe.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import no.ikt205.tictactoe.databinding.DialogCreateGameBinding
import java.lang.ClassCastException


class CreateGameDialog() : DialogFragment() {

    internal lateinit var listener:GameDialogListener

    //Here i create the dialogbox that allows the user to create a game.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            //Setup needed for later
            val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogCreateGameBinding.inflate(inflater)

            //Sets the name of the dialog box and the name of its choices.
            //Then sets the name of the player (inserted by the user)
            builder.apply {
                setTitle("Create game")
                setPositiveButton("Create") { _, _ ->
                    //Checks if the players insertet anything, if nothing, then nothing happens.
                    if(binding.username.text.toString() != ""){
                        listener.onDialogCreateGame(binding.username.text.toString())
                    }
                }
                //The ability to cancel
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                setView(binding.root)
            }
            builder.create()

            //Stops the code if "null" is inserted, mainly just used to guard against possible bugs.
        } ?: throw IllegalStateException("Cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as GameDialogListener
        } catch (e:ClassCastException){
            throw ClassCastException(("$context missing GameDialogListener"))
        }
    }
}
