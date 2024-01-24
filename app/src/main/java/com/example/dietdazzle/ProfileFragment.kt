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
import androidx.cardview.widget.CardView
import com.example.dietdazzle.SetProfile.FinalReport
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var tvName : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)



        val tvumur = view.findViewById<TextView>(R.id.tv_umur)
        val tvuname = view.findViewById<TextView>(R.id.tv_username)
        val tvheight = view.findViewById<TextView>(R.id.tv_height)
        val tvweight = view.findViewById<TextView>(R.id.tv_weight)
        val btlogout= view.findViewById<FloatingActionButton>(R.id.btn_logout)
        val cdEdit= view.findViewById<CardView>(R.id.cd_edit)
        val profile = view.findViewById<CircleImageView>(R.id.profile_image)

        firebaseAuth = FirebaseAuth.getInstance()
        btlogout.setOnClickListener {
            firebaseAuth.signOut()
            if (firebaseAuth.currentUser == null) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

        cdEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users").child(currentUid)

        //get data 1
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val weight = snapshot.child("weight").getValue(Int::class.java)
                    val height = snapshot.child("height").getValue(Int::class.java)
                    val umur = snapshot.child("umur").getValue(Int::class.java)
                    val name = snapshot.child("username").getValue(String::class.java)

                    tvweight.text= weight.toString()
                    tvheight.text= height.toString()
                    tvumur.text = umur.toString()
                    tvuname.text= name.toString()


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