package com.example.dietdazzle.SetProfile

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.dietdazzle.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ProfileDobFrag : Fragment() {

    private lateinit var textViewDate: TextView
    private lateinit var btn_calender : ImageView
    private lateinit var btn_next : FloatingActionButton
    private lateinit var et_age : EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_dob, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewDate = view.findViewById(R.id.textViewDate)
        btn_calender = view.findViewById(R.id.btn_calender)
        btn_next = view.findViewById(R.id.btn_next)
        et_age = view.findViewById(R.id.et_age)

        btn_calender.setOnClickListener {
            showDatePickerDialog()
        }

        btn_next.setOnClickListener {

            val selectedDate = textViewDate.text.toString()
            val umur = et_age.text.toString().toInt()
            updateDataDOB(umur, selectedDate)

            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.pro_fragment, ProfileWeHeFrag())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users").child(currentUid)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                textViewDate.text = SimpleDateFormat("MMMM / d / yyyy", Locale.getDefault()).format(selectedDate.time)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
    private fun updateDataDOB(umur :Int, dob : String){
        val newValue = mapOf<String, Any>(
            "umur" to umur,
            "dob" to dob
        )
        databaseRef.updateChildren(newValue)
    }


    }