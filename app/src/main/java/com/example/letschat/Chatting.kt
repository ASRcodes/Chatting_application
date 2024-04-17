package com.example.letschat

import android.content.Intent
import android.os.Bundle
import android.telephony.SignalStrength
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letschat.databinding.ActivityChattingBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chatting : AppCompatActivity() {
    private lateinit var chatMessage:RecyclerView
    private lateinit var binding: ActivityChattingBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var refDatabase:DatabaseReference

//    Creating sender and receiver room
    var senderRooom:String? = null
    var receiverRoom:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityChattingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        refDatabase = FirebaseDatabase.getInstance().getReference()

//        Getting the uid and name here
        val name = intent.getStringExtra("name")

//        Setting the title as the name
        supportActionBar?.title = name

        val receiveruid = intent.getStringExtra("uid")
//        if (name != null) {
//            // Log that you are getting the name
//            Log.d("ChattingActivity", "Name received: $name")
//        } else {
//            // Log that name is not received
//            Log.d("ChattingActivity", "Name not received")
//        }

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

//        In this block of code we have created a separate room for both sender and receiver so that
//        They can chat inside the same room
        senderRooom = receiveruid+senderUid
        receiverRoom = senderUid+receiveruid



        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)
        binding.chatMessage.layoutManager = LinearLayoutManager(this)
        binding.chatMessage.adapter = messageAdapter

//        Logic to set data on recycler view
        refDatabase.child("chats").child(receiverRoom!!).child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
//                    Using this snapShot to get data from FireBase and show it over RecyclerView

                    messageList.clear()
//                    Getting the messageList data using For loop
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



//        On send button clicked we have to we have to add messages to database and show them to receiver's end
        binding.send.setOnClickListener {
            val message = binding.messageBox.text.toString()
           val messageObject = Message(message,senderUid)

//            Now we'll create an node for chats in firebase where all the chats wil be stored
            refDatabase.child("chats").child(senderRooom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {
                    refDatabase.child("chats").child(receiverRoom!!).child("message").push()
                        .setValue(messageObject)
                }
            binding.messageBox.setText("")
        }
    }
}