package com.example.serkanaysan.livescore.Fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.serkanaysan.livescore.*
import com.example.serkanaysan.livescore.Adapter.LiveAdapter
import com.example.serkanaysan.livescore.Helper.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_main_detail.*
import okhttp3.*
import java.io.IOException

@SuppressLint("ValidFragment")
class MainDetail(var league: Match): Fragment() {

    var matchList = ArrayList<Match>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        var loading = (context as AppCompatActivity).findViewById<ConstraintLayout>(R.id.loading)
        loading.visibility = View.VISIBLE

        setupNavigation()

        setupRecylerview()

        mFirebaseAnalytics?.logEvent("livescore_main_detail", null)

        if (!settingsMap["INTERSTITIAL_ID"].isNullOrEmpty()) {
            if (mPublisherInterstitialAd.isLoaded) {
                mPublisherInterstitialAd.show()
            } else {
                println("The interstitial wasn't loaded yet.")
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater!!.inflate(R.layout.fragment_main_detail, container, false)
    }

    private fun setupNavigation() {
        var backButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_back)
        backButton.visibility = View.VISIBLE

        backButton.setOnClickListener {
            val manager = fragmentManager
            manager!!.popBackStack()
        }

        var calendarButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_calendar)
        calendarButton.visibility = View.VISIBLE
        var calendarText = (context as AppCompatActivity).findViewById<TextView>(R.id.textView_calendar)
        calendarText.visibility = View.VISIBLE

        var starButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_star)
        starButton.visibility = View.GONE
    }



    private fun setupRecylerview() {
        matchList = ArrayList<Match>()
        recylerView.layoutManager = LinearLayoutManager(context)
        val url = "https://apifootball.com/api/?action=get_events&from=$strDate&to=$strDate&league_id="+league.league_id+"&APIkey="+settingsMap["API_KEY"]
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                val body = "{\"matchs\":" + response?.body()?.string() + "}"


                val gson = GsonBuilder().create()
                val liveFeed = gson.fromJson(body, LiveFeed::class.java)



                liveFeed.matchs.forEach {
                    matchList.add(Match(it.match_id, it.country_id, it.country_name, it.league_id, it.league_name, it.match_date, it.match_status, it.match_time, it.match_hometeam_name, it.match_hometeam_score, it.match_awayteam_name, it.match_awayteam_score, it.match_hometeam_halftime_score, it.match_awayteam_halftime_score, it.match_hometeam_system, it.match_awayteam_system, it.match_live, it.goalscorer, it.cards))

                }

                if (activity != null) {
                    activity!!.runOnUiThread {
                        if (recylerView != null) {
                            var loading = (context as AppCompatActivity).findViewById<ConstraintLayout>(R.id.loading)
                            loading.visibility = View.GONE
                            recylerView.adapter = LiveAdapter(matchList)

                            checkAds()
                        }
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failure")
            }
        })
    }

    private fun checkAds(){
        if (matchList.size < 5) {
            adLayout.layoutParams.width = dp2px(300)
            adLayout.layoutParams.height = dp2px(250)
            displayAds(adLayout, settingsMap["MEDIUMREC_ID"], 300, 250)
        }
        else {
            displayAds(adLayout, settingsMap["BANNER_ID"], 320, 50)
        }
    }

    private fun dp2px(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }



}