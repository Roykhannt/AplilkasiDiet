package com.example.dietdazzle.SetProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.dietdazzle.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileGenderFrag : Fragment() {

    private lateinit var btn_next : FloatingActionButton
    private lateinit var cdMale : CardView
    private lateinit var cdFemale : CardView
    private lateinit var tv_male : TextView
    private lateinit var tv_female : TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    var selectedValue = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_gender, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_next = view.findViewById(R.id.btn_next)
        cdMale = view.findViewById(R.id.cdMale)
        cdFemale = view.findViewById(R.id.cdFemale)
        tv_male  = view.findViewById(R.id.tv_male)
        tv_female  = view.findViewById(R.id.tv_female)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users").child(currentUid)

        btn_next.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.pro_fragment, ProfileDobFrag())
            transaction?.addToBackStack(null)
            transaction?.commit()
            updateDataGender(selectedValue)
        }

        cdMale.setOnClickListener{
            selectedValue = tv_male.text.toString()
        }
        cdFemale.setOnClickListener{
            selectedValue = tv_female.text.toString()
        }
    }

    private fun updateDataGender(gender :String){
        val newValue = gender
        databaseRef.child("gender").setValue(newValue)

    }


}