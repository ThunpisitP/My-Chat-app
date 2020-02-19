package thunpisit.example.mychat.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*
import thunpisit.example.mychat.R
import thunpisit.example.mychat.adapters.SectionPageAdapter

class DashboardActivity : AppCompatActivity() {

    private var mAuth:FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.title = "Dashboard"

        mAuth = FirebaseAuth.getInstance()

        var sectionAdapter = SectionPageAdapter(supportFragmentManager)
        vp_dashboard.adapter = sectionAdapter
        tl_dashboard.setupWithViewPager(vp_dashboard)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item!=null){
            if(item.itemId == R.id.menu_settings){
                var intent = Intent(this,SettingActivity::class.java)
                startActivity(intent)
            }else if(item.itemId == R.id.menu_logout){
                mAuth!!.signOut()
                var intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }
}
