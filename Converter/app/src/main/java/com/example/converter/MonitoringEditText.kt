package com.example.converter

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.util.AttributeSet
import android.widget.Toast

class MonitoringEditText : androidx.appcompat.widget.AppCompatEditText {
    private var monContext: Context

    constructor(context: Context) : super(context) {
        monContext = context
    }

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs) {
        monContext = context
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            : super(context, attrs, defStyle) {
        monContext = context
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.paste -> {
                if (!onTextPaste()) {
                    return false
                }
            }
            android.R.id.addToDictionary -> {
                Toast.makeText(context, "that was a clickbait", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return super.onTextContextMenuItem(id)
    }

    /*private fun onTextCut() {
        Toast.makeText(context, "Cut!", Toast.LENGTH_SHORT).show()
    }

    private fun onTextCopy() {
        Toast.makeText(context, "Copy!", Toast.LENGTH_SHORT).show()
    }*/

    private fun checkForDigitsAndDots(str: String): Boolean {
        for (ch: Char in str) {
            if (ch != '.' && !ch.isDigit()) {
                return false
            }
        }

        return true
    }

    private fun onTextPaste(): Boolean {
        val clipboardManager: ClipboardManager =
            context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val pData = clipboardManager.primaryClip
        val item = pData!!.getItemAt(0)
        val txtPaste: String = item.text.toString()

        val txtCurrent: String = text.toString()

        if (checkForDigitsAndDots(txtPaste)) {
            val currTextHasDot: Boolean = txtCurrent.findAnyOf(listOf(".")) != null
            val pasteTextHasDot: Boolean = txtPaste.findAnyOf(listOf(".")) != null

            if (currTextHasDot && pasteTextHasDot) {
                Toast.makeText(
                    context,
                    "already has one dot!",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        else {
            Toast.makeText(
                context,
                "the text you're trying to paste has invalid characters!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }
}