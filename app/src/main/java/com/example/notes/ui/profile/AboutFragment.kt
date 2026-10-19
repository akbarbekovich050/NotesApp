package com.example.notes.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.notes.R
import com.example.notes.databinding.FragmentAboutBinding

/** Экран 13. О приложении. */
class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentAboutBinding.bind(view)
        val version = requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0).versionName
        b.version.text = getString(R.string.about_version, version)
    }
}
