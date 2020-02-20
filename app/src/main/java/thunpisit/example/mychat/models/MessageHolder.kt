package thunpisit.example.mychat.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_row.view.*
import thunpisit.example.mychat.R

class MessageHolder(val customView: View):RecyclerView.ViewHolder(customView) {
    fun bind(message:String,sender:String,image:String){
        customView.tv_name_message_row_left.text = sender
        customView.tv_message_row_left.text = message

        customView.tv_name_message_row_right.text = sender
        customView.tv_message_row_right.text = message

        Picasso.get().load(image).placeholder(R.drawable.ic_user).into(customView.iv_message_row_left)
        Picasso.get().load(image).placeholder(R.drawable.ic_user).into(customView.iv_message_row_right)
    }
}