package com.example.dietdazzle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.dietdazzle.SetProfile.ProfileDobFrag
import com.example.dietdazzle.SetProfile.ProfileGenderFrag
import com.example.dietdazzle.SetProfile.ProfileGoalFrag
import com.example.dietdazzle.SetProfile.ProfileNameFrag
import com.example.dietdazzle.SetProfile.ProfileWeHeFrag
import com.example.dietdazzle.databinding.ActivityHomeBinding
import com.example.dietdazzle.databinding.ActivitySetProfileBinding
import com.example.dietdazzle.databinding.FragmentProfileGoalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SetProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetProfileBinding
    private lateinit var bindingGoal : FragmentProfileGoalBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(ProfileNameFrag())

//        binding.btnNext.setOnClickListener {
//            onNextButtonClicked()
//        }

        //Cek database users
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users").child(currentUid)

    }
    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.pro_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadFragment(fragment: Fragment) {
        setFragment(fragment)
    }


}