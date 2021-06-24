package com.islam.muhammad.model

class Vpvcount {

    private var postView:Int = 0

    constructor()

    constructor(postView:Int) {
        this.postView = postView
    }

    fun getPostView():Int{
        return postView
    }


    fun setPostView(postView:Int){
        this.postView = postView
    }


}