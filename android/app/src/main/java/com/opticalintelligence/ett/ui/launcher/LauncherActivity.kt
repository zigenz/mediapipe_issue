package com.opticalintelligence.ett.ui.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opticalintelligence.ett.R

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // don't want to see the action bar
        supportActionBar?.hide();

        setContentView(R.layout.activity_launcher)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LauncherFragment.newInstance())
                .commitNow()
        }
    }
}