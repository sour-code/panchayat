package com.example.coro1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class AdminAdapter (private val mConntext: Context, private var adminList: MutableList<Admin>): RecyclerView.Adapter<AdminAdapter.ViewHolder>(){



    inner class ViewHolder (@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var Firstname : TextView?=null
        //var Lastname : TextView?=null
        var Address: TextView?=null
        var Status: ImageView?=null
        // var Pincode : TextView?=null
        init {
            Firstname =itemView.findViewById(R.id.firstname)
          //  Lastname =itemView.findViewById(R.id.lastname)
            Address =itemView.findViewById(R.id.address)
            Status =itemView.findViewById(R.id.status)
           // Pincode =itemView.findViewById(R.id.pincode)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mConntext).inflate(R .layout.notification_item_layout,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val admin = adminList[position]


        holder.itemView.setOnClickListener(){
            if(admin.getUserId() == FirebaseAuth.getInstance().currentUser!!.uid){
                //Do Nothing
            }
            else{
            val intentcomment = Intent(mConntext,show_status::class.java)


                intentcomment.putExtra("userId",admin.getUserId())
                intentcomment.putExtra("requestId",admin.getreuestID())

            intentcomment.putExtra("decide",admin.gettype())
            mConntext.startActivity(intentcomment)

        }

        }

        holder.Firstname!!.text= admin.getfname() + " "+admin.getlname()

        holder.Address!!.text= admin.getAddress() + " "+ admin.getPincode()
        if(admin.getIsSuccessful() ==true){
            holder.Status!!.setImageResource(R.drawable.green)
        }
        else if(admin.getIsStart()==true){
            holder.Status!!.setImageResource(R.drawable.orange)
        }
        else{
            holder.Status!!.setImageResource(R.drawable.red)
        }
    }
}
