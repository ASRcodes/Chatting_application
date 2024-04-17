package com.example.letschat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.letschat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mRefDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.hide()
        mRefDB = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()

        binding.goLogin.setOnClickListener{
            startActivity(Intent(this@SignUp,Login::class.java))
            finish()
        }
        binding.btnSignup.setOnClickListener {

            val name=binding.userName.text.toString()
            val email=binding.email.text.toString()
            val password=binding.password.text.toString()


            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Fill them first", Toast.LENGTH_SHORT).show()
            }
            else
                Signup(name,email,password)
        }
    }
    private fun Signup(name:String,email:String,password:String){
//        Logic for Sign-up
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    Adding to dataBase.....
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                startActivity(Intent(this@SignUp,HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignUp, "Error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserToDatabase(name:String,email: String,uid:String){
//        Adding user to dataBase
//        In this code a node will be created with the name user and it's child will be uid through which it will be identified
//        and then values are set
        mRefDB.child("user").child(uid).setValue(User(name,email,uid))
    }
}