package com.example.converter

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.converter.databinding.FragmentNumpadBinding

class NumpadFragment : Fragment(), NumpadClickHandler {
    private var _binding: FragmentNumpadBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val dataViewModel: DataViewModel by activityViewModels()

    //private var dotPressed: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNumpadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.numpadRecyclerView
        // this also can be important
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = NumAdapter(this) // IMPORTANT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleClick(pressedKey: Char) {
        val inputConnection: InputConnection =
            dataViewModel.sourceInputConnection
        val currText: String = inputConnection.getExtractedText(
            ExtractedTextRequest(),
            0
        ).text.toString()

        if (pressedKey == 'C') {
            // need to delete a symbol here
            inputConnection.deleteSurroundingText(1, 0)
            inputConnection.commitText("", 1)
            return
        } else if (pressedKey == '.') {
            val hasDotAlready: Boolean = currText.findAnyOf(listOf(".")) != null

            if (hasDotAlready) {
                val errToast: Toast = Toast.makeText(
                    requireContext(),
                    "already has one dot!",
                    Toast.LENGTH_SHORT
                )
                errToast.show()
                return
            }
        } /*else if (pressedKey == '0') {

        }*/

        dataViewModel.sourceInputConnection.commitText(
            pressedKey.toString(),
            0
        )
    }

    override fun handleLongClick(pressedKey: Char) {
        if (pressedKey == 'C') {
            val inputConnection: InputConnection =
                dataViewModel.sourceInputConnection

            val currText: CharSequence = inputConnection.getExtractedText(ExtractedTextRequest(), 0).text
            val beforeCursorText: CharSequence =
                inputConnection.getTextBeforeCursor(currText.length, 0)!!
            /*val afterCursorText: CharSequence =
                inputConnection.getTextAfterCursor(currText.length, 0)!!*/
            inputConnection.deleteSurroundingText(
                beforeCursorText.length,
                0
            )
        }
    }
}