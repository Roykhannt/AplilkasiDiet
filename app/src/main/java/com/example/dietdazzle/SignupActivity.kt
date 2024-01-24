package com.example.dietdazzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dietdazzle.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance("https://dietdazzle-3dbce-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseRef = firebaseDatabase.getReference("Users")


        binding.btSignup.setOnClickListener {
            val hashMap: HashMap<String, Any> = HashMap()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val confirmPass = binding.etConfpassword.text.toString()
            val username = binding.etUsername.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && username.isNotEmpty()){
                if (pass ==  confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener {
                        if(it.isSuccessful){

                            hashMap["username"]=username
                            hashMap["email"]=email
                            hashMap["gender"]=""
                            hashMap["umur"] = ""
                            hashMap["foto"] = ""
                            hashMap["goal"] = ""
                            hashMap["dob"] = ""
                            hashMap["weight"] = ""
                            hashMap["height"] = ""

                            databaseRef.child(it.result.user?.uid.toString()).setValue(hashMap).addOnSuccessListener {
                                Log.d("FirebaseDatabase", "Berhasil Register")
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else{
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "Empty Fileds Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}