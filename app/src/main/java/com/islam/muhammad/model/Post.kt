package com.islam.muhammad.model

class Post{
    private var postid:String = ""
    private var postimage:String = ""
    private var publisher:String = ""
    private var description:String = ""
    private var postDate:String = ""
    private var postTime:String = ""
    private var photoClassify:String = ""


    constructor()
    constructor(postid: String, postimage: String, publisher: String, description: String,postDate:String,postTime:String,photoClassify:String) {
        this.postid = postid
        this.postimage = postimage
        this.publisher = publisher
        this.description = description
        this.postDate =postDate
        this.postTime = postTime
        this.photoClassify = photoClassify
    }
    fun setPostid(postid:String){
        this.postid =postid
    }
    fun setPostimage(postimage:String){
        this.postimage =postimage
    }
    fun setPublisher(publisher:String){
        this.publisher =publisher
    }
    fun setDescription(description:String){
        this.description =description
    }
    fun setPostDate(postDate: String){
        this.postDate = postDate
    }
    fun setPostTime(postTime: String){
        this.postTime = postTime
    }
    fun setPhotoClassify(photoClassify: String){
        this.photoClassify = photoClassify
    }



    fun getPostid():String{
        return postid
    }
    fun getPostTime():String{
        return postTime
    }
    fun getPhotoClassify():String{
        return photoClassify
    }

    fun getPostDate():String{
        return postDate
    }
    fun getPostimage():String{
        return postimage
    }
    fun getPublisher():String{
        return publisher
    }
    fun getDescription():String{
        return description
    }

}