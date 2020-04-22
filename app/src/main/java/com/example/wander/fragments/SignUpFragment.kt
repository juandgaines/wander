package com.example.wander.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wander.MapsActivity
import com.example.wander.R
import com.example.wander.databinding.SignUpFragmentBinding
import com.example.wander.network.Network
import com.example.wander.network.Result
import com.example.wander.preferences.PreferencesManager

class SignUpFragment : Fragment() {


    private lateinit var viewModel: SignUpViewModel
    private lateinit var mapsActivity: MapsActivity
    private lateinit var binding: SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding.viewModel = viewModel
        mapsActivity = requireActivity() as MapsActivity

        binding.goSignIn.setOnClickListener {

            viewModel.registerUser()

        }

        viewModel.response.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    PreferencesManager.getPreferenceProvider(requireContext()).idUser=result.data?.id!!
                    mapsActivity.navController.navigate(R.id.action_signUpFragment_to_signInFragment)
                }
                is Result.Error -> {
                    Toast.makeText(mapsActivity, result.exception, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            when (it) {
                Network.NetworkState.SUCCESS,
                Network.NetworkState.ERROR -> {
                    mapsActivity.hideLoading()
                }
                else -> {
                    mapsActivity.showLoading()
                }
            }
        })

    }

}
