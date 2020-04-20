package com.example.coro1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_user_main.*

class user_main : AppCompatActivity() {
    private var admin: Admin?=null
    private var adminadapter: AdminAdapter?=null
    private var adminMutableList: MutableList<Admin>?=null

    private var firebaseUser: FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)
//recyclerview

        showrequest()
        var recyclerView : RecyclerView?=null
        recyclerView = findViewById(R.id.recyclerview_user)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout=false
        linearLayoutManager.stackFromEnd=true
        recyclerView.layoutManager=linearLayoutManager
        adminMutableList= ArrayList()
        adminadapter= AdminAdapter(this,adminMutableList!!)
        recyclerView.adapter=adminadapter
        recyclerView.visibility= View.GONE

        //
        show_sts_btn.setOnClickListener(){
            recyclerView.visibility= View.VISIBLE
            hide_sts_btn.visibility=View.VISIBLE
            show_sts_btn.visibility=View.GONE
        }
        hide_sts_btn.setOnClickListener(){
            recyclerView.visibility= View.GONE
            hide_sts_btn.visibility=View.GONE
            show_sts_btn.visibility=View.VISIBLE
        }
        var food_prblm: TextView = findViewById(R.id.food_problem)
        var tokenreference= FirebaseDatabase.getInstance().getReference("Tokens")
        var device_token= FirebaseInstanceId.getInstance().getToken()
        tokenreference.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(device_token)
        food_prblm.setOnClickListener(){
            val intentcomment = Intent(this@user_main,user_detial::class.java)
            intentcomment.putExtra("Select","0")
            this.startActivity(intentcomment)


        }
        outside_ppl_prblm.setOnClickListener(){
            val intentoutsideppl = Intent(this@user_main,user_detial::class.java)
            intentoutsideppl.putExtra("Select","1")
            this.startActivity(intentoutsideppl)

        }



        log_out_btn?.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(applicationContext,"You are Successfully Logout..", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@user_main, UserLogin::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun showrequest() {
        var ref = FirebaseDatabase.getInstance().reference.child("Jamshedpur").child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                adminMutableList?.clear()
                    for (p0 in snapshot.children) {
                        if (p0.exists()) {
                            outside_ppl_prblm.visibility=View.GONE
                            val user = p0.getValue<Admin>(Admin::class.java)
                            val fname = p0.child("fname").value.toString()
                            val lname = p0.child("lname").value.toString()
                            val adhar = p0.child("Adhar").value.toString()
                            val member = p0.child("members").value.toString()
                            val address = p0.child("address").value.toString()
                            val pincode = p0.child("pincode").value.toString()
                            val userid = p0.child("userid").value.toString()
                            val gpsadd = p0.child("gpsadd").value.toString()
                            val isstart = p0.child("isStart").value.toString()
                            val idtype = p0.child("inProgress").value.toString()
                            val issuccessful = p0.child("isSuccessful").value.toString()
                            val Requestid = p0.child("requestid").value.toString()
                            val Type = p0.child("type").value.toString()

                            adminMutableList!!.add(
                                Admin(
                                    p0.child("userid").value.toString(),
                                    p0.child("fname").value.toString(),
                                    p0.child("lname").value.toString(),
                                    p0.child("Adhar").value.toString(),
                                    p0.child("members").value.toString(),
                                    p0.child("address").value.toString(),
                                    p0.child("pincode").value.toString(),

                                    isstart.toBoolean(),
                                    idtype,
                                    issuccessful.toBoolean(),
                                    p0.child("gpsadd").value.toString(),Requestid,Type,p0.child("block").value.toString()
                                    ,p0.child("panchayat").value.toString()
                                )
                            )
                            adminadapter!!.notifyDataSetChanged()

                        }
                    }
            }

        })
         ref = FirebaseDatabase.getInstance().reference.child("OutSider").child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                adminMutableList?.clear()
                for (p0 in snapshot.children) {
                    if (p0.exists()) {
                        food_problem.visibility=View.GONE
                        val user = p0.getValue<Admin>(Admin::class.java)
                        val fname = p0.child("fname").value.toString()
                        val lname = p0.child("lname").value.toString()
                        val adhar = p0.child("Adhar").value.toString()
                        val member = p0.child("members").value.toString()
                        val address = p0.child("address").value.toString()
                        val pincode = p0.child("pincode").value.toString()
                        val userid = p0.child("userid").value.toString()
                        val gpsadd = p0.child("gpsadd").value.toString()
                        val isstart = p0.child("isStart").value.toString()
                        val idtype = p0.child("inProgress").value.toString()
                        val issuccessful = p0.child("isSuccessful").value.toString()
                        val Requestid = p0.child("requestid").value.toString()
                        val Type = p0.child("type").value.toString()

                        adminMutableList!!.add(
                            Admin(
                                p0.child("userid").value.toString(),
                                p0.child("fname").value.toString(),
                                p0.child("lname").value.toString(),
                                p0.child("Adhar").value.toString(),
                                p0.child("members").value.toString(),
                                p0.child("address").value.toString(),
                                p0.child("pincode").value.toString(),

                                isstart.toBoolean(),
                                idtype,
                                issuccessful.toBoolean(),
                                p0.child("gpsadd").value.toString(),Requestid,Type,p0.child("block").value.toString()
                                ,p0.child("panchayat").value.toString()
                            )
                        )

                        adminadapter!!.notifyDataSetChanged()
                    }
                }
            }

        })
    }
}
