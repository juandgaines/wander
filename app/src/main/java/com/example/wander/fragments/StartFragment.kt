package com.example.wander.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wander.MapsActivity
import com.example.wander.R
import com.example.wander.databinding.StartFragmentBinding
import com.example.wander.preferences.PreferencesManager

class StartFragment : Fragment() {
    private lateinit var viewModel: StartViewModel
    private lateinit var binding: StartFragmentBinding
    private lateinit var mapsActivity: MapsActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.start_fragment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

        mapsActivity = requireActivity() as MapsActivity

        binding.signUp.setOnClickListener {
            mapsActivity.navController.navigate(R.id.action_startFragment_to_signUpFragment)
        }
        binding.signIn.setOnClickListener {
            mapsActivity.navController.navigate(R.id.action_startFragment_to_signInFragment)
        }

        if(PreferencesManager.getPreferenceProvider(requireContext()).token.isNullOrEmpty().not()){
            mapsActivity.navController.navigate(R.id.action_startFragment_to_mapFragment)
        }
    }

}
