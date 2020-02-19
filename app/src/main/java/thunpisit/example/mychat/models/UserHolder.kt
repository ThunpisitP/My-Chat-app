package thunpisit.example.mychat.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_setting.view.*
import kotlinx.android.synthetic.main.users_row.view.*
import thunpisit.example.mychat.R

class UserHolder(val customView:View):RecyclerView.ViewHolder(customView) {

    fun bind(user: User){
        customView.tv_user_name?.setText(user.display_name)
        customView.tv_status?.setText(user.status)
        if(user.thumb_image!!.equals("default")){
            Picasso.get().load(user.thumb_image).placeholder(R.drawable.ic_user).into(customView.iv_user_row)
        }
    }
}