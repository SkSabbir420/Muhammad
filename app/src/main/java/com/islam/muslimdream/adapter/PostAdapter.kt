package com.islam.muslimdream.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.islam.muslimdream.model.Post
import com.islam.muslimdream.model.User
import com.islam.muslimdream.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.islam.muslimdream.comment.PostCommentActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.posts_layout.view.*
import android.graphics.drawable.BitmapDrawable
import android.view.MenuItem
import android.widget.*
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.posts_layout.view.post_image_share_btn


class PostAdapter(private val mContext:Context,private val mPost: List<Post>):
    RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    private  var firebaseUser:FirebaseUser? = null
    private  var userid:String? = null
    private  var postKey:String? = null
    private  var likeReference:DatabaseReference? = null
    private  var testClick:Boolean =false
//    private var hashMap:HashMap<String,Bitmap> = HashMap<String,Bitmap>()
//    private var bitmap: Bitmap? = null


    inner class  ViewHolder(@NonNull itemView:View):RecyclerView.ViewHolder(itemView)
        ,PopupMenu.OnMenuItemClickListener{

        var userName:TextView
        var profileImage:CircleImageView
        var description:TextView
        var postImage:ImageView

        var likeButton:ImageView
        var likes:TextView

        var commentButton:ImageView
        var comments: TextView
        var date:TextView
        var post_progress_bar:ProgressBar
//        var adView: AdView
        //var publisher: TextView
        //var saveButton:ImageView

        init {
            userName = itemView.findViewById(R.id.user_name_post)
            profileImage = itemView.findViewById(R.id.user_profile_image_post)
            description = itemView.findViewById(R.id.post_text_home)
            postImage = itemView.findViewById(R.id.post_image_home)
            date = itemView.findViewById(R.id.txt_post_date)
            likeButton = itemView.findViewById(R.id.post_image_like_btn)
            likes = itemView.findViewById(R.id.post_image_like_text)
            post_progress_bar = itemView.findViewById(R.id.post_progressbar)

            commentButton = itemView.findViewById(R.id.post_image_comment_btn)
            comments = itemView.findViewById(R.id.post_image_comment_text)
//            adView = itemView.findViewById(R.id.adView)
            //publisher = itemView.findViewById(R.id.publisher)
            //saveButton = itemView.findViewById(R.id.post_save_comment_btn)

            itemView.post_image_like_btn.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                postKey = post.getPostid()
                testClick =true

                likeReference!!.addValueEventListener(object :ValueEventListener{

                    override fun onDataChange(p0: DataSnapshot) {
                        if (testClick ==true){
                            if(p0.child(postKey!!).hasChild(userid!!)){
                                likeReference!!.child(postKey!!).child(userid!!).removeValue()
                                testClick =false
                            }else{
                                likeReference!!.child(postKey!!).child(userid!!).setValue(true)
                                testClick = false
                            }
                        }
                    }

                    /*override fun onDataChange(p0: DataSnapshot) {
                        if (testClick ==true){
                            if(p0.child(postKey!!).child("like").hasChild(userid!!)){
                                likeReference!!.child(postKey!!).child("like").child(userid!!).removeValue()
                                testClick =false
                            }else{
                                likeReference!!.child(postKey!!).child("like").child(userid!!).setValue(true)
                                testClick = false
                            }
                        }
                    }*///update

                    override fun onCancelled(p0: DatabaseError){

                    }
                })
            }
            itemView.post_image_comment_btn.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                postKey = post.getPostid()
                val intent = Intent(mContext, PostCommentActivity::class.java)
                intent.putExtra("key",postKey)
                intent.putExtra("keyUid",post.getPublisher())
                mContext.startActivity(intent)
            }
            itemView.more_picture_layout.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                postKey = post.getPostid()
                val popupMenu = PopupMenu(mContext,it)
                popupMenu.inflate(R.menu.post_option_menu_picture)
                getDeleteButtonStatus(postKey!!,popupMenu)
                popupMenu.setOnMenuItemClickListener(this)
                popupMenu.show()
            }
            /*itemView.picture_post_delete.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                postKey = post.getPostid()

                try {
                val instance = FirebaseDatabase.getInstance()
                instance.getReference("postPictures").child("allUsers")
                    .child(FirebaseAuth.getInstance().uid.toString()).child(postKey!!).removeValue()
                instance.getReference("postPictures").child("allPostPictures")
                    .child(postKey!!).removeValue()
                instance.getReference("postPictureLikes").child(postKey!!).removeValue()
                instance.getReference("postPictureComments").child(post.getPublisher())
                    .child(postKey!!).removeValue()
                FirebaseStorage.getInstance().reference.child("postPictures")
                    .child(post.getPublisher()).child(postKey!!+".jpg").delete()
                    Toast.makeText(mContext, "Delete Successful", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(mContext, "Delete Unsuccessful", Toast.LENGTH_SHORT).show()
            }


//                val deleteReference = FirebaseDatabase.getInstance().getReference("postPictures")
//                    .child("allUsers").child(FirebaseAuth.getInstance().uid.toString())
//                var testClick =true
//                deleteReference.addValueEventListener(object :ValueEventListener{
//                    override fun onDataChange(p0: DataSnapshot) {
//                        if (testClick ==true){
//                            if(p0.hasChild(postKey!!)){
//                                deleteReference.child(postKey!!).removeValue()
//                                Toast.makeText(mContext,"Delete success",Toast.LENGTH_SHORT).show()
//                                testClick =false
//                            }
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                    }
//
//
//                })

            }*/
            itemView.post_image_share_btn.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                val currentUrl = post.getPostimage()
                val title = post.getDescription()

                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_TEXT, "Post Description:\n$title\n\nPhoto Link: $currentUrl")
                mContext.startActivity(Intent.createChooser(i, "Share this photo Uri with"))

            }


        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item!!.itemId) {
                R.id.delete_picture ->{
                    val  position:Int = adapterPosition
                    val post = mPost[position]
                    val postKey = post.getPostid()
                    try {
                        val instance = FirebaseDatabase.getInstance()
                        instance.getReference("postPictures").child("allUsers")
                            .child(FirebaseAuth.getInstance().uid.toString()).child(postKey!!).removeValue()
                        instance.getReference("postPictures").child("allPostPictures")
                            .child(postKey!!).removeValue()
                        instance.getReference("postPictureLikes").child(postKey!!).removeValue()
                        instance.getReference("postPictureComments").child(post.getPublisher())
                            .child(postKey!!).removeValue()
                        FirebaseStorage.getInstance().reference.child("postPictures")
                            .child(post.getPublisher()).child(postKey!!+".jpg").delete()
                        Toast.makeText(mContext, "Delete Successful", Toast.LENGTH_SHORT).show()
                    }catch (e:Exception){
                        Toast.makeText(mContext, "Delete Unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                    //Toast.makeText(mContext, postKey,Toast.LENGTH_SHORT).show()
                return true
                }
            }
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser
        userid = firebaseUser!!.uid
        val post = mPost[position]
        postKey = post.getPostid()
//        Toast.makeText(mContext,"$position",Toast.LENGTH_SHORT).show()
        publisherInfo(holder.profileImage,holder.userName,post.getPublisher())
        holder.date.setText(post.getPostDate())
        holder.description.setText(post.getDescription())
        //Picasso.get().load(post.getPostimage()).into(holder.postImage)
        Glide.with(mContext).load(post.getPostimage()).listener(object: RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.post_progress_bar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.post_progress_bar.visibility = View.GONE
                return false
            }
        }).into(holder.postImage)
        getLikeButtonStatus(postKey!!, userid!!,holder)
        getcommentButtonStatus(postKey!!,post.getPublisher(),holder)

//        val urlString:String = post.getPostimage()
//        try{
//                if(hashMap.containsKey(urlString)){
//                    holder.post_progress_bar.visibility = View.GONE
//                    holder.postImage.setImageBitmap(hashMap.get(urlString))
//                    Log.d("TAG", "Load from HashMap :: " + hashMap.get(urlString).toString())
//                }else{
//                    GlobalScope.launch {
//                        val bitImage = urlToBitmap(urlString)
//                        hashMap.put(urlString,bitImage)
//                    }
//                    holder.post_progress_bar.visibility = View.GONE
//                    holder.postImage.setImageBitmap(hashMap.get(urlString))
//                    Log.d("TAG", "Store in HashMap :: " + hashMap.get(urlString).toString())
//                }
//        }catch (e:Exception){ }

//        MobileAds.initialize(mContext)
//        val adRequest = AdRequest.Builder().build()
//        //Picasso.get().load(adRequest!!).into(holder.adView)
//        holder.adView.loadAd(adRequest)


    }

    private fun publisherInfo(profileImage: CircleImageView, userName: TextView,publisherId: String) {
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(publisherId)
        userRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    userName.text = user!!.getUsername()
                    Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(profileImage)

                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private  fun getLikeButtonStatus(postKey:String,userid:String,holder: ViewHolder){
        likeReference = FirebaseDatabase.getInstance().getReference("postPictureLikes")
        //likeReference = FirebaseDatabase.getInstance().getReference("Post")//update
        likeReference!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.child(postKey).hasChild(userid)){
                    var likeCount:Int = p0.child(postKey).childrenCount.toInt()
                    holder.likes.setText("$likeCount likes")
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                }else{
                    var likeCount:Int = p0.child(postKey).childrenCount.toInt()
                    holder.likes.setText("$likeCount likes")
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }

                /*if (p0.child(postKey).child("like").hasChild(userid)){
                    var likeCount:Int = p0.child(postKey).child("like").childrenCount.toInt()
                    holder.likes.setText("$likeCount likes")
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                }else{
                    var likeCount:Int = p0.child(postKey).child("like").childrenCount.toInt()
                    holder.likes.setText("$likeCount likes")
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }*/

            }
            override fun onCancelled(p0: DatabaseError){
            }
        })

    }

    private  fun getcommentButtonStatus(postkey:String,publisher:String,holder: ViewHolder){
//        val commentReference = FirebaseDatabase.getInstance().reference.child("Post").child(postkey).child("Comments")
        val commentReference = FirebaseDatabase.getInstance().reference.child("postPictureComments").child(publisher).child(postkey)
        commentReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val likeCount:Int = p0.childrenCount.toInt()
                holder.comments.setText("$likeCount comments")
            }
            override fun onCancelled(p0: DatabaseError){
            }
        })

    }
    private fun getDeleteButtonStatus(postkey: String,popupMenu: PopupMenu){
        val deleteReference = FirebaseDatabase.getInstance().getReference("postPictures")
            .child("allUsers").child(FirebaseAuth.getInstance().uid.toString())
            //.child("uploadVideoPost")
        deleteReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.hasChild(postKey!!)){
                    popupMenu.menu.findItem(R.id.delete_picture).setVisible(true)
                }else{
                    popupMenu.menu.findItem(R.id.delete_picture).setVisible(false)
                }
            }
            override fun onCancelled(p0: DatabaseError){
            }
        })
    }

     suspend fun urlToBitmap(url:String):Bitmap{
        val loading = ImageLoader(mContext)
        val request = ImageRequest.Builder(mContext).data(url).build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return  (result as BitmapDrawable).bitmap
    }


}