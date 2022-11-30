package com.example.fantasyfootball

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fantasyfootball.databinding.OverviewFragmentBinding

class OverviewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // creating the binding root
        val binding = OverviewFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
}