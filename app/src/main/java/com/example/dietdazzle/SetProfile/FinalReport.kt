package com.example.dietdazzle.SetProfile

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dietdazzle.HomeActivity
import com.example.dietdazzle.LoginActivity
import com.example.dietdazzle.databinding.ActivityFinalReportBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FinalReport : AppCompatActivity() {

    private lateinit var binding: ActivityFinalReportBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFinalReportBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btDetails.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users").child(currentUid)


        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val bmi = snapshot.child("bmi").getValue(Float::class.java)
                    val weight = snapshot.child("weight").getValue(Int::class.java)
                    val height = snapshot.child("height").getValue(Int::class.java)
                    val bmiResult = snapshot.child("bmiResult").getValue(String::class.java)
                    val umur = snapshot.child("umur").getValue(Int::class.java)
                    val bodyGoals = snapshot.child("bodyGoals").getValue(Int::class.java)

                    binding.tvBmi.text ="%.1f".format(bmi)
                    if (bmi != null) {
                        binding.circularProgressIndicator.progress = bmi.toInt()
                    }
                    binding.circularProgressIndicator.max = 40
                    binding.tvWeight.text= weight.toString()
                    binding.tvHeight.text= height.toString()
                    binding.tvUmur.text = umur.toString()
                    binding.tvBodygoals.text = bodyGoals.toString()

                    when(bmiResult) {
                        "Underweight" -> binding.tvResultbmi.text = "You have an underweight body"
                        "Ideal weight" -> binding.tvResultbmi.text = "You have normal body weight"
                        "Overweight" -> binding.tvResultbmi.text = "You have an overweight body"
                        "Obesity" -> binding.tvResultbmi.text = "You have an obese body"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
            }
        })

    }
}