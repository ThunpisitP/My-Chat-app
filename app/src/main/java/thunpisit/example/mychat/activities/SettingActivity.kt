package thunpisit.example.mychat.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_setting.*
import thunpisit.example.mychat.R
import java.io.ByteArrayOutputStream
import java.io.File

class SettingActivity : AppCompatActivity() {

    var mAuth:FirebaseAuth?=null
    var mDatabase:FirebaseDatabase?=null
    var mStorage:FirebaseStorage?=null
    var GALLERY_ID:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mStorage = FirebaseStorage.getInstance()

        var userId = mAuth!!.currentUser!!.uid

        var userRef = mDatabase!!.reference.child("Users").child(userId)
        userRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                finish()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var display_name = dataSnapshot!!.child("display_name").value.toString()
                var status = dataSnapshot!!.child("status").value.toString()
                var image = dataSnapshot!!.child("image").value.toString()

                tv_displayname.text = display_name
                tv_status.text = status

                if(image!="default"){
                    Picasso.get().load(image).placeholder(R.drawable.ic_user).into(iv_profile_image)
                }
            }
        })

        btn_change_status.setOnClickListener{
            var intent = Intent(this,StatusActivity::class.java)
            intent.putExtra("status",tv_status.text.toString())
            startActivity(intent)
        }

        btn_change_pic.setOnClickListener{
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent,"Select image"),GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==GALLERY_ID && resultCode == Activity.RESULT_OK){
            var image = data!!.data
            CropImage.activity(image).setAspectRatio(1,1).start(this)
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            var result = CropImage.getActivityResult(data)

            var resultUri = result.uri
            var thumbFile = File(resultUri.path)

            var thumbBitmap = Compressor(this)
                .setMaxHeight(200)
                .setMaxWidth(200)
                .setQuality(70)
                .compressToBitmap(thumbFile)

            var byteArray = ByteArrayOutputStream()
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray)
            var thumbByteArray = byteArray.toByteArray()
            var userId = mAuth!!.currentUser!!.uid

            var imageRef = mStorage!!.reference.child("profile_image").child("$userId.jpg")
            var thumbRef = mStorage!!.reference.child("profile_image").child("thumb_image").child("$userId.jpg")

            imageRef.putFile(resultUri).continueWithTask(Continuation<UploadTask.TaskSnapshot,Task<Uri>> {
                task ->
                if(!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation imageRef.downloadUrl
            }).addOnCompleteListener {
                task: Task<Uri> ->
                if(task.isComplete){
                    var imageUri = task.result.toString()
                    var uploadTask: UploadTask = thumbRef.putBytes(thumbByteArray)

                    uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot,Task<Uri>> {
                            task ->
                        if(!task.isSuccessful){
                            task.exception?.let {
                                throw it
                            }
                        }
                        return@Continuation thumbRef.downloadUrl})
                        .addOnCompleteListener { task: Task<Uri> ->
                            if(task.isComplete){
                                var thumbUri = task.result.toString()

                                var updateObject = HashMap<String,Any>()
                                updateObject.put("image",imageUri)
                                updateObject.put("thumb_image",thumbUri)

                                mDatabase!!.reference.child("Users").child(userId)
                                    .updateChildren(updateObject)
                                    .addOnCompleteListener {
                                        if(task.isSuccessful){
                                            Toast.makeText(this,"Upload success",Toast.LENGTH_SHORT).show()
                                        }else{
                                            Toast.makeText(this,"Upload not success",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        }
                }
            }
        }
    }
}
