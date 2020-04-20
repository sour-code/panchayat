package com.example.coro1

import android.R.attr.name
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var admin: Admin?=null
    private var adminadapter: AdminAdapter?=null
    private var adminMutableList: MutableList<Admin>?=null
    private var adminMutableList1: MutableList<Admin>?=null
    private var firebaseUser: FirebaseUser?=null
    private var block_admin :String?=""
    private var panchayat_admin: String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getAdminBlockandPanchayat()
        showrequest()
        showrequest1()

        var recyclerView : RecyclerView?=null
        recyclerView = findViewById(R.id.recyclerview_inside)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout=false
        linearLayoutManager.stackFromEnd=true
        recyclerView.layoutManager=linearLayoutManager
        adminMutableList= ArrayList()
adminadapter= AdminAdapter(this,adminMutableList!!)
        recyclerView.adapter=adminadapter
// outside jsr
        adminMutableList1 = ArrayList()
        var recyclerView1 : RecyclerView?=null
        recyclerView1 = findViewById(R.id.recyclerview_outside)


        recyclerView.visibility=View.VISIBLE
        recyclerView1.visibility=View.GONE
        request_jsr?.setOnClickListener(){
            recyclerView.visibility=View.VISIBLE
            recyclerView1.visibility=View.GONE
        }
        request_outside?.setOnClickListener(){
            recyclerView.visibility=View.GONE
            recyclerView1.visibility=View.VISIBLE
            recyclerView1.setHasFixedSize(true)
            val linearLayoutManager1 = LinearLayoutManager(this)
            linearLayoutManager1.reverseLayout=false
            linearLayoutManager1.stackFromEnd=true
            recyclerView1.layoutManager=linearLayoutManager1

            adminadapter= AdminAdapter(this,adminMutableList1!!)
            recyclerView1.adapter=adminadapter
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        var tokenreference=FirebaseDatabase.getInstance().getReference("Tokens")
        var device_token= FirebaseInstanceId.getInstance().getToken()
        tokenreference.child(firebaseUser!!.uid).setValue(device_token)



        logout_btn?.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
                Toast.makeText(applicationContext,"You are Successfully Logout..",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, AdminLogin::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        showrequest1()
        showrequest()
    }

    private fun showrequest() {
        getAdminBlockandPanchayat()
        val ref = FirebaseDatabase.getInstance().reference.child("Jamshedpur")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                adminMutableList?.clear()
                for(s0 in snapshot.children) {
                    for (p0 in s0.children) {

                        if (p0.exists() )
                           if ( block_admin==p0.child("block").value.toString())
                               if( panchayat_admin==p0.child("panchayat").value.toString() )
                        {
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
                                    p0.child("gpsadd").value.toString(),Requestid,Type,p0.child("block").value.toString(),p0.child("panchayat").value.toString()
                                )
                            )

                            adminadapter!!.notifyDataSetChanged()
                        }
                    }
                    // adminMutableList?.clear()

                }
            }

        })

    }
    private fun showrequest1() {
        getAdminBlockandPanchayat()
        val ref = FirebaseDatabase.getInstance().reference.child("OutSider")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                adminMutableList1?.clear()

                for(s0 in snapshot.children) {
                    for (p0 in s0.children) {
                           if ( block_admin==p0.child("block").value.toString())
                                   if( panchayat_admin==p0.child("panchayat").value.toString() )
                        {
                            val user = p0.getValue<Admin>(Admin::class.java)

                            val isstart = p0.child("isStart").value.toString()
                            val idtype = p0.child("inProgress").value.toString()
                            val issuccessful = p0.child("isSuccessful").value.toString()
                            val Requestid = p0.child("requestid").value.toString()
                            val Type = p0.child("type").value.toString()
                            adminMutableList1!!.add(
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
                                    p0.child("gpsadd").value.toString(),
                                    Requestid,Type,p0.child("block").value.toString()
                                    ,p0.child("panchayat").value.toString()
                                )
                            )
                            adminadapter!!.notifyDataSetChanged()
                        }
                    }
                    // adminMutableList?.clear()

                }
            }

        })

    }
    private fun getAdminBlockandPanchayat(){
        val ref = FirebaseDatabase.getInstance().reference.child("Admins").child(FirebaseAuth.getInstance().currentUser!!.uid)

        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot?) {

                if(p0!!.exists()){
               //     var admindetails = p0.getValue(String::class.java)
                    block_admin= p0.child("block").value.toString()
                    panchayat_admin= p0.child("panchayat").value.toString()

                }
                Toast.makeText(applicationContext,block_admin+panchayat_admin,Toast.LENGTH_SHORT).show()

            }

        })
    }
}
