package thunpisit.example.mychat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import thunpisit.example.mychat.R

class MainActivity : AppCompatActivity() {
    var mAuth:FirebaseAuth?=null
    var mAuthListener:FirebaseAuth.AuthStateListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSignin.setOnClickListener{
            var intent = Intent(this,
                LoginActivity::class.java)
            startActivity(intent)
        }

        btnCreateAccount.setOnClickListener{
            var intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth ->
            var user = firebaseAuth.currentUser
            if(user!=null){
                var intent = Intent(this,
                    DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        mAuth!!.removeAuthStateListener(mAuthListener!!)
    }
}
