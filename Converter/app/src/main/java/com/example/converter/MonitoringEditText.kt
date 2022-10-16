package com.example.converter

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
//import android.widget.EditText
import android.widget.Toast

class MonitoringEditText : androidx.appcompat.widget.AppCompatEditText {
//class MonitoringEditText : EditText {
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
        val consumed: Boolean = super.onTextContextMenuItem(id)

        when (id) {
            android.R.id.cut -> onTextCut()
            android.R.id.copy -> onTextCopy()
            android.R.id.paste -> onTextPaste()
        }

        return consumed
    }

    private fun onTextCut() {
        Toast.makeText(context, "Cut!", Toast.LENGTH_SHORT).show()
    }

    private fun onTextCopy() {
        Toast.makeText(context, "Copy!", Toast.LENGTH_SHORT).show()
    }

    private fun onTextPaste() {
        Toast.makeText(context, "Paste!", Toast.LENGTH_SHORT).show()
    }
}