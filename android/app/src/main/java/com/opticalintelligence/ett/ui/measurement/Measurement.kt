package com.opticalintelligence.ett.ui.measurement

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.opticalintelligence.ett.R

class Measurement : Fragment() {

    companion object {
        fun newInstance() = Measurement()
    }

    private lateinit var viewModel: MeasurementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.measurement_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MeasurementViewModel::class.java)
        // TODO: Use the ViewModel
    }

}