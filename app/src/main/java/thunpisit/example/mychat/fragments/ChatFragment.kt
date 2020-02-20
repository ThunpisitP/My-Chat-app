package thunpisit.example.mychat.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chat.*
import thunpisit.example.mychat.R
import thunpisit.example.mychat.activities.ChatActivity
import thunpisit.example.mychat.models.User
import thunpisit.example.mychat.models.UserHolder

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    private var mDatabase: FirebaseDatabase?=null
    private var mAuth: FirebaseAuth?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        var userId = mAuth!!.currentUser!!.uid
        var linearLayoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        var query = mDatabase!!.reference.child("Chat").child(userId).orderByChild("Recent/date")

        var dataPhase = SnapshotParser<HashMap<String,String>>{
            snapshot: DataSnapshot ->
            var friendId = snapshot.key!!
            var chatId = snapshot.child("chat_id").value.toString()
            var lastMessage = snapshot.child("Recent").child("lastMessage").value.toString()
            var data = HashMap<String,String>()
            data.put("friendid",friendId)
            data.put("chatid",chatId)
            data.put("last_message",lastMessage)
            data

        }

        var option = FirebaseRecyclerOptions.Builder<HashMap<String,String>>()
            .setQuery(query,dataPhase)
            .setLifecycleOwner(this).build()

        var adapter = object : FirebaseRecyclerAdapter<HashMap<String,String>,UserHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
                return UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.users_row,parent,false))
            }

            override fun onBindViewHolder(holder: UserHolder, position: Int, model: HashMap<String, String>) {
                var friendId = model.get("friendid")
                var chatId = model.get("chatid")
                var lastMessage= model.get("last_message")

                var friendRef = mDatabase!!.reference.child("Users").child(friendId!!)
                friendRef.addValueEventListener(object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.exists()){
                            var friendUser = dataSnapshot.getValue(User::class.java)
                            holder.bind(friendUser!!,lastMessage!!)
                        }
                    }

                })

                holder.itemView.setOnClickListener{
                    var intent = Intent(context,ChatActivity::class.java)
                    intent.putExtra("friendId",friendId)
                    intent.putExtra("chatId",chatId)
                    startActivity(intent)
                }
            }

        }
        rv_recent.setHasFixedSize(true)
        rv_recent.layoutManager = linearLayoutManager
        rv_recent.adapter = adapter

    }

}
