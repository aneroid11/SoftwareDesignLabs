package com.example.converter

import android.content.Context
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem

class NoMenuEditText(context: Context) : androidx.appcompat.widget.AppCompatEditText(context) {
    init {
        init()
    }

    override fun isSuggestionsEnabled(): Boolean {
        return false
    }

    fun canPaste(): Boolean {
        return false
    }

    private fun init() {
        isLongClickable = false
        customSelectionActionModeCallback = ActionModeCallbackInterceptor()
    }

    private class ActionModeCallbackInterceptor : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
        }
    }
}