package com.utnfrbamobile.runnity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.auth.SignUpViewModel
import com.utnfrbamobile.runnity.databinding.FragmentNewCompetitionBinding
import com.utnfrbamobile.runnity.domain.AdversaryAdapter
import com.utnfrbamobile.runnity.domain.OnAdversarySelectedListener
import com.utnfrbamobile.runnity.domain.User
import com.utnfrbamobile.runnity.util.FirebaseWrapper

class NewCompetitionFragment : Fragment(), OnAdversarySelectedListener {
    private var _binding: FragmentNewCompetitionBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: SignUpViewModel

    private lateinit var emailSelected: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentNewCompetitionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseWrapper.setCurrentActivity(requireActivity())

        val viewManager = LinearLayoutManager(this.context)
        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        FirebaseWrapper.findAdversaries(viewModel.email).addOnSuccessListener { usersFinded ->
            val transformDocumentToUser: (QueryDocumentSnapshot) -> User = { userDocument ->
                var user = userDocument.toObject(User::class.java)
                user.email = userDocument.id
                user
            }

            val users = usersFinded.map(transformDocumentToUser)

            val adversaryAdapter = AdversaryAdapter(users, this)

            recyclerView = binding.adversariesList.apply {
                layoutManager = viewManager
                adapter = adversaryAdapter
            }
        }

        binding.createCompetitionButton.setOnClickListener(){
            when{
                emailSelected.isNullOrBlank() -> Toast.makeText(activity, R.string.adversary_unselected_message, Toast.LENGTH_SHORT).show()
                else -> FirebaseWrapper.createRace(viewModel.email, emailSelected, 2)
            }
        }
    }

    override fun onAdversarySelected(email: String) {
        emailSelected = email
    }
}