package com.utnfrbamobile.runnity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.domain.PendingRace
import com.utnfrbamobile.runnity.domain.PendingRaceAdapter

class CompetitionMenuFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val adapter = PendingRaceAdapter()
        adapter.data = listOf(
            PendingRace(1,"5K", "Esteban Quito"),
            PendingRace(2, "5K", "Usain Bolt"),
            PendingRace(2, "2K", "Otro Nombre"),
            PendingRace(2, "10K", "Franco Curi")
        )
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.VISIBLE
        val competitionMenuView: View = inflater.inflate(R.layout.fragment_competition_menu, container, false)
        competitionMenuView.findViewById<RecyclerView>(R.id.pendingRaceList).adapter = adapter
        return competitionMenuView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LinearLayout>(R.id.newRaceButton).setOnClickListener(){
            db.collection("races").document(1.toString()).set(
                hashMapOf(
                    "category" to 2,
                    "user1" to hashMapOf("email" to "asd@asd.com", "duration" to 400, "distance" to 1500),
                    "user2" to hashMapOf("email" to "fer@gmail.com", "duration" to 500, "distance" to 1600)
                )
            ).addOnCompleteListener {

                Toast.makeText(
                    activity,
                    if (it.isSuccessful) R.string.profile_updated_ok_message else R.string.signup_error_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}