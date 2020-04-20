package com.example.wander.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.wander.MapsActivity

import com.example.wander.R
import com.example.wander.databinding.StartFragmentBinding

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel
    private lateinit var binding:StartFragmentBinding
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

        mapsActivity=requireActivity() as MapsActivity

        binding.buttonNav.setOnClickListener {
            mapsActivity.navController.navigate(R.id.action_startFragment_to_mapFragment)
        }
    }

}
