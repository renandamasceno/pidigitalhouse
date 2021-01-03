package com.nasinha.digitalspace.bibliography.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.nasinha.digitalspace.R
import com.nasinha.digitalspace.exploration.utils.DrawerUtils.lockDrawer

class BibliographyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bibliography, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lockDrawer(requireActivity())

        view.findViewById<ImageButton>(R.id.btnBackBibliography).setOnClickListener {
            activity?.onBackPressed()
        }
    }
}