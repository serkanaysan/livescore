package com.example.serkanaysan.livescore.Fragment.Tab

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
import com.example.serkanaysan.livescore.Adapter.OddAdapter
import com.example.serkanaysan.livescore.Helper.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.tab_odds.*
import okhttp3.*
import java.io.IOException


@SuppressLint("ValidFragment")
class Odds(var match: Match): Fragment() {

    var oddList = ArrayList<Odd>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mFirebaseAnalytics?.logEvent("livescore_match_detail_odds", null)

        loading_tab.visibility = View.VISIBLE
        constraintLayout_emptyLayout.visibility = View.GONE

        setupRecylerview()

        if (oddList.size == 0) {
            displayAds(mrAdLayout, settingsMap["MEDIUMREC_ID"], 300, 250)
        }
        else {
            displayAds(adLayout, settingsMap["BANNER_ID"], 320, 50)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater!!.inflate(R.layout.tab_odds, container, false)
    }

    private fun setupRecylerview() {
        oddList = ArrayList<Odd>()
        recylerView.layoutManager = LinearLayoutManager(context)
        val url = "https://apifootball.com/api/?action=get_odds&match_id="+match.match_id+"&from=$strDate&to=$strDate&APIkey="+settingsMap["API_KEY"]
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                val body = "{\"odds\":" + response?.body()?.string() + "}"

                val gson = GsonBuilder().create()

                if (!body.contains("No odd found")) {
                    val oddFeed = gson.fromJson(body, OddFeed::class.java)

                    oddFeed.odds.forEach {
                        oddList.add(Odd(it.match_id,it.odd_bookmakers,it.odd_date,it.odd_1,it.odd_x,it.odd_2))
                    }

                    if (activity != null) {
                        activity!!.runOnUiThread {
                            if (recylerView != null) {
                                recylerView.adapter = OddAdapter(oddList)

                                if (oddFeed.odds.isEmpty()) {
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
                else {
                    if (activity != null) {
                        activity!!.runOnUiThread {
                            showEmptyMessage()
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
        textView_message.text = "Bu maç için sistemde kayıtlı bir oran bulunmamaktadır."

        displayAds(mrAdLayout, settingsMap["MEDIUMREC_ID"], 300, 250)

        constraintLayout_emptyLayout.visibility = View.VISIBLE
    }

}