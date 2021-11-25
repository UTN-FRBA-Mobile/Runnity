package com.utnfrbamobile.runnity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.domain.PendingRace
import com.utnfrbamobile.runnity.domain.PendingRaceAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CompetitionMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompetitionMenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this.context)
        //linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adapter = PendingRaceAdapter()
        adapter.data = listOf(PendingRace(1,"5K", "Esteban Quito"),
            PendingRace(2, "5K", "Usain Bolt"),
            PendingRace(2, "2K", "Otro Nombre"),
            PendingRace(2, "10K", "Franco Curi"))
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.VISIBLE
        val competitionMenuView: View = inflater.inflate(R.layout.fragment_competition_menu, container, false)
        competitionMenuView.findViewById<RecyclerView>(R.id.pendingRaceList).adapter = adapter
        return competitionMenuView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CompetitionMenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CompetitionMenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}