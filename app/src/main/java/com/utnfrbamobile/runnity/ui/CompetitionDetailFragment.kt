package com.utnfrbamobile.runnity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.utnfrbamobile.runnity.R
import org.w3c.dom.Text

class CompetitionDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_competition_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val participateButton: Button = view.findViewById(R.id.competeButton)
        val seeResultsLink: TextView = view.findViewById(R.id.seeResultsLink)
        participateButton.setOnClickListener{
            it.findNavController().navigate(R.id.action_competitionDetailFragment_to_permissionFragment)
        }
        seeResultsLink.setOnClickListener{
            it.findNavController().navigate(R.id.action_competitionDetailFragment_to_competitionResultsFragment)
        }
    }
}