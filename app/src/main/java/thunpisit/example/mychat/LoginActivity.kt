package thunpisit.example.mychat

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var mAuth:FirebaseAuth?=null
    var mDatabase:FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        btnLogin.setOnClickListener{

            var email = etEmail.text.toString().trim()
            var password = etPassword.text.toString().trim()
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
