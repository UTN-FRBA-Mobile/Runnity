package com.utnfrbamobile.runnity.util

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.auth.LoginFragmentDirections
import com.utnfrbamobile.runnity.auth.SignUpStep1FragmentDirections
import com.utnfrbamobile.runnity.auth.SignUpViewModel

class FirebaseWrapper {
    companion object {

        private lateinit var currentActivity: FragmentActivity
        private lateinit var viewModel: SignUpViewModel
        private lateinit var navController: NavController

        private val db = FirebaseFirestore.getInstance()

        fun setCurrentActivity(curActivity: FragmentActivity){
            currentActivity = curActivity
        }

        fun setViewModel(model: SignUpViewModel){
            viewModel = model
        }

        fun setNavController(navigationController: NavController){
            navController = navigationController
        }

        fun authenticateUser(email: String, password: String) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    viewModel.email = email
                    db.collection("users").document(email).get().addOnSuccessListener {
                        if(it.exists()){
                            viewModel.name = it.get("name") as String
                            viewModel.birthdate = it.get("birthdate") as Long
                            viewModel.weight = it.get("weight") as String

                            navController.navigate(LoginFragmentDirections.actionLoginFragmentToCompetitionMenuFragment())
                        } else{
                            navController.navigate(LoginFragmentDirections.actionLoginFragmentToSignUpStep2Fragment())
                        }
                    }
                } else {
                    Toast.makeText(currentActivity, R.string.login_error_message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun createUser(email: String, password: String){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    viewModel.email = email
                    navController.navigate(SignUpStep1FragmentDirections.actionSignUpStep1FragmentToSignUpStep2Fragment())
                } else {
                    Toast.makeText(currentActivity, R.string.signup_error_message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun saveProfile(email: String, name: String, birthdate: Long, weight: String): Task<Void> {
            return db.collection("users").document(email).set(
                hashMapOf(
                    "name" to name,
                    "birthdate" to birthdate,
                    "weight" to weight
                )
            ).addOnCompleteListener {
                if (it.isSuccessful){
                    viewModel.name = name
                    viewModel.birthdate = birthdate
                    viewModel.weight = weight
                    Toast.makeText(currentActivity, R.string.profile_update_ok_message, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(currentActivity, R.string.profile_update_error_message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun findAdversaries(email: String): Task<QuerySnapshot> {
            return db
                .collection("users")
                .whereNotEqualTo(FieldPath.documentId(), email)
                .get()
        }

        fun createRace(myEmail: String, myName: String, adversaryEmail: String, adversaryName: String, category: Int){
            db.collection("races").add(
                hashMapOf(
                    "category" to category,
                    "user1" to hashMapOf(
                        "email" to myEmail,
                        "name" to myName,
                        "duration" to 0,
                        "distance" to "0"
                    ),
                    "user2" to hashMapOf(
                        "email" to adversaryEmail,
                        "name" to adversaryName,
                        "duration" to 0,
                        "distance" to "0"
                    )
                )
            ).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(currentActivity, R.string.race_creation_ok_message, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(currentActivity, R.string.race_creation_error_message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun findRace(fieldPath: String, email: String): Task<QuerySnapshot> {
            return db
                .collection("races")
                .whereEqualTo(fieldPath, email)
                .get()
        }

        fun findRacesCreatedByMe(email: String): Task<QuerySnapshot> {
            return findRace("user1.email", email)
        }

        fun findRacesCreatedByAdversary(email: String): Task<QuerySnapshot> {
            return findRace("user2.email", email)
        }
    }
}