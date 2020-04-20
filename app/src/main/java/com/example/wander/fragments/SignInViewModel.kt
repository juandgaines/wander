package com.example.wander.fragments

import android.util.Patterns
import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel() {



    fun isEmailValid(email: CharSequence?):Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
