package com.example.serkanaysan.livescore.Fragment

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.serkanaysan.livescore.*
import com.example.serkanaysan.livescore.Adapter.MainAdapter
import com.example.serkanaysan.livescore.Helper.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_live.*
import okhttp3.*
import java.io.IOException

class Main: Fragment() {

    var leagueList = ArrayList<Match>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)

        var tb = (context as AppCompatActivity).findViewById<AppBarLayout>(R.id.appBarLayout)
        var navigation = (context as AppCompatActivity).findViewById<BottomNavigationView>(R.id.navigation)


        if (tb != null) {
            tb.visibility = View.VISIBLE
            navigation.visibility = View.VISIBLE

            if (Build.VERSION.SDK_INT >= 16) {
                activity!!.window.clearFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT)
                activity!!.window.decorView.systemUiVisibility = 3328
            }else{
                activity!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setupNavigation()

        setupRecylerview()

        mFirebaseAnalytics?.logEvent("livescore_main", null)

        if (leagueList.size == 0) {
            displayAds(mrAdLayout, settingsMap["MEDIUMREC_ID"], 300, 250)
        }
        else {
            displayAds(adLayout, settingsMap["BANNER_ID"], 320, 50)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    private fun setupNavigation() {
        var loading = (context as AppCompatActivity).findViewById<ConstraintLayout>(R.id.loading)
        loading.visibility = View.VISIBLE

        var backButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_back)
        backButton.visibility = View.GONE

        var calendarButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_calendar)
        calendarButton.visibility = View.VISIBLE
        var calendarText = (context as AppCompatActivity).findViewById<TextView>(R.id.textView_calendar)
        calendarText.visibility = View.VISIBLE

        var starButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_star)
        starButton.visibility = View.GONE
    }

    private fun setupRecylerview() {
        leagueList = ArrayList<Match>()
        recylerView.layoutManager = LinearLayoutManager(context)
        val url = "https://apifootball.com/api/?action=get_events&APIkey="+settingsMap["API_KEY"]+"&from=$strDate&to=$strDate"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call?, response: Response?) {
                val body = "{\"matchs\":" + response?.body()?.string() + "}"


                val gson = GsonBuilder().create()
                val liveFeed = gson.fromJson(body, LiveFeed::class.java)

                var leaugeMap = HashMap<String, Int>()
                var leaugeLiveMap = HashMap<String, Int>()

                val bannerRepeat = settingsMap["BANNER_REPEAT"]!!.toInt()
                //var i = 0
                liveFeed.matchs.forEach {
                    if (it.country_name != "" && it.league_name != "") {
                        if (leaugeMap[it.league_id] == null) {
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
                                leaugeLiveMap[it.league_id] = 1
                            }
                            else {
                                leaugeLiveMap[it.league_id] = 0
                            }
                            leaugeMap[it.league_id] = 1
                            leagueList.add(Match(it.match_id, it.country_id, it.country_name, it.league_id, it.league_name, it.match_date, it.match_status, it.match_time, it.match_hometeam_name, it.match_hometeam_score, it.match_awayteam_name, it.match_awayteam_score, it.match_hometeam_halftime_score, it.match_awayteam_halftime_score, it.match_hometeam_system, it.match_awayteam_system, it.match_live, it.goalscorer, it.cards))
                            /*i++
                            if (i%bannerRepeat == 0) {
                                val goalScorer = ArrayList<GoalScorer>()
                                var cardList = ArrayList<Cards>()
                                leagueList.add(Match("ad","","","","","","","","","","","","","","","","", goalScorer, cardList))
                            }*/
                        }
                        else {
                            if (it.match_live == "1" &&
                                    it.match_hometeam_score != "" &&
                                    it.match_hometeam_score != "?" &&
                                    it.match_awayteam_score != "" &&
                                    it.match_awayteam_score != "?" &&
                                    it.match_status != "Canc." &&
                                    it.match_status != "FT" &&
                                    it.match_status != "AAW" &&
                                    it.match_status != "AET" &&
                                    it.match_status != "AP") {
                                leaugeLiveMap[it.league_id] = leaugeLiveMap[it.league_id]!! + 1
                            }
                            leaugeMap[it.league_id] =  leaugeMap[it.league_id]!! +  1
                        }
                    }
                }

                if (activity != null) {
                    activity!!.runOnUiThread {
                        if (recylerView != null) {
                            var loading = (context as AppCompatActivity).findViewById<ConstraintLayout>(R.id.loading)
                            loading.visibility = View.GONE

                            /*if(leagueList.size < bannerRepeat) {
                                val goalScorer = ArrayList<GoalScorer>()
                                var cardList = ArrayList<Cards>()
                                leagueList.add(Match("ad","","","","","","","","","","","","","","","","", goalScorer, cardList))
                            }*/

                            recylerView.adapter = MainAdapter(leagueList, leaugeMap, leaugeLiveMap)

                            if (leagueList.size == 0) {
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
        textView_message.text = "$strDate tarihi için hiçbir kayıtlı maç bulunmamaktadır."
        displayAds(mrAdLayout, settingsMap["MEDIUMREC_ID"], 300, 250)
        constraintLayout_emptyLayout.visibility = View.VISIBLE
    }

}