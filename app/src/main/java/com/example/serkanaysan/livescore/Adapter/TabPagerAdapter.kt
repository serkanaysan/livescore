package com.example.serkanaysan.livescore.Adapter

/**
 * Created by serkan on 23.01.2018.
 */

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.serkanaysan.livescore.Fragment.Tab.AwayTeam
import com.example.serkanaysan.livescore.Fragment.Tab.H2H
import com.example.serkanaysan.livescore.Fragment.Tab.HomeTeam
import com.example.serkanaysan.livescore.Fragment.Tab.Odds
import com.example.serkanaysan.livescore.Match


class MatchDetailTabPagerAdapter(fm: FragmentManager,private  var match: Match, private var tabCount: Int) :
        FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> Odds(match)
            1 -> HomeTeam(match)
            2 -> AwayTeam(match)
            3 -> H2H(match)
            else -> Odds(match)
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}
