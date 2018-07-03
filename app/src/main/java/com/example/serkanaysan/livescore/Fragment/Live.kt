package com.example.serkanaysan.livescore.Fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.serkanaysan.livescore.Adapter.LiveAdapter
import com.example.serkanaysan.livescore.Helper.*
import com.example.serkanaysan.livescore.LiveFeed
import com.example.serkanaysan.livescore.Match
import com.example.serkanaysan.livescore.R
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_live.*
import okhttp3.*
import java.io.IOException



class Live: Fragment() {

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

        mFirebaseAnalytics?.logEvent("livescore_live", null)

        if (matchList.size == 0) {
            displayAds(mrAdLayout, settingsMap["MEDIUMREC_ID"], 300, 250)
        }
        else {
            displayAds(adLayout, settingsMap["BANNER_ID"], 320, 50)
        }

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
        return inflater!!.inflate(R.layout.fragment_live, container, false)
    }

    private fun setupNavigation() {
        var loading = (context as AppCompatActivity).findViewById<ConstraintLayout>(R.id.loading)
        loading.visibility = View.VISIBLE

        var backButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_back)
        backButton.visibility = View.GONE

        var calendarButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_calendar)
        calendarButton.visibility = View.GONE
        var calendarText = (context as AppCompatActivity).findViewById<TextView>(R.id.textView_calendar)
        calendarText.visibility = View.GONE

        var starButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_star)
        starButton.visibility = View.GONE
    }

    private fun setupRecylerview() {
        matchList = ArrayList<Match>()
        recylerView.layoutManager = LinearLayoutManager(context)
        val url = "https://apifootball.com/api/?action=get_events&APIkey="+settingsMap["API_KEY"]+"&from=$strToday&to=$strToday"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                val body = "{\"matchs\":" + response?.body()?.string() + "}"

                val gson = GsonBuilder().create()
                val liveFeed = gson.fromJson(body, LiveFeed::class.java)

                liveFeed.matchs.forEach {
                    if (it.match_live == "1" &&
                            it.match_hometeam_score != "" &&
                            it.match_hometeam_score != "?" &&
                            it.match_awayteam_score != "" &&
                            it.match_awayteam_score != "?" &&
                            it.match_status != "Canc." &&
                            it.match_status != "Aband." &&
                            it.match_status != "FT" &&
                            it.match_status != "AAW" &&
                            it.match_status != "AET" &&
                            it.match_status != "AP") {
                        matchList.add(Match(it.match_id, it.country_id, it.country_name, it.league_id, it.league_name, it.match_date, it.match_status, it.match_time, it.match_hometeam_name, it.match_hometeam_score, it.match_awayteam_name, it.match_awayteam_score, it.match_hometeam_halftime_score, it.match_awayteam_halftime_score, it.match_hometeam_system, it.match_awayteam_system, it.match_live, it.goalscorer, it.cards))
                    }
                }



                if (activity != null) {
                    activity!!.runOnUiThread {
                        if (recylerView != null) {
                            var loading = (context as AppCompatActivity).findViewById<ConstraintLayout>(R.id.loading)
                            loading.visibility = View.GONE
                            recylerView.adapter = LiveAdapter(matchList)

                            if (matchList.size == 0) {
                                showEmptyMessage()
                            }
                            else {
                                displayAds(adLayout, settingsMap["BANNER_ID"], 320, 50)
                            }
                        }
                    }
                }


            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failure")
            }
        })
    }

    private fun showEmptyMessage() {
        textView_message.text = "Şu anda hiçbir karşılaşma oynanmamaktadır."
        displayAds(mrAdLayout, settingsMap["MEDIUMREC_ID"], 300, 250)
        constraintLayout_emptyLayout.visibility = View.VISIBLE
    }



}