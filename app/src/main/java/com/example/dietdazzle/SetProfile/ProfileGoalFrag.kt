package com.example.dietdazzle.SetProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.dietdazzle.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileGoalFrag : Fragment() {

    private lateinit var btn_next : FloatingActionButton
    private lateinit var cdLose : CardView
    private lateinit var cdGain : CardView
    private lateinit var cdStay : CardView
    private lateinit var tv_sh : TextView
    private lateinit var tv_lw : TextView
    private lateinit var tv_gw : TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    var selectedValue = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_next = view.findViewById(R.id.btn_next)
        cdLose = view.findViewById(R.id.cdLose)
        cdGain = view.findViewById(R.id.cdGain)
        cdStay = view.findViewById(R.id.cdStay)
        tv_lw  = view.findViewById(R.id.tv_lw)
        tv_gw  = view.findViewById(R.id.tv_gw)
        tv_sh  = view.findViewById(R.id.tv_sh)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users").child(currentUid)

        btn_next.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.pro_fragment, ProfileGenderFrag())
            transaction?.addToBackStack(null)
            transaction?.commit()
            updateDataGoal(selectedValue)
        }


        cdStay.setOnClickListener{
            selectedValue = tv_sh.text.toString()
        }
        cdGain.setOnClickListener{
            selectedValue = tv_gw.text.toString()
        }
        cdLose.setOnClickListener{
            selectedValue = tv_lw.text.toString()
        }
    }

    private fun updateDataGoal(goal :String){
        val newValue = goal
        databaseRef.child("goal").setValue(newValue)

    }
}