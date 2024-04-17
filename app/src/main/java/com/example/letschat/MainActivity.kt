package com.example.letschat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            if (mAuth.currentUser!=null){
                val displayName = mAuth.currentUser?.email
               val intent=(Intent(this@MainActivity,HomeActivity::class.java))
                intent.putExtra("name1",displayName)
                startActivity(intent)
                finish()
            }
            else {
                startActivity(Intent(this@MainActivity, SignUp::class.java))
                finish()
            }
        },1000)
    }
}