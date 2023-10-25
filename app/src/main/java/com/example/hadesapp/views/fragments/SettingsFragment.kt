package com.example.hadesapp.views.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.hadesapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}