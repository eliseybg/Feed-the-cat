package com.breaktime.lab1.view.guitar_hero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.breaktime.lab1.databinding.FragmentGuitarHeroBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GuitarHeroFragment : Fragment() {
    private lateinit var binding: FragmentGuitarHeroBinding
    private val viewModel: GuitarHeroViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuitarHeroBinding.inflate(
            inflater, container, false
        ).apply {
            counter.text = viewModel.counterText
            guitarhero.setOnDotClickListener {
                viewModel.increaseCounter()
                counter.text = viewModel.counterText
            }
        }
        return binding.root
    }
}