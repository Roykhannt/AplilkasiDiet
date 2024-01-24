package com.example.dietdazzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.dietdazzle.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = firebaseAuth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(currentUid)

        //check data untuk masuk ke halaman setprofile
        databaseRef.get().addOnSuccessListener {
            if (it.child("goal").getValue(String::class.java)!!.isEmpty()) {
                val intent = Intent(this, SetProfileActivity::class.java)
                startActivity(intent)
            }
            else{
                loadFragment(HomeFragment())
            }
        }

        binding.btNavigation.setOnItemSelectedListener{
            when (it.itemId){
                R.id.menu1 -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.menu2 -> {
                    loadFragment(ReportFragment())
                    true
                }
                else -> {
                    loadFragment(ProfileFragment())
                    true
                }

            }
            true
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container,fragment)
        transaction.commit()
    }
}