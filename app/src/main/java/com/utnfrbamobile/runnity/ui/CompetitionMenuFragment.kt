package com.utnfrbamobile.runnity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.auth.SignUpViewModel
import com.utnfrbamobile.runnity.databinding.FragmentCompetitionMenuBinding
import com.utnfrbamobile.runnity.domain.Race
import com.utnfrbamobile.runnity.domain.RaceAdapter
import com.utnfrbamobile.runnity.domain.RaceParticipant
import com.utnfrbamobile.runnity.util.FirebaseWrapper

class CompetitionMenuFragment : Fragment() {
    private var _binding: FragmentCompetitionMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private val racesAdapter = RaceAdapter()

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentCompetitionMenuBinding.inflate(inflater, container, false)
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseWrapper.setCurrentActivity(requireActivity())

        val viewManager = LinearLayoutManager(this.context)
        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        recyclerView = binding.pendingRaceList.apply {
            layoutManager = viewManager
            adapter = racesAdapter
        }

        binding.newRaceButton.setOnClickListener(){
            findNavController().navigate(CompetitionMenuFragmentDirections.actionCompetitionMenuFragmentToNewCompetitionFragment())
        }
    }

    override fun onResume() {
        super.onResume()

        racesAdapter.data = emptyList()

        FirebaseWrapper.findRacesCreatedByAdversary(viewModel.email)

        FirebaseWrapper.findRacesCreatedByMe(viewModel.email).addOnSuccessListener { racesFinded ->
            if(!racesFinded.documents.isEmpty()){
                val transformDocumentToRace: (QueryDocumentSnapshot) -> Race = { raceDocument ->
                    formQueryDocumentSnapshotToRace(raceDocument)
                }

                val races: List<Race> = racesFinded.map(transformDocumentToRace)
                var mergedRaces: List<Race> = racesAdapter.data + races

                racesAdapter.data = mergedRaces
            }
        }

        FirebaseWrapper.findRacesCreatedByAdversary(viewModel.email).addOnSuccessListener { racesFinded ->
            if(!racesFinded.documents.isEmpty()) {
                val transformDocumentToRace: (QueryDocumentSnapshot) -> Race = { raceDocument ->
                    formQueryDocumentSnapshotToRace(raceDocument)
                }

                val races: List<Race> = racesFinded.map(transformDocumentToRace)
                var mergedRaces: List<Race> = racesAdapter.data + races

                racesAdapter.data = mergedRaces
            }
        }
    }

    fun formQueryDocumentSnapshotToRace( raceDocument: QueryDocumentSnapshot): Race {
        return Race(
            raceDocument.id,
            raceDocument.get("category") as Long,
            RaceParticipant(
                raceDocument.get("user1.email") as String,
                raceDocument.get("user1.name") as String,
                raceDocument.get("user1.duration") as Long,
                (raceDocument.get("user1.distance") as String).toFloat()
            ),
            RaceParticipant(
                raceDocument.get("user2.email") as String,
                raceDocument.get("user2.name") as String,
                raceDocument.get("user2.duration") as Long,
                (raceDocument.get("user2.distance") as String).toFloat()
            )
        )
    }
}