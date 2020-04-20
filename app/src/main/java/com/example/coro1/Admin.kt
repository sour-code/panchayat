package com.example.coro1

class Admin {
    private  var userid : String=""
    private var fname : String= ""
    private var lname : String= ""
    private var Adhar : String= ""
    private var members : String= ""
    private var address : String= ""
    private var pincode : String= ""
    private var gpsadd : String=""
    private var isStart : Boolean=false
    private var idtype : String=""
    private var isSuccessful : Boolean=false
    private var requestid : String=""
    private var type : String=""
    private var block : String =""
    private var panchayat: String=""
    constructor()
    constructor(
        userid: String,
        fname: String,
        lname: String,
        Adhar: String,
        members: String,
        address: String,
        pincode: String,
        isStart : Boolean,
        idtype: String,
        isSuccessful:Boolean,
        gpsadd:String,
        requestid:String,
        type : String,
        block : String,
        panchayat: String
    ) {
        this.userid = userid
        this.fname = fname
        this.lname = lname
        this.Adhar = Adhar
        this.members = members
        this.address = address
        this.pincode = pincode
        this.isStart=isStart
        this.isSuccessful =isSuccessful
        this.idtype= idtype
        this.gpsadd =gpsadd
        this.requestid =requestid
        this.type=type
        this.block=block
        this.panchayat=panchayat
    }
    fun getUserId():String{
        return userid
    }
    fun getfname():String{
        return fname
    }
    fun getlname():String{
        return lname
    }
    fun getAdhar():String{
        return Adhar
    }
    fun getFamilymember():String{
        return members
    }
    fun getAddress():String{
        return address
    }
    fun getPincode():String{
        return pincode
    }
    fun getIsStart(): Boolean{
        return isStart
    }
    fun getIsSuccessful(): Boolean{
        return isSuccessful
    }
    fun getGPSadd():String{
        return gpsadd
    }
    fun getIDtype():String{
        return idtype
    }
    fun gettype():String{
        return type
    }
    fun getreuestID():String{
        return requestid
    }
    fun getBlock():String{
        return block
    }
    fun getPanchayat():String{
        return panchayat
    }
}