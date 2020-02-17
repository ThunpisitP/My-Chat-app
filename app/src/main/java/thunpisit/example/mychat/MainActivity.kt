package thunpisit.example.mychat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSignin.setOnClickListener{
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        btnCreateAccount.setOnClickListener{
            var intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
