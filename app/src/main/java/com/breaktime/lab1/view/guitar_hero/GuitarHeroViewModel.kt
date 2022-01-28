package com.breaktime.lab1.view.guitar_hero

import androidx.lifecycle.ViewModel
import com.breaktime.lab1.R
import com.breaktime.lab1.util.ResourcesProvider

class GuitarHeroViewModel(private val resProvider: ResourcesProvider) : ViewModel() {
    private var counter = 0
    val counterText: String
        get() = resProvider.getString(R.string.counter, counter)

    fun increaseCounter() {
        counter++
    }
}