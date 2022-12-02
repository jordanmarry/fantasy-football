package com.example.fantasyfootball
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fantasyfootball.databinding.MainFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import java.util.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AdviceFragment: Fragment() {

    private lateinit var database: DatabaseReference
    private var writer = ReadAndWriteData()




}