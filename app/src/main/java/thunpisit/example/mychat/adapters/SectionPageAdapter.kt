package thunpisit.example.mychat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import thunpisit.example.mychat.fragments.ChatFragment
import thunpisit.example.mychat.fragments.UserFragment

class SectionPageAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return UserFragment()
            1 -> return ChatFragment()
        }
        return null!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> return "User"
            1 -> return "Chat"
        }
        return null!!

    }
}