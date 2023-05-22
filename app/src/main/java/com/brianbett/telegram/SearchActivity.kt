package com.brianbett.telegram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.brianbett.telegram.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class SearchActivity : AppCompatActivity() {
    private lateinit var viewPager2:ViewPager2
    private lateinit var viewPagerAdapter:ViewPagerAdapter
    private lateinit var tabLayout:TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchView:SearchView=findViewById(R.id.search_view)
        tabLayout=findViewById(R.id.tabsLayout);
        viewPager2=findViewById(R.id.container)
        viewPagerAdapter=ViewPagerAdapter(this,tabLayout)
        viewPager2.adapter=viewPagerAdapter
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem=tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                tabLayout.getTabAt(position)!!.select()
                super.onPageSelected(position)
            }
        })

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterContent(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContent(newText!!)
                return true
            }

        })

    }
    private fun filterContent(query: String) {
        val currentTabPosition = tabLayout.selectedTabPosition
        val currentFragment = viewPagerAdapter.getFragmentAtPosition(currentTabPosition)

        // Pass the search query to the current fragment
        if (currentFragment is SearchInterface) {
            currentFragment.filterContent(query)
        }
    }
}