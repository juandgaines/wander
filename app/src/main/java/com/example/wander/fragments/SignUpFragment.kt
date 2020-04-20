package com.example.wander.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.wander.MapsActivity

import com.example.wander.R
import com.example.wander.databinding.SignUpFragmentBinding
import com.example.wander.databinding.StartFragmentBinding

class SignUpFragment : Fragment() {


    private lateinit var viewModel: SignUpViewModel
    private lateinit var mapsActivity: MapsActivity
    private lateinit var  binding:SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        mapsActivity = requireActivity() as MapsActivity

        binding.goSignIn.setOnClickListener {
            mapsActivity.navController.navigate(R.id.action_signUpFragment_to_mapFragment)
        }

    }

}
