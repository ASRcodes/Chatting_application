package com.example.letschat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context,val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
    val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int{
       return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.text.text=currentUser.name
        holder.layout.setOnClickListener {
//            val context = holder.itemView.context
            val intent = Intent(context,Chatting::class.java)
//            We'll send user's uid and user's name
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }
    }

//    ViewHolder class
    class UserViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val text = itemView.findViewById<TextView>(R.id.nameUser)
        val layout = itemView.findViewById<CardView>(R.id.cardLayout)
    }
}