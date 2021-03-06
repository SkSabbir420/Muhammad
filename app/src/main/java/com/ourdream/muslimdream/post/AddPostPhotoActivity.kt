package com.ourdream.muslimdream.post

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.ourdream.muslimdream.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.activity_add_post_photo.*
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.collections.HashMap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddPostPhotoActivity : AppCompatActivity() {

    private var myUrl = ""
    private var classify:String? = null
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null
    private var bitmap: Bitmap? = null
    protected var tflite: Interpreter? = null
    private var imageSizeX = 0
    private var imageSizeY = 0
    private var inputImageBuffer: TensorImage? = null
    private var outputProbabilityBuffer: TensorBuffer? = null
    private var probabilityProcessor: TensorProcessor? = null
    private var labels: List<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_photo)

        storagePostPicRef = FirebaseStorage.getInstance().reference.child("postPictures")

        try {
            ///Main code 1
            tflite = Interpreter(loadmodelfile(this))
        } catch (e: Exception) {
            //e.printStackTrace();
            Toast.makeText(this, "Dont load model!!", Toast.LENGTH_SHORT).show()
        }

        save_new_post_btn.setOnClickListener {
            uploadImage()
        }
        close_add_post_btn.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        image_post.setOnClickListener {
            CropImage.activity().setAspectRatio(4, 3).
            start(this@AddPostPhotoActivity)
        }



//        video_post.setOnClickListener {
//            loadImage()
//        }
//        image_post.setOnClickListener {
//            loadImage()
//        }

        //Main code 2.
        save_new_post_btn.setOnClickListener {
            val imageTensorIndex = 0
            val imageShape =
                tflite!!.getInputTensor(imageTensorIndex).shape() // {1, height, width, 3}
            //Toast.makeText(MainActivity.this,Integer.toString(imageShape[1]),Toast.LENGTH_SHORT).show();
            imageSizeY = imageShape[1] //224
            //Toast.makeText(MainActivity.this,Integer.toString(imageSizeY),Toast.LENGTH_SHORT).show();
            imageSizeX = imageShape[2] //224
            //Toast.makeText(MainActivity.this,Integer.toString(imageSizeX),Toast.LENGTH_SHORT).show();
            val imageDataType = tflite!!.getInputTensor(imageTensorIndex).dataType()
            val probabilityTensorIndex = 0
            val probabilityShape = tflite!!.getOutputTensor(probabilityTensorIndex).shape() // {1, NUM_CLASSES}
            //Toast.makeText(MainActivity.this,Integer.toString(probabilityShape[0]),Toast.LENGTH_SHORT).show();
            val probabilityDataType = tflite!!.getOutputTensor(probabilityTensorIndex).dataType()
            inputImageBuffer = TensorImage(imageDataType)
            inputImageBuffer = loadImage(bitmap) ///vvimp
            outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType)
            probabilityProcessor = TensorProcessor.Builder().add(postprocessNormalizeOp).build()
            tflite!!.run(inputImageBuffer!!.buffer, outputProbabilityBuffer!!.buffer.rewind())
            showresult()
            uploadImage()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri

            //image_post.setImageURI(imageUri)
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//                image_post.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(){
        if(description_post.text.toString().isEmpty()){
            description_post.error="Please Write your description"
            description_post.requestFocus()
            return
        }else if (imageUri ==null){
                Toast.makeText(this, "Please add your photo", Toast.LENGTH_LONG).show()
                return
        }else{
//                val progressDialog = ProgressDialog(this)
//                progressDialog.setTitle("Adding your Post")
//                progressDialog.setMessage("Please wait...")
//                progressDialog.show()

            val notification = NotificationCompat
                .Builder(this, "content_loading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("Please wait")
                .setContentTitle("Uploading")
                .setTicker("Uploading")
                .setProgress(0, 100, true)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            val no: NotificationManagerCompat = NotificationManagerCompat.from(this)
            no.notify(2,notification.build())
            super.onBackPressed()

                val ref = FirebaseDatabase.getInstance().reference.child("postPictureTemporary")
//                val ref = FirebaseDatabase.getInstance().reference.child("postPictures")
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                val postId = ref.push().key
                val fileRef = storagePostPicRef!!.child(uid).child(postId.toString() + ".jpg")
                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)
                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw it
                            //progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        //val ref = FirebaseDatabase.getInstance().reference.child("Temporary")

                        val localdate = LocalDate.now()
                        val localtime  = LocalTime.now()
                        val date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localdate).toString()
                        val time = DateTimeFormatter.ofPattern("HH:mm:ss").format(localtime).toString()



                        val postMap = HashMap<String, Any>()
                        postMap["postid"] = postId!!
                        postMap["description"] = description_post.text.toString()
                        postMap["publisher"] = uid
                        postMap["postimage"] = myUrl
//                        postMap["photoClassify"] = classify!!
                        postMap["postDate"] = date
                        postMap["postTime"] = time
                        if(classify == "Islamic") {


                            //ref.child(postId).updateChildren(postMap)
                            FirebaseDatabase.getInstance().reference.child("postPictures")
                                .child("allUsers").child(uid)
                                .child(postId!!).updateChildren(postMap)
                            FirebaseDatabase.getInstance().reference.child("postPictures")
                                .child("allPostPictures")
                                .child(postId!!).updateChildren(postMap)

                            //Toast.makeText(this, "Post upload successfully.\nWait for verification", Toast.LENGTH_LONG).show()
                            Toast.makeText(this, "Post upload successfully", Toast.LENGTH_LONG)
                                .show()
                            // progressDialog.dismiss()
                            notification.setContentTitle("Upload Complete")
                                //.setContentText("Wait for verification")
                                .setContentText("Congratulation")
                                .setProgress(0, 0, false)
                                .setOngoing(false)
                            no.notify(2, notification.build())
//                        val intent = Intent(this@AddPostPhotoActivity, MainActivity::class.java)
//                        startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this, "Post Not Accepted", Toast.LENGTH_LONG)
                                .show()
                            // progressDialog.dismiss()
                            notification.setContentTitle("Post Not Accepted")
                                //.setContentText("Wait for verification")
                                .setContentText("add Muslim post")
                                .setProgress(0, 0, false)
                                .setOngoing(false)
                            no.notify(2, notification.build())
//                        val intent = Intent(this@AddPostPhotoActivity, MainActivity::class.java)
//                        startActivity(intent)
                            finish()
                        }

                    } else{
                        Toast.makeText(this, "Post upload Unsuccessfully", Toast.LENGTH_LONG).show()
                        //progressDialog.dismiss()
                        notification.setContentTitle("Upload Unsuccessfull")
                            .setContentText("Try Again")
                            .setProgress(0,0,false)
                            .setOngoing(false)
                        no.notify(2,notification.build())
                        super.onBackPressed()
                        finish()

                    }
                } )
        }
    }

    //Main code 1.
    //@Throws(IOException::class)
    fun loadmodelfile(activity: Activity): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd("model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startoffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        //Toast.makeText(MainActivity.this,Long.toString(declaredLength),Toast.LENGTH_SHORT).show();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startoffset, declaredLength)
    }

    private fun showresult() {
        try {
            labels = FileUtil.loadLabels(this, "label.txt")
        }catch (e: Exception) {
            Toast.makeText(this, "testtext not work", Toast.LENGTH_SHORT).show()
        }

        try {
            val labeledProbability = TensorLabel(labels!!,
                probabilityProcessor!!.process(outputProbabilityBuffer) ).mapWithFloatValue
            val maxValueInMap = Collections.max(labeledProbability.values)
            for ((key, value) in labeledProbability) {
                if (value == maxValueInMap) {
                    Toast.makeText(this, key, Toast.LENGTH_SHORT).show()
                    //classitext!!.text = key
                    classify = key
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "show 2nd part not work!!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadImage(bitmap: Bitmap?): TensorImage {
        // Loads bitmap into a TensorImage.
        inputImageBuffer!!.load(bitmap!!)

        // Creates processor for the TensorImage.
        val cropSize = Math.min(bitmap.width, bitmap.height)
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(preprocessNormalizeOp)
            .build()
        return imageProcessor.process(inputImageBuffer)
    }
    private val preprocessNormalizeOp: TensorOperator
        private get() = NormalizeOp(IMAGE_MEAN, IMAGE_STD)
    private val postprocessNormalizeOp: TensorOperator
        private get() = NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD)

    companion object {
        private const val IMAGE_MEAN = 0.0f
        private const val IMAGE_STD = 1.0f
        private const val PROBABILITY_MEAN = 0.0f
        private const val PROBABILITY_STD = 255.0f
    }


}//end.
