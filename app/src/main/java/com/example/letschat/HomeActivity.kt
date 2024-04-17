package com.example.letschat

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letschat.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.feedback.setOnClickListener{
        val dialog = Dialog(this)
            dialog.setContentView(R.layout.feedback_dialog)
        val send= dialog.findViewById<Button>(R.id.send)
          val cancel =  dialog.findViewById<Button>(R.id.cancel)
         val feedText = dialog.findViewById<EditText>(R.id.feedText)

            val  feedback = feedText.text.toString()
            send.setOnClickListener {
                val feedback = feedText.text.toString()
//                sendFeedbackViaGmail(feedback)
                sendFeedbackViaWhatsApp(feedback)
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        val name = intent.getStringExtra("name1")
        if (name != null) {
            supportActionBar?.title = "You as $name"
            Log.d("NameAct", "Name received: $name")
        } else {
            Log.d("NameAct", "Name is null")
        }
//        TO get the details About current user who is log in..
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().getReference()
        userList = ArrayList()
        adapter = UserAdapter(this,userList)

//        RecyclerView.......
        binding.recyclerNameList.layoutManager = LinearLayoutManager(this)
        binding.recyclerNameList.adapter = adapter

        mRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
//                Fetching out user's data from the given snapshot
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        Inflating our created menu
        menuInflater.inflate(R.menu.menu_logout,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.logoutMenu){
//            Logic for logout and back to login screen
            mAuth.signOut()
            finish()
            startActivity(Intent(this,Login::class.java))
            return true
        }
        return true
    }
//    private fun sendFeedbackViaGmail(feedback: String) {
//        val emailIntent = Intent(Intent.ACTION_SEND)
//        emailIntent.type = "text/plain"
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("rajsinghrajput@gmail.com"))
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
//        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback)
//        emailIntent.setPackage("com.google.android.gm") // Restrict to Gmail app
//        startActivity(Intent.createChooser(emailIntent, "Send feedback via Gmail"))
//    }

//    private fun sendFeedbackViaWhatsApp(feedback: String) {
//        val whatsappIntent = Intent(Intent.ACTION_SEND)
//        whatsappIntent.type = "text/plain"
//        whatsappIntent.putExtra(Intent.EXTRA_TEXT, feedback)
//        whatsappIntent.setPackage("com.whatsapp") // Restrict to WhatsApp app
//        startActivity(Intent.createChooser(whatsappIntent, "Send feedback via WhatsApp"))
//    }

    private fun sendFeedbackViaWhatsApp(feedback: String) {
        // Replace "your_phone_number" with your actual phone number
        val phoneNumber = "+916204630259"

        // Create a URI with the phone number
        val uri = "https://wa.me/$phoneNumber"

        // Create the intent
        val whatsappIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        // Set the text of the message
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, feedback)

        // Restrict the intent to the WhatsApp app
        whatsappIntent.setPackage("com.whatsapp")

        // Start the activity
        startActivity(whatsappIntent)
    }
}