package com.example.dietdazzle

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DateFormat
import java.time.LocalTime
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize the TextView for displaying the date
        val textViewDateTime = view.findViewById<TextView>(R.id.datetime)
        val profile = view.findViewById<CircleImageView>(R.id.profile_image)
        val tvkalori = view.findViewById<TextView>(R.id.tv_kalorie)
        val progresKal = view.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator)
        val tvresulBmi = view.findViewById<TextView>(R.id.tv_resultBmi)
        val tvbmi = view.findViewById<TextView>(R.id.tv_bmi)
        val btvideo = view.findViewById<AppCompatButton>(R.id.bt_video)


        btvideo.setOnClickListener { v ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=y5OabVgga0E"))
            startActivity(intent)
        }

        // Get the current date and format it
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)
        // Update the TextView with the current date
        textViewDateTime.text = currentDate

        val greetingText = view.findViewById<TextView>(R.id.tv_greeting)
        val currentHour = LocalTime.now().hour

        when {
            currentHour in 4..10 -> greetingText.text = "Good Morning"
            currentHour in 11..15 -> greetingText.text = "Good Afternoon"
            currentHour in 16..19 -> greetingText.text = "Good Evening"
            currentHour in 20..23 || currentHour in 0..3 -> greetingText.text = "Good Night"
        }
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

                    tvbmi.text= "%.1f".format(bmi)
                    tvresulBmi.text= bmiResult
                    tvkalori.text= kalorie.toString()

                    if (kalorie != null) {
                        progresKal.progress = (kalorie.toInt()*100)/2200
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