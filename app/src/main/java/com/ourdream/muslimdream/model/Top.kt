package com.ourdream.muslimdream.model

class Top {
    private var publisher:String = ""
    private var uid: String = ""
    private var image: String = ""
    private var username: String = ""
    private var verified: String = ""
    constructor()
    constructor(username: String, publisher: String, image: String, uid: String, verified:String) {
        this.publisher = publisher
        this.uid= uid
        this.username = username
        this.verified = verified
        this.image = image
    }

    fun getPublisher():String{
        return publisher
    }
    fun getVerification():String{
        return verified
    }
    fun getUsername(): String
    {
        return  username
    }
    fun getImage(): String
    {
        return  image
    }
    fun getUID(): String
    {
        return  uid
    }
}