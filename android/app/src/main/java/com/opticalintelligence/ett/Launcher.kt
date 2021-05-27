package com.opticalintelligence.ett

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opticalintelligence.ett.ui.launcher.LauncherFragment

class Launcher : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // don't want to see the action bar
        supportActionBar?.hide();


        setContentView(R.layout.launcher_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LauncherFragment.newInstance())
                .commitNow()
        }
    }
}