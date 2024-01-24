package com.example.dietdazzle.SetProfile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.dietdazzle.R
import com.example.dietdazzle.SetProfileActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileWeHeFrag : Fragment() {

    private lateinit var btn_next: FloatingActionButton
    private lateinit var et_weight: EditText
    private lateinit var et_height: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private var gender: String = ""
    private var umur = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_we_he, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_next = view.findViewById(R.id.btn_next)
        et_height = view.findViewById(R.id.et_height)
        et_weight = view.findViewById(R.id.et_weight)

        //inisialisinya
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(currentUid)

        // read data gender
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gender = snapshot.child("gender").getValue(String::class.java) ?: ""
                umur = snapshot.child("umur").getValue(Int::class.java) ?: 0
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        btn_next.setOnClickListener {

            val weight = et_weight.text.toString().toInt()
            val height = et_height.text.toString().toInt()

            if (weight <= 0 || height <= 0) {
                Toast.makeText(context, "Please enter valid weight and height", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val bmi = weight.toFloat() / (height.toFloat() / 100 * height.toFloat() / 100)

            val bmiResult = when {
                bmi < 18.5 -> "Underweight"
                bmi >= 18.5 && bmi <= 24.9 -> "Ideal weight"
                bmi >= 25 && bmi <= 29.9 -> "Overweight"
                else -> "Obesity"
            }
            val bodyGoals = (height.toFloat() - 100) - ((height.toFloat() - 100) * 10 / 100)

            val kalories = when (gender) {
                "Male" -> {
                    when {
                        bmi < 18.5 -> 2200
                        bmi >= 18.5 && bmi <= 24.9 -> 2000
                        bmi >= 25 && bmi <= 29.9 -> 1800
                        else -> 1500
                    }
                }

                "Female" -> {
                    when {
                        bmi < 18.5 -> 1900
                        bmi >= 18.5 && bmi <= 24.9 -> 1700
                        bmi >= 25 && bmi <= 29.9 -> 1500
                        else -> 1200
                    }
                }
                else -> {
                    Toast.makeText(context, "Gender Not Found", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val protein = when{
                bmi < 18.5 -> 1.2*weight
                bmi >= 18.5 && bmi <= 24.9 -> 1.0*weight
                bmi >= 25 && bmi <= 29.9 -> 1.0*weight
                else -> 1.0*weight
            }
            val karbohidrat = 0.5*kalories
            val lemak= 0.3*kalories
            val serat= 30
            //Save data child kalori dan nutrisi

            saveDataKal(kalories , protein.toFloat(), karbohidrat.toFloat(), lemak.toFloat()  , serat)

            // update data ke database
            updateDataBMI(weight, height, bmi, bmiResult, bodyGoals)

            val intent = Intent(requireContext(), FinalReport::class.java)
            startActivity(intent)

        }
    }

    private fun updateDataBMI(
        weight: Int,
        height: Int,
        bmi: Float,
        bmiResult: String,
        bodyGoals: Float
    ) {
        val newValue = mapOf<String, Any>(
            "weight" to weight,
            "height" to height,
            "bmi" to bmi,
            "bmiResult" to bmiResult,
            "bodyGoals" to bodyGoals
        )
        databaseRef.updateChildren(newValue)
    }

    private fun saveDataKal(kalories: Int, protein: Float, karbohidrat: Float, lemak: Float, serat: Int){

        val goalValue= mapOf<String,Any>(
            "kalories" to kalories,
            "protein" to protein,
            "karbohidrat" to karbohidrat,
            "lemak" to lemak,
            "serat" to serat
        )
        databaseRef.child("rekomendasi").updateChildren(goalValue)
    }

}