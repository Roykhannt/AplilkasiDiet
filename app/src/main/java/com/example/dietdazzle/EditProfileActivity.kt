package com.example.dietdazzle

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dietdazzle.SetProfile.FinalReport
import com.example.dietdazzle.databinding.ActivityEditProfileBinding
import com.example.dietdazzle.databinding.ActivityFinalReportBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfileActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users").child(currentUid)


        binding.btSave.setOnClickListener {
            val username= binding.etUsername.text.toString()
            val umur = binding.etUmur.text.toString().toInt()
            val weight = binding.etWeight.text.toString().toInt()
            val height = binding.etHeight.text.toString().toInt()

            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val gender = snapshot.child("gender").getValue(String::class.java)

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
                                return
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

                        saveDataKal(kalories , protein.toFloat(), karbohidrat.toFloat(), lemak.toFloat()    , serat)
                        updateDataBMI(weight, height, bmi, bmiResult, bodyGoals)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
                }
            })
            
            //Save data child kalori dan nutrisi

            updateData(umur,username)

            // update data ke database

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
    private fun updateData(umur :Int, username : String){
        val newValue = mapOf<String, Any>(
            "umur" to umur,
            "username" to username,
        )
        databaseRef.updateChildren(newValue)
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