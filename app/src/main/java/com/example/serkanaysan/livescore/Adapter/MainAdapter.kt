package com.example.serkanaysan.livescore.Adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.serkanaysan.livescore.Fragment.MainDetail
import com.example.serkanaysan.livescore.Helper.countryFlag
import com.example.serkanaysan.livescore.Helper.countryNameENTR
import com.example.serkanaysan.livescore.Helper.displayAds
import com.example.serkanaysan.livescore.Helper.settingsMap
import com.example.serkanaysan.livescore.League
import com.example.serkanaysan.livescore.Match
import com.example.serkanaysan.livescore.R
import com.example.serkanaysan.livescore.R.id.view
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherAdView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.main_row.view.*


class MainAdapter(val mainFeed: ArrayList<Match>, val mainMap: HashMap<String, Int>, val mainLiveMap: HashMap<String, Int>): RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return mainFeed.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_row,parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val adLayout = holder.view.adLayout
        if (mainFeed[position].match_id == "ad"){
            adLayout.visibility = View.VISIBLE
            (holder.view.context as AppCompatActivity).runOnUiThread {
                //displayAds(adLayout)
            }


        }
        else {
            adLayout.visibility = View.GONE
        }

        val countryameText = holder.view.textView_countryname

        if(countryNameENTR[mainFeed[position].country_name] == null) {
            countryameText.text = mainFeed[position].country_name + ":"
        }
        else {
            countryameText.text = countryNameENTR[mainFeed[position].country_name] + ":"
        }

        val leaguenameText = holder.view.textView_leaguename
        leaguenameText.text = mainFeed[position].league_name

        val countliveText = holder.view.textView_livecount
        val countText = holder.view.textView_count

        var count = 0
        var countLive = 0

        if (mainMap[mainFeed[position].league_id] != null) {
            count = mainMap[mainFeed[position].league_id]!!
            if (mainLiveMap[mainFeed[position].league_id] != null) {
                countLive = mainLiveMap[mainFeed[position].league_id]!!
            }
        }

        count -= countLive

        countText.text = count.toString()
        countliveText.text = countLive.toString()

        when (count) {
            0 -> countText.visibility = View.GONE
            else -> countText.visibility = View.VISIBLE
        }

        when (countLive) {
            0 -> countliveText.visibility = View.GONE
            else -> countliveText.visibility = View.VISIBLE
        }


        val flagImageview = holder.view.imageView_flag
        if(countryFlag[mainFeed[position].country_name] != null) {
            flagImageview.setImageResource(countryFlag[mainFeed[position].country_name]!!)
            flagImageview.visibility = View.VISIBLE
        }
        else{
            flagImageview.visibility = View.GONE
        }

        holder.view.setOnClickListener {
            val manager = (holder.view.context as AppCompatActivity).supportFragmentManager
            val transaction = manager.beginTransaction()
            var fragment: Fragment = MainDetail(mainFeed[position])
            transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            transaction.replace(R.id.content_main, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }





    }
}