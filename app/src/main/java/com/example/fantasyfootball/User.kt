package com.example.fantasyfootball

// Null default values create a no-argument default constructor, which is needed
// for deserialization from a DataSnapshot.

data class User(val id: String? = null, val email: String? = null)
