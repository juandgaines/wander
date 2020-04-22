package com.example.wander.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.wander.R
import com.example.wander.databinding.BottomSheetBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddDialogPosition private constructor (val position: LatLng, val onSuccess: () -> Unit) :BottomSheetDialogFragment() {


    private lateinit var binding:BottomSheetBinding
    companion object {
        fun newInstance(position: LatLng, onSuccess: ()->Unit): AddDialogPosition =
            AddDialogPosition(position,onSuccess).apply {
                /*
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
                 */
            }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.bottom_sheet, container, false)

        binding.buttonNo.setOnClickListener {
            dismiss()
        }

        binding.buttonYes.setOnClickListener {
            onSuccess
        }
        return binding.root
    }


}