package thunpisit.example.mychat.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_status.*
import thunpisit.example.mychat.R

class StatusActivity : AppCompatActivity() {
    var mDatabase:FirebaseDatabase?=null
    var mCurrentUser:FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        if(intent.extras != null){
            var status = intent.extras!!.get("status").toString()
            etStatus.setText(status)
        }

        btnUpdateStatus.setOnClickListener{
            var updateStatus = etStatus.text.toString().trim()

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var uid = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance()
            var statusRef = mDatabase!!.reference.child("Users").child(uid).child("status")
            statusRef.setValue(updateStatus).addOnCompleteListener {
                task: Task<Void> ->
                if(task.isSuccessful){
                    Toast.makeText(this,"Update Successful",Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this,"Update Unsuccessful",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
