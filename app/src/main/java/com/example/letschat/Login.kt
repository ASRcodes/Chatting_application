package com.example.letschat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.letschat.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener {
            val email=binding.email.text.toString()
            val password=binding.password.text.toString()

            if(email.isEmpty() && password.isEmpty()){
                Toast.makeText(this, "Fill them first", Toast.LENGTH_SHORT).show()
            }
            else
            Login(email,password)
        }

    }
    private fun Login(email:String,password:String){
//        Logic for login
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                  startActivity(Intent(this,HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "No account found back yo SingUp screen", Toast.LENGTH_SHORT).show()
                }
            }

    }
}