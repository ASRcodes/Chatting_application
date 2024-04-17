package com.example.letschat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class MessageAdapter(val context: Context,val message:ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SEND = 1;
    val RECEIVED = 2;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        Inflating our view
        if (viewType==1){
//          inflate send
            val view:View = LayoutInflater.from(context).inflate(R.layout.recycler_message_send,parent,false)
            return sentViewHolder(view)
        }
        else{
//            inflate received
            val view:View = LayoutInflater.from(context).inflate(R.layout.recycler_message_recieved,parent,false)
            return receivedViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return message.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage = message[position]

        if (holder.javaClass==sentViewHolder::class.java){
//            Do stuff for sent message
//            TypeCasting the viewHolder
            val viewHolder = holder as sentViewHolder
            holder.send.text = currentMessage.message
        }
        else{
//            Do stuff for received message
            val viewHolder = holder as receivedViewHolder
            holder.received.text = currentMessage.message
        }
    }



//    To get the view which we are sending or getting message
    override fun getItemViewType(position: Int): Int {
        val  currentMessage = message[position]

//    now if the message's uid matches with sender's uid i.e, the user is current one its send message
    if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderID)){
        return SEND
    }
    else{
        return RECEIVED
    }
    }

    //    Here we'll make two viewHolders one for sent message and one for received
//    Sent viewHolder
    class sentViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
    val send = itemView.findViewById<TextView>(R.id.text_send_mess)
    }
    class receivedViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
    val received = itemView.findViewById<TextView>(R.id.txt_received_mess)
    }
}