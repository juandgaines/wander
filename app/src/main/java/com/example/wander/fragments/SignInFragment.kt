package com.example.wander.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wander.MapsActivity
import com.example.wander.R
import com.example.wander.databinding.SignInFragmentBinding

class SignInFragment : Fragment() {


    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: SignInFragmentBinding
    private lateinit var mapsActivity: MapsActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.sign_in_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        mapsActivity = requireActivity() as MapsActivity

        binding.lifecycleOwner=this
        binding.viewModel=viewModel

        // mapsActivity.navController.navigate(R.id.action_signInFragment_to_mapFragment)

    }

}
