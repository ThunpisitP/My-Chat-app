package thunpisit.example.mychat.fragments


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_user.*
import thunpisit.example.mychat.R
import thunpisit.example.mychat.activities.ChatActivity
import thunpisit.example.mychat.activities.ProfileActivity
import thunpisit.example.mychat.models.User
import thunpisit.example.mychat.models.UserHolder

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {
    private var mDatabase:FirebaseDatabase?=null
    private var mAuth: FirebaseAuth?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        var linearLayoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        var query = mDatabase!!.reference.child("Users").orderByChild("display_name")
        var option = FirebaseRecyclerOptions.Builder<User>().setQuery(query,User::class.java).setLifecycleOwner(this).build()
        var adapter = object : FirebaseRecyclerAdapter<User,UserHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
                return UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.users_row,parent,false))
            }

            override fun onBindViewHolder(holder: UserHolder, position: Int, model: User) {
                holder.bind(model)

                var friendId = getRef(position).key.toString()
                var userId = mAuth!!.currentUser!!.uid
                var chatId:String?=null
                var chatRef = mDatabase!!.reference.child("Chat").child(userId).child(friendId).child("chat_id")

                holder.itemView.setOnClickListener{
                    if(userId==friendId){
                        Toast.makeText(context,"Your Profile",Toast.LENGTH_SHORT).show()
                    }else {
                        var option = arrayOf("Open Profile", "Send Message")
                        var builder = AlertDialog.Builder(context)
                        builder.setTitle("Select Option")
                        builder.setItems(option) { dialog: DialogInterface?, i ->
                            if (i == 0) {
                                var intent = Intent(context, ProfileActivity::class.java)
                                intent.putExtra("userId", friendId)
                                startActivity(intent)
                            } else {
                                chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            chatId = dataSnapshot.value.toString()
                                        } else {
                                            var messageRef =
                                                mDatabase!!.reference.child("Messages").push()
                                            var userList = HashMap<String, String>()
                                            userList.put("0", userId)
                                            userList.put("1", friendId)
                                            messageRef.child("user_list").setValue(userList)

                                            chatId = messageRef.key.toString()

                                            var userDataRef =
                                                mDatabase!!.reference.child("Chat").child(userId)
                                                    .child(friendId).child("chat_id")
                                            userDataRef.setValue(chatId)
                                            var friendDataRef =
                                                mDatabase!!.reference.child("Chat").child(friendId)
                                                    .child(userId).child("chat_id")
                                            friendDataRef.setValue(chatId)
                                        }

                                        var intent = Intent(context, ChatActivity::class.java)
                                        intent.putExtra("chatId", chatId)
                                        intent.putExtra("friendId", friendId)
                                        startActivity(intent)

                                    }

                                })

                            }
                        }
                        builder.show()
                    }
                }
            }
        }

        recycler_user.setHasFixedSize(true)
        recycler_user.layoutManager = linearLayoutManager
        recycler_user.adapter = adapter
    }

}
