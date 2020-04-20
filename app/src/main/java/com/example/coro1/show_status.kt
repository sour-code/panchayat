package com.example.coro1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_show_status.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class show_status : AppCompatActivity() {
        private var userid = ""
    private var requestId = ""
    private var decid = ""
    var apiService: APIService?=null
    private var adminList: MutableList<Admin>?=null
    private var firebaseUser: FirebaseUser?=null
    private var admin: Admin?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_status)
//decide
//Toast.makeText(applicationContext,decid,Toast.LENGTH_SHORT).show()
        val intent = intent
        userid = intent.getStringExtra("userId")
        requestId = intent.getStringExtra("requestId")
        decid = intent.getStringExtra("decide")
        firebaseUser =FirebaseAuth.getInstance().currentUser
        var fname: TextView = findViewById(R.id.firstname_show)
       // var lname: TextView = findViewById(R.id.lastname_show)
        apiService = Client()
            .getClient("https://fcm.googleapis.com/").create(APIService::class.java)
        var adhar: TextView = findViewById(R.id.adhar_no)
        var familymember: TextView = findViewById(R.id.members)
        var address: TextView = findViewById(R.id.address_show)
    //    var pincode: TextView = findViewById(R.id.pincode_show)
        var startproces: Button = findViewById(R.id.start_process)
        var completed: Button = findViewById(R.id.successful_btn)
        var dot : ImageView = findViewById(R.id.status_status_show)
        var gpsadd : TextView = findViewById(R.id.gps_address)
        var idtype : TextView = findViewById(R.id.idtype)
        var block : TextView =findViewById(R.id.block_show)
        var panchayat : TextView =findViewById(R.id.panchayat_show)
        retrieveAllinfo(fname,adhar,familymember,address,startproces,completed,dot,gpsadd,idtype,block,panchayat)
        back_btn?.setOnClickListener(){
            finish()
        }
        start_process?.setOnClickListener(){
            var ref = FirebaseDatabase.getInstance().reference
            if(decid=="0"){
                ref= ref.child("Jamshedpur").child(userid).child(requestId)
            }
            else{
                ref= ref.child("OutSider").child(userid).child(requestId)
            }
            var usermap = HashMap<String, Any>()
            usermap["isStart"] =true
            ref.updateChildren(usermap)
            Toast.makeText(applicationContext,"You Accepted the Problem..",Toast.LENGTH_SHORT).show()
            retrieveAllinfo(fname,adhar,familymember,address,startproces,completed,dot,gpsadd,idtype,block,panchayat)
            generateNotification(userid,"Your Request is Accepted!!")
        }
        completed?.setOnClickListener(){
            var ref = FirebaseDatabase.getInstance().reference
            if(decid=="0"){
                ref= ref.child("Jamshedpur").child(userid).child(requestId)
            }
            else{
                ref= ref.child("OutSider").child(userid).child(requestId)
            }
            var usermap = HashMap<String, Any>()
            usermap["isSuccessful"] =true
            ref.updateChildren(usermap)
            generateNotification(userid,"Your Request is Successfully Completed!!")
            Toast.makeText(applicationContext,"Problem is resolved Successfully..",Toast.LENGTH_SHORT).show()
            retrieveAllinfo(fname,adhar,familymember,address,startproces,completed,dot,gpsadd,idtype,block,panchayat)
        }


    }
    fun generateNotification(recieverId:String, message:String) {
        var tokenreference = FirebaseDatabase.getInstance().getReference("Tokens").child(recieverId)
        tokenreference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                var token =p0!!.value.toString()
                var data= notification(
                    firebaseUser!!.uid,
                    R.drawable.progress,
                    message,
                    "Alert!!",
                    recieverId
                )
                var sender= Sender(data, token)
                apiService?.sendNotification(sender)?.enqueue(object : Callback<MyResponse> {
                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                        if (response.code() == 200){
                            if (response.body()?.success != 1){
                           //     Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show();
                            }

                        }  }

                })
            }


        })



    }

    private fun retrieveAllinfo(
        fname: TextView,

        adhar: TextView,
        familymember: TextView,
        address: TextView,

        startproces: Button,
        completed: Button,
        dot: ImageView,
        gpsadd: TextView,
        idtyp: TextView,block :TextView,panchayat :TextView
    ) {
        var ref = FirebaseDatabase.getInstance().reference
        if(decid=="0"){
            ref= ref.child("Jamshedpur").child(userid).child(requestId)
        }
        else{
            ref= ref.child("OutSider").child(userid).child(requestId)
        }
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {var details = p0.getValue<Admin>(Admin::class.java)
                    fname.text = p0.child("fname").value.toString() + " "+p0.child("lname").value.toString()

                    adhar.text = p0.child("Adhar").value.toString()
                    address.text = p0.child("address").value.toString() +", "+ p0.child("pincode").value.toString()
                    familymember.text = p0.child("members").value.toString()
                    idtyp.text=p0.child("idtype").value.toString()
                    gpsadd.text=p0.child("gpsadd").value.toString()
                    block.text=p0.child("block").value.toString()
                    panchayat.text=p0.child("panchayat").value.toString()
                    if(p0.child("isSuccessful").value ==true){
                        startproces.visibility = View.GONE
                        completed.visibility = View.GONE
                        dot.setImageResource(R.drawable.green)
                    }
                 else  if(p0.child("isStart").value ==true){
                        startproces.visibility = View.GONE
                        completed.visibility = View.VISIBLE
                        dot.setImageResource(R.drawable.orange)
                    }
                    else{startproces.visibility = View.VISIBLE
                       completed.visibility = View.GONE
                       dot.setImageResource(R.drawable.red)
                    }


                }

            }

        })
    }
}


