package com.example.converter

import android.content.Context
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem

class NoMenuEditText : androidx.appcompat.widget.AppCompatEditText {
    private var noMenuContext: Context

    constructor(context: Context) : super(context) {
        noMenuContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs) {
        noMenuContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            : super(context, attrs, defStyle) {
        noMenuContext = context
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