package com.example.serkanaysan.livescore.Fragment.Tab

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.example.serkanaysan.livescore.*
import com.example.serkanaysan.livescore.Adapter.H2HAdapter
import com.example.serkanaysan.livescore.Helper.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.tab_hometeam.*
import okhttp3.*
import java.io.IOException

@SuppressLint("ValidFragment")
class AwayTeam(var match: Match): Fragment() {

    var matchList = ArrayList<Match>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)

        loading_tab.visibility = View.VISIBLE

        setupRecylerview()

    }

    override fun onStart() {
        super.onStart()
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mFirebaseAnalytics?.logEvent("livescore_match_detail_awayteam", null)

        if (matchList.size == 0) {
            displayAds(mrAdLayout, settingsMap["MEDIUMREC_ID"], 300, 250)
        }
        else {
            displayAds(adLayout, settingsMap["BANNER_ID"], 320, 50)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater!!.inflate(R.layout.tab_hometeam, container, false)
    }

    private fun setupRecylerview() {
        matchList = ArrayList<Match>()
        recylerView.layoutManager = LinearLayoutManager(context)

        val url = "https://apifootball.com/api/?action=get_H2H&firstTeam="+match.match_hometeam_name+"&secondTeam="+match.match_awayteam_name+"&APIkey="+settingsMap["API_KEY"]
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()

                val gson = GsonBuilder().create()
                val awayteamFeed = gson.fromJson(body, AwayTeamFeed::class.java)


                val goalScorer = ArrayList<GoalScorer>()
                var cardList = ArrayList<Cards>()

                awayteamFeed.secondTeam_lastResults.forEach {
                    matchList.add(Match(it.match_id, it.country_id, it.country_name, it.league_id, it.league_name, it.match_date, it.match_status, it.match_time, it.match_hometeam_name, it.match_hometeam_score, it.match_awayteam_name, it.match_awayteam_score, it.match_hometeam_halftime_score, it.match_awayteam_halftime_score, "", "", it.match_live, goalScorer, cardList))
                }

                if (activity != null) {
                    activity!!.runOnUiThread {
                        if (recylerView != null) {
                            recylerView.adapter = H2HAdapter(matchList, match.match_awayteam_name)

                            if (matchList.size == 0) {
                                showEmptyMessage()
                            }
                            else {
                                displayAds(adLayout, settingsMap["BANNER_ID"], 320, 50)
                            }

                            loading_tab.visibility = View.GONE
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
        textView_message.text = "Deplasman takımı için sistemde kayıtlı bir maç verisi bulunmamaktadır."

        displayAds(mrAdLayout, settingsMap["MEDIUMREC_ID"], 300, 250)

        constraintLayout_emptyLayout.visibility = View.VISIBLE
    }





}