package thunpisit.example.mychat.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.users_row.view.*
import thunpisit.example.mychat.R

class UserHolder(val customView:View):RecyclerView.ViewHolder(customView) {

    fun bind(user: User){
        customView.tv_user_name?.setText(user.display_name)
        customView.tv_user_status?.setText(user.status)
        if(!user.thumb_image!!.equals("default")){
            Picasso.get().load(user.thumb_image).placeholder(R.drawable.ic_user).into(customView.iv_user_row)
        }
    }

    fun bind(user:User,recent:String){
        customView.tv_user_name?.setText(user.display_name)
        if(recent!=null && recent != "null"){
            customView.tv_user_status?.setText(recent)
        }else{
            customView.tv_user_status?.setText("")
        }

        if(!user.thumb_image!!.equals("default")){
            Picasso.get().load(user.thumb_image).placeholder(R.drawable.ic_user).into(customView.iv_user_row)
        }
    }
}