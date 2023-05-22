package com.brianbett.telegram.adapters

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brianbett.telegram.*
import com.google.android.material.tabs.TabLayout

class ViewPagerAdapter(fragmentActivity:SearchActivity, private val tabLayout: TabLayout) :FragmentStateAdapter(fragmentActivity){
    private val fragments: SparseArray<Fragment> = SparseArray()
    override fun getItemCount(): Int {
        return tabLayout.tabCount
    }


    override fun createFragment(position: Int): Fragment {
        val fragment=when(position){
            0->ChatsSFragment()
            1->MediaFragment()
            2->DownloadsFragment()
            3->FilesFragment()
            4->VoiceFragment()
            5->MusicFragment()
            else -> ChatsSFragment()
        }
        fragments.put(position, fragment)
        return fragment
    }
    fun getFragmentAtPosition(position: Int): Fragment? {
        return fragments.get(position)
    }
}