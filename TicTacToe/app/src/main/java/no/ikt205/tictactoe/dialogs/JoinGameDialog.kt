package no.ikt205.tictactoe.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import no.ikt205.tictactoe.databinding.DialogJoinGameBinding
import java.lang.ClassCastException

class JoinGameDialog() : DialogFragment() {

    private lateinit var listener:GameDialogListener
//Code is very similar to "CreateGameDialog" and as such comments will be mostly the same
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogJoinGameBinding.inflate(inflater)

            builder.apply {
                setTitle("Join game")
                setPositiveButton("Join") { _, _ ->
                    if(binding.username.text.toString() != ""){
                        //Need to add extra "binding.gameId.text.toString()" to have a dynamic gameId system
                        //Where the user can insert the gameId needed.
                        listener.onDialogJoinGame(binding.username.text.toString(), binding.gameId.text.toString())
                    }
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                setView(binding.root)
            }

            builder.create()


        } ?: throw IllegalStateException("Cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as GameDialogListener
        } catch (e: ClassCastException){
            throw ClassCastException(("$context missing GameDialogListener"))

        }
    }
}