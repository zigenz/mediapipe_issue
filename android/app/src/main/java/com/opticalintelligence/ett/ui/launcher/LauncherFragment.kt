package com.opticalintelligence.ett.ui.launcher

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.opticalintelligence.ett.MainActivity
import com.opticalintelligence.ett.R
import com.opticalintelligence.ett.ui.LauncherButton

class LauncherFragment : Fragment() {

    companion object {
        private const val TAG = "LauncherFragment"

        fun newInstance() = LauncherFragment()
    }

    private lateinit var viewModel: LauncherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.launcher_fragment, container, false)

        val invokeEyeTest = view?.findViewById<LauncherButton>(R.id.eye_test_btn)

        invokeEyeTest?.setOnClickListener {
            Log.v(TAG, "Eye Test button clicked!")
            val intent = Intent( this.activity, MainActivity::class.java )
            startActivity( intent )
        }

        return view;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LauncherViewModel::class.java)
        // TODO: Use the ViewModel


    }

}