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

    private lateinit var id: String
    private var category: Long = 0
    private lateinit var user1Email: String
    private lateinit var user1Name: String
    private var user1Duration: Long = 0
    private var user1Distance: Float = 0F
    private lateinit var user2Email: String
    private lateinit var user2Name: String
    private var user2Duration: Long = 0
    private var user2Distance: Float = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        id = arguments?.getString("id")!!
        category = arguments?.getLong("category")!!
        user1Email = arguments?.getString("user1Email")!!
        user1Name = arguments?.getString("user1Name")!!
        user1Duration = arguments?.getLong("user1Duration")!!
        user1Distance = arguments?.getFloat("user1Distance")!!
        user2Email = arguments?.getString("user2Email")!!
        user2Name = arguments?.getString("user2Name")!!
        user2Duration = arguments?.getLong("user2Duration")!!
        user2Distance = arguments?.getFloat("user2Distance")!!

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