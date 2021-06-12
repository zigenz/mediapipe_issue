package com.opticalintelligence.ett.ui.eye_test

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.opticalintelligence.ett.R

class EyeTestFragment : Fragment() {

    companion object {
        fun newInstance() = EyeTestFragment()
    }

    private lateinit var viewModel: EyeTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.eye_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EyeTestViewModel::class.java)
        // TODO: Use the ViewModel
    }

}