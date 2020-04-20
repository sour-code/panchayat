package com.example.coro1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.green
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_user_detial.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class user_detial : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    val REQUEST_CODE = 1000
    var decide: String? =null
    var mAuth : FirebaseUser?=null
    var adminsId= ArrayList<String>()
    var apiService: APIService?=null
    var lat : Double?=null
    var long : Double?=null
    var GPSadd : String?=null
    var IDtype : String? = "AADHAR"
    var block : String?= ""
    var panchayat: String?= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detial)
//
        val intent = intent
        decide = intent.getStringExtra("Select")

back_btn?.setOnClickListener(){
    finish()
}


        //spinner
        val Blocks = resources.getStringArray(R.array.Blocks)

        var language  = resources.getStringArray(R.array.Saraikella)
        if (spinner_block != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, Blocks)
            spinner_block.adapter = adapter

            spinner_block.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    block = Blocks[position]
                    when{
                        position ==0 -> language  = resources.getStringArray(R.array.Saraikella)
                        position ==1 -> language  = resources.getStringArray(R.array.Kharsawan)
                        position ==2 -> language  = resources.getStringArray(R.array.Gamharia)
                        position ==3 -> language  = resources.getStringArray(R.array.Rajnagar)
                        position ==4 -> language  = resources.getStringArray(R.array.Kuchai)
                        position ==5 -> language  = resources.getStringArray(R.array.Chandil)
                        position ==6 -> language  = resources.getStringArray(R.array.Ichagarh)
                        position ==7 -> language  = resources.getStringArray(R.array.Nimdih)
                        position ==8 -> language  = resources.getStringArray(R.array.Kukru)

                    }
                    Toast.makeText(this@user_detial,
                        getString(R.string.selected_item) + " " +
                                "" + Blocks[position], Toast.LENGTH_SHORT).show()
                    if (spinner_panchayat != null ) {
                        val adapter = ArrayAdapter(applicationContext
                            ,
                            android.R.layout.simple_spinner_item, language)
                        spinner_panchayat.adapter = adapter

                        spinner_panchayat.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>,
                                                        view: View, position: Int, id: Long) {
                                panchayat = language[position]
                                Toast.makeText(this@user_detial,
                                    getString(R.string.selected_item) + " " +
                                            "" + language[position], Toast.LENGTH_SHORT).show()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

//spinner for id verification
        var languages = resources.getStringArray(R.array.Spinner_items)
        val spinner : Spinner = findViewById(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                 AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    IDtype = languages[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        //notification
        apiService = Client().getClient("https://fcm.googleapis.com/").create(APIService::class.java)
        mAuth= FirebaseAuth.getInstance().currentUser
        var fname : EditText = findViewById(R.id.firstname)
        var lnmae : EditText = findViewById(R.id.lastname)
        var adhar : EditText = findViewById(R.id.adhaar)
        var mobile : EditText = findViewById(R.id.mobile)
        var address : EditText = findViewById(R.id.address)
        var pincode : EditText = findViewById(R.id.pincode)




        // spinner details

        //spinner details end

        add_locn.setOnClickListener(){

            if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this@user_detial, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_CODE)
                return@setOnClickListener
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
                Looper.myLooper())

            //  start.isEnabled = !start.isEnabled
            // stop.isEnabled = !stop.isEnabled
        }
        findlatitudeaandlongitude()
        submit_req_btn.setOnClickListener(){

            when{
            TextUtils.isEmpty(fname.text) -> Toast.makeText(applicationContext,"Fill First Name Properly ", Toast.LENGTH_SHORT).show()
                     TextUtils.isEmpty(lnmae.text)-> Toast.makeText(applicationContext,"Fill Last NAme Properly", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(mobile.text)-> Toast.makeText(applicationContext,"Fill Mobile Number Properly", Toast.LENGTH_SHORT).show()
                 TextUtils.isEmpty(address.text) -> Toast.makeText(applicationContext,"Fill Address Properly", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(pincode.text) -> Toast.makeText(applicationContext,"Fill Pincode Properly", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(GPSadd) -> Toast.makeText(applicationContext,"Please Allow Location..", Toast.LENGTH_SHORT).show()
                    mobile.length()!=10 -> Toast.makeText(applicationContext,"Fill Mobile Number Properly", Toast.LENGTH_SHORT).show()
                    pincode.length()!=6 -> Toast.makeText(applicationContext,"Fill Pincode Properly", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(block) -> Toast.makeText(applicationContext,"Enter Your Block", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(panchayat) -> Toast.makeText(applicationContext,"Enter Your Panchayat", Toast.LENGTH_SHORT).show()
            else ->{
                var ref = FirebaseDatabase.getInstance().reference
                if(decide == "0"){
                    ref = ref.child("Jamshedpur").child(mAuth!!.uid)
                }
                else{
                    ref = ref.child("OutSider").child(mAuth!!.uid)
                }
                val requestId = ref.push().key
                var user = HashMap<String,Any>()
                user["fname"]= fname.text.toString()
                user["lname"]= lnmae.text.toString()
                user["Adhar"]= adhar.text.toString()
                user["members"]= mobile.text.toString()
                user["address"]= address.text.toString()
                user["pincode"]= pincode.text.toString()
                user["userid"]= mAuth!!.uid
                user["gpsadd"]= GPSadd!!
                user["isStart"]= false
                user["idtype"]=IDtype!!
                user["isSuccessful"]= false
                user["requestid"]=requestId
                user["type"] = decide!!
                user["block"] = block!!
                user["panchayat"] = panchayat!!

                ref.child(requestId).setValue(user)
                Toast.makeText(applicationContext,"Request is Submitted Successfully", Toast.LENGTH_SHORT).show()
              sendNotification()
               // sendNotificationtoBlock()
                //sendNotificationtoPanchayat()
                startActivity(Intent(applicationContext,user_main::class.java))
                finish()}
        }}



    }
    private fun sendNotificationtoPanchayat() {
        var databaseReference= FirebaseDatabase.getInstance().getReference("Admins")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(x in p0.children){

                    var adminId=x.value.toString()
                    var panchayat_admin=x.child("panchayat").value.toString()
                    var block_admin=x.child("block").value.toString()
                    if(block_admin.equals(block)&&panchayat_admin.equals(panchayat))
                        generateNotification(adminId)
                }
            }

        })
    }

    private fun sendNotificationtoBlock() {
        var databaseReference= FirebaseDatabase.getInstance().getReference("Admins")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(x in p0.children){

                    var adminId=x.value.toString()
                    var block_admin=x.child("block").value.toString()
                    if(block_admin.equals(block))
                    generateNotification(adminId)
                }
            }

        })
    }
    private fun sendNotification() {
        var databaseReference= FirebaseDatabase.getInstance().getReference("Admins")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(x in p0.children){

                    var adminId=x.value.toString()
                    var panchayat_admin=x.child("panchayat").value.toString()
                    var block_admin=x.child("block").value.toString()
                    if(panchayat_admin.equals("")&&block_admin.equals(""))
                   generateNotification(adminId)
                }
            }

        })
    }


    fun generateNotification(recieverId:String) {
        var tokenreference = FirebaseDatabase.getInstance().getReference("Tokens").child(recieverId)
        tokenreference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
               // Toast.makeText(applicationContext,p0.exists().toString(),Toast.LENGTH_SHORT).show()
                var token =""

             token =p0!!.value.toString()

                var data= notification(
                    mAuth!!.uid,
                    R.drawable.red,
                    "New Request Found!!",
                    "Alert",
                    recieverId
                )
                var sender= Sender(data, token)
                apiService?.sendNotification(sender)?.enqueue(object : Callback<MyResponse> {
                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                      // Toast.makeText(applicationContext, response.body()?.success.toString() , Toast.LENGTH_SHORT).show();
                        if (response.code() == 200){
                            if (response.body()?.success != 1){
                         //      Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show();
                            }

                        }  }

                })
            }


        })



    }

    private fun findlatitudeaandlongitude() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
        else{
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

            /* stop.setOnClickListener(){
                if(ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_CODE)
                    return@setOnClickListener
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)

                start.isEnabled = !start.isEnabled
                stop.isEnabled = !stop.isEnabled
            }*/
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){REQUEST_CODE->{
            if(grantResults.size>0){
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(applicationContext,"Permission Granted", Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(applicationContext,"Permission Denied", Toast.LENGTH_SHORT).show()
            }

        }}
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                var location = p0!!.locations.get(p0!!.locations.size-1)
               lat= location.latitude
                long=location.longitude
                getAddress(lat!!,long!!)
            }
        }
    }

    private fun buildLocationRequest() {

        locationRequest= LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        //    locationRequest.interval=5000
        //    locationRequest.fastestInterval=3000
        locationRequest.smallestDisplacement =10f


    }
    fun getAddress(latitude: Double, longitude: Double): Address? {
        val geocoder: Geocoder
        val addresses: List<*>
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            GPSadd = addresses.get(0).getAddressLine(0)

            return addresses[0]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}

