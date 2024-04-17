package com.example.letschat

//We have created this class to fetch the values from fireBase of users and show them on our recyclerView
class User {
    var name:String?=null
    var email:String?=null
    var uid:String?=null

    constructor(){}
    constructor(name:String?,email:String?,uid:String?){
        this.name = name
        this.email = email
        this.uid = uid
    }
}