package com.example.converter

interface NumpadClickHandler {
    fun handleClick(pressedKey: Char)

    fun handleLongClick(pressedKey: Char)
}