package thunpisit.example.mychat.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import thunpisit.example.mychat.R

class SignupActivity : AppCompatActivity() {

    var mAuth:FirebaseAuth?=null
    var mDatabase:FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        btnSignup.setOnClickListener{
            var displayName = etDisplayName.text.toString().trim()
            var userEmail = etStatus.text.toString().trim()
            var userPassword = etPassword.text.toString().trim()
            createUser(displayName,userEmail,userPassword)

        }
    }

    private fun createUser(displayName:String,userEmail:String,userPassword:String){
        if(!TextUtils.isEmpty(displayName) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)){
            mAuth!!.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener {
                    task: Task<AuthResult> ->
                if(task.isSuccessful){
                    sendUserDataToFirebase(displayName)
                }else{
                    Toast.makeText(this,"Create Unsuccessful",Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this,"Invalid Input",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendUserDataToFirebase(displayName: String){
        var user = mAuth!!.currentUser
        var userId = user!!.uid

        var userRef = mDatabase!!.reference.child("Users").child(userId)

        var userObj = HashMap<String,String>()
        userObj.put("display_name",displayName)
        userObj.put("status","Hello, My chat")
        userObj.put("image","default")
        userObj.put("thumb_image","default")

        userRef.setValue(userObj).addOnCompleteListener {
                task: Task<Void> ->
            if(task.isSuccessful){
                Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()

                var intent = Intent(this,
                    DashboardActivity::class.java)
                intent.putExtra("userId",userId)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"Unsuccessful",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
