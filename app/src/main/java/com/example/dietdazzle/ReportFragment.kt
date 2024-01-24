package com.example.dietdazzle

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DateFormat
import java.time.LocalTime
import java.util.Calendar

class ReportFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        val progresBMI = view.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator)
        val tvresulBmi = view.findViewById<TextView>(R.id.tv_resultbmi)
        val tvbmi = view.findViewById<TextView>(R.id.tv_bmi)
        val tvumur = view.findViewById<TextView>(R.id.tv_umur)
        val tvheight = view.findViewById<TextView>(R.id.tv_height)
        val tvweight = view.findViewById<TextView>(R.id.tv_weight)
        val tvgoals = view.findViewById<TextView>(R.id.tv_bodygoals)
        val tvkalorie = view.findViewById<TextView>(R.id.tv_kalorie)
        val tvkarbo = view.findViewById<TextView>(R.id.tv_karbohidrat)
        val tvprotein = view.findViewById<TextView>(R.id.tv_protein)
        val tvlemak = view.findViewById<TextView>(R.id.tv_lemak)
        val tvserat = view.findViewById<TextView>(R.id.tv_serat)


        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users").child(currentUid)

        //get data 1
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val bmi = snapshot.child("bmi").getValue(Float::class.java)
                    val bmiResult = snapshot.child("bmiResult").getValue(String::class.java)
                    val kalorie = snapshot.child("rekomendasi").child("kalories").getValue(Int::class.java)
                    val karbo = snapshot.child("rekomendasi").child("karbohidrat").getValue(Int::class.java)
                    val protein = snapshot.child("rekomendasi").child("protein").getValue(Int::class.java)
                    val lemak = snapshot.child("rekomendasi").child("lemak").getValue(Int::class.java)
                    val serat = snapshot.child("rekomendasi").child("serat").getValue(Int::class.java)
                    val weight = snapshot.child("weight").getValue(Int::class.java)
                    val height = snapshot.child("height").getValue(Int::class.java)
                    val umur = snapshot.child("umur").getValue(Int::class.java)
                    val bodyGoals = snapshot.child("bodyGoals").getValue(Int::class.java)

                    tvbmi.text ="%.1f".format(bmi)
                    if (bmi != null) {
                        progresBMI.progress = bmi.toInt()
                    }
                    progresBMI.max = 40
                    tvweight.text= weight.toString()
                    tvheight.text= height.toString()
                    tvumur.text = umur.toString()
                    tvgoals.text = bodyGoals.toString()
                    tvkalorie.text= kalorie.toString()
                    tvkarbo.text= karbo.toString()
                    tvprotein.text=protein.toString()
                    tvlemak.text=lemak.toString()
                    tvserat.text=serat.toString()

                    when(bmiResult) {
                        "Underweight" -> tvresulBmi.text = "You have an underweight body"
                        "Ideal weight" -> tvresulBmi.text = "You have normal body weight"
                        "Overweight" -> tvresulBmi.text = "You have an overweight body"
                        "Obesity" -> tvresulBmi.text = "You have an obese body"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
            }
        })

        return view
    }

}