package com.example.fantasyfootball

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fantasyfootball.databinding.ActivityAdviceBinding


class AdviceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

}