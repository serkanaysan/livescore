package com.example.serkanaysan.livescore.Fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.serkanaysan.livescore.*
import com.example.serkanaysan.livescore.Adapter.MatchDetailTabPagerAdapter
import com.example.serkanaysan.livescore.Helper.*
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_match_detail.*
import okhttp3.*
import java.io.IOException


@SuppressLint("ValidFragment")
class MatchDetail(var match: Match): Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)

        configureTabLayout()


    }

    override fun onStart() {
        super.onStart()
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        loading_matchdetail.visibility = View.VISIBLE

        setupNavigation()

        mFirebaseAnalytics?.logEvent("livescore_match_detail", null)

        fetchJSON()

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
        return inflater!!.inflate(R.layout.fragment_match_detail, container, false)
    }

    private fun setupNavigation() {
        var backButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_back)
        backButton.visibility = View.VISIBLE

        backButton.setOnClickListener {
            val manager = fragmentManager
            manager!!.popBackStack()
        }

        var calendarButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_calendar)
        calendarButton.visibility = View.GONE
        var calendarText = (context as AppCompatActivity).findViewById<TextView>(R.id.textView_calendar)
        calendarText.visibility = View.GONE

        var starButton = (context as AppCompatActivity).findViewById<RelativeLayout>(R.id.relativeLayout_star)
        starButton.visibility = View.VISIBLE
        val starImage = (context as AppCompatActivity).findViewById<ImageView>(R.id.imageView_star)

        if (favouriteMap[match.match_id] == null) {
            starImage.tag = 0
            starImage.setImageResource(R.drawable.star_empty_toolbar)
        }
        else {
            starImage.tag = 1
            starImage.setImageResource(R.drawable.star_toolbar)
        }

        starImage.setOnClickListener {
            // Write a message to the database

            val android_id = Settings.Secure.getString(context!!.contentResolver,
                    Settings.Secure.ANDROID_ID)

            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("v1").child("favourite").child(android_id).child(match.match_id)

            if (starImage.tag == 0) {
                starImage.tag = 1
                starImage.setImageResource(R.drawable.star_toolbar)

                myRef.setValue(true)

                favouriteMap[match.match_id] = true
            }
            else {
                starImage.tag = 0
                starImage.setImageResource(R.drawable.star_empty_toolbar)

                myRef.removeValue()

                favouriteMap[match.match_id] = null
            }
        }
    }

    private fun fetchJSON() {
        val url = "https://apifootball.com/api/?action=get_events&from=$strDate&to=$strDate&league_id="+match.league_id+"&APIkey="+settingsMap["API_KEY"]
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                val body = "{\"matchs\":" + response?.body()?.string() + "}"
                val gson = GsonBuilder().create()
                val liveFeed = gson.fromJson(body, LiveFeed::class.java)

                liveFeed.matchs.forEach {
                    if (it.match_id == match.match_id){
                        var matchJSON = Match(it.match_id, it.country_id, it.country_name, it.league_id, it.league_name, it.match_date, it.match_status, it.match_time, it.match_hometeam_name, it.match_hometeam_score, it.match_awayteam_name, it.match_awayteam_score, it.match_hometeam_halftime_score, it.match_awayteam_halftime_score, it.match_hometeam_system, it.match_awayteam_system, it.match_live, it.goalscorer, it.cards)
                        if (activity != null) {
                            activity!!.runOnUiThread {
                                setupMatchdetail(matchJSON)
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

    private fun setupMatchdetail(obj: Match) {
        if(countryFlag[match.country_name] != null) {
            imageView_flag.setImageResource(countryFlag[match.country_name]!!)
        }

        if(countryNameENTR[obj.country_name] == null) {
            textView_country.text = obj.country_name + ":"
        }
        else {
            textView_country.text = countryNameENTR[obj.country_name] + ":"
        }
        textView_league.text = obj.league_name

        if (obj.match_hometeam_score == "?" || obj.match_hometeam_score == "") {
            textView_homescore.text = "-"
        }
        else {
            textView_homescore.text = obj.match_hometeam_score
        }

        if (obj.match_awayteam_score == "?" || obj.match_hometeam_score == "") {
            textView_awayscore.text = "-"
        }
        else {
            textView_awayscore.text = obj.match_awayteam_score
        }

        when {
            obj.match_status == "HT" -> textView_time.text = "Devre Arası"
            obj.match_status == "FT" -> textView_time.text = "Maç Sonu"
            obj.match_status == "Canc." -> textView_time.text = "İptal"
            obj.match_status == "" -> textView_time.text = obj.match_time
            else -> textView_time.text = obj.match_status
        }

        val day = obj.match_date.substring(8)
        val month = obj.match_date.substring(5, 7)
        val year = obj.match_date.substring(0, 4)
        textView_date.text = day.padStart(2, '0') + "." + month.padStart(2, '0') + "." + year

        textView_home.text = obj.match_hometeam_name
        textView_away.text = obj.match_awayteam_name

        if (obj.match_live == "1" &&
                obj.match_hometeam_score != "" &&
                obj.match_hometeam_score != "?" &&
                obj.match_awayteam_score != "" &&
                obj.match_awayteam_score != "?" &&
                obj.match_status != "Canc." &&
                obj.match_status != "Aband." &&
                obj.match_status != "FT" &&
                obj.match_status != "AAW" &&
                obj.match_status != "AET" &&
                obj.match_status != "AP") {
            textView_homescore.setTextColor(Color.parseColor("#ffcc0000"))
            textView_awayscore.setTextColor(Color.parseColor("#ffcc0000"))
            textView_time.setTextColor(Color.parseColor("#ffcc0000"))
        }
        else {
            textView_homescore.setTextColor(Color.parseColor("#cccccc"))
            textView_awayscore.setTextColor(Color.parseColor("#cccccc"))
            textView_time.setTextColor(Color.parseColor("#555555"))
        }

        loading_matchdetail.visibility = View.GONE
    }

    private fun configureTabLayout() {
        tab_layout.addTab(tab_layout.newTab().setText("Oranlar"))
        tab_layout.addTab(tab_layout.newTab().setText("Ev Sahibi"))
        tab_layout.addTab(tab_layout.newTab().setText("Deplasman"))
        tab_layout.addTab(tab_layout.newTab().setText("H2H"))

        val adapter = MatchDetailTabPagerAdapter(childFragmentManager, match, tab_layout.tabCount)
        pager.adapter = adapter
        pager.offscreenPageLimit = 4



        pager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //((tab_layout)adapter.getItem(position)).refresh()
                pager.currentItem = tab.position


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }
}
