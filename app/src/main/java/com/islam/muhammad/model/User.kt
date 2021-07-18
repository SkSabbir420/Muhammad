package com.islam.muhammad.model

class User
{
    private var username: String = ""
    private var fullname: String = ""
    private var bio: String = ""
    private var image: String = ""
    private var uid: String = ""
    private var email: String = ""
    private var membership: String = ""


    constructor()


    constructor(username: String, fullname: String, bio: String, image: String, uid: String,email:String,membership:String) {
        this.username = username
        this.fullname = fullname
        this.bio = bio
        this.image = image
        this.uid = uid
        this.email = email
        this.membership = membership
    }


    fun getUsername(): String
    {
        return  username
    }
    fun getEmail():String{
        return email
    }
    fun getMembership():String{
        return membership
    }
    fun setMembership(membership: String){
        this.membership = membership
    }

    fun setUsername(username: String)
    {
        this.username = username
    }


    fun getFullname(): String
    {
        return  fullname
    }

    fun setFullname(fullname: String)
    {
        this.fullname = fullname
    }


    fun getBio(): String
    {
        return  bio
    }

    fun setBio(bio: String)
    {
        this.bio = bio
    }


    fun getImage(): String
    {
        return  image
    }

    fun setImage(image: String)
    {
        this.image = image
    }


    fun getUID(): String
    {
        return  uid
    }

    fun setUID(uid: String)
    {
        this.uid = uid
    }
}