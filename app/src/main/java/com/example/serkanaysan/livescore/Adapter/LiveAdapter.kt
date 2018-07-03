package com.example.serkanaysan.livescore.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.serkanaysan.livescore.Fragment.MatchDetail
import com.example.serkanaysan.livescore.Match
import com.example.serkanaysan.livescore.R
import kotlinx.android.synthetic.main.live_row.view.*
import com.google.firebase.database.FirebaseDatabase
import android.view.View
import com.example.serkanaysan.livescore.Helper.countryFlag
import com.example.serkanaysan.livescore.Helper.countryNameENTR
import com.example.serkanaysan.livescore.Helper.favouriteMap


class LiveAdapter(val liveFeed: ArrayList<Match>): RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return liveFeed.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.live_row,parent,false)
        return CustomViewHolder(cellForRow)
    }

    @SuppressLint("HardwareIds")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val headerView = holder.view.viewHeader

        when {
            position == 0 || liveFeed[position].league_id != liveFeed[position-1].league_id -> {
                val flagImage = holder.view.imageView_flag
                val countryText = holder.view.textView_country
                val leagueText = holder.view.textView_league

                if(countryFlag[liveFeed[position].country_name] != null) {
                    flagImage.setImageResource(countryFlag[liveFeed[position].country_name]!!)
                }

                if(countryNameENTR[liveFeed[position].country_name] == null) {
                    countryText.text = liveFeed[position].country_name + ":"
                } else {
                    countryText.text = countryNameENTR[liveFeed[position].country_name] + ":"
                }

                leagueText.text = liveFeed[position].league_name
                headerView.visibility = View.VISIBLE
            }
            else -> headerView.visibility = View.GONE
        }

        val homeText = holder.view.textView_home
        homeText.text = liveFeed[position].match_hometeam_name

        val awayText = holder.view.textView_away
        awayText.text = liveFeed[position].match_awayteam_name

        val timeText = holder.view.textView_time
        when {
            liveFeed[position].match_status == "HT" -> timeText.text = "Devre Arası"
            liveFeed[position].match_status == "FT" -> timeText.text = "Maç Sonu"
            liveFeed[position].match_status == "Canc." -> timeText.text = "İptal"
            liveFeed[position].match_status == "" -> timeText.text = liveFeed[position].match_time
            else -> timeText.text = liveFeed[position].match_status
        }

        val homeScoreText = holder.view.textView_home_score
        if (liveFeed[position].match_hometeam_score == "?" || liveFeed[position].match_hometeam_score == "") {
            homeScoreText.text = "-"
        }
        else {
            homeScoreText.text = liveFeed[position].match_hometeam_score
        }

        val awayScoreText = holder.view.textView_away_score
        if (liveFeed[position].match_awayteam_score == "?" || liveFeed[position].match_awayteam_score == "") {
            awayScoreText.text = "-"
        }
        else {
            awayScoreText.text = liveFeed[position].match_awayteam_score
        }

        if (liveFeed[position].match_hometeam_score.toIntOrNull() != null && liveFeed[position].match_awayteam_score.toIntOrNull() != null) {
            when {
                liveFeed[position].match_hometeam_score.toInt() > liveFeed[position].match_awayteam_score.toInt() -> {
                    homeText.typeface = Typeface.DEFAULT_BOLD
                    awayText.typeface = Typeface.DEFAULT
                }
                liveFeed[position].match_hometeam_score.toInt() < liveFeed[position].match_awayteam_score.toInt() -> {
                    homeText.typeface = Typeface.DEFAULT
                    awayText.typeface = Typeface.DEFAULT_BOLD
                }
                else -> {
                    homeText.typeface = Typeface.DEFAULT
                    awayText.typeface = Typeface.DEFAULT
                }
            }
        }

        if (liveFeed[position].match_live == "1" &&
                liveFeed[position].match_hometeam_score != "" &&
                liveFeed[position].match_hometeam_score != "?" &&
                liveFeed[position].match_awayteam_score != "" &&
                liveFeed[position].match_awayteam_score != "?" &&
                liveFeed[position].match_status != "Canc." &&
                liveFeed[position].match_status != "FT" &&
                liveFeed[position].match_status != "AAW" &&
                liveFeed[position].match_status != "Aband." &&
                liveFeed[position].match_status != "AET" &&
                liveFeed[position].match_status != "AP") {
            homeScoreText.setTextColor(Color.parseColor("#ffcc0000"))
            awayScoreText.setTextColor(Color.parseColor("#ffcc0000"))
            timeText.setTextColor(Color.parseColor("#ffcc0000"))
        }
        else {
            homeScoreText.setTextColor(Color.parseColor("#555555"))
            awayScoreText.setTextColor(Color.parseColor("#555555"))
            timeText.setTextColor(Color.parseColor("#555555"))
        }

        val favouriteRelative = holder.view.relativelayout_favourite
        val favouriteImage = holder.view.imageView_favourite

        if (favouriteMap[liveFeed[position].match_id] == null) {
            favouriteImage.tag = 0
            favouriteImage.setImageResource(R.drawable.star_empty)
        }
        else {
            favouriteImage.tag = 1
            favouriteImage.setImageResource(R.drawable.star)
        }

        favouriteRelative.setOnClickListener {
            // Write a message to the database

            val android_id = Settings.Secure.getString(holder.view.context.contentResolver,
                    Settings.Secure.ANDROID_ID)

            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("v1").child("favourite").child(android_id).child(liveFeed[position].match_id)

            if (favouriteImage.tag == 0) {
                favouriteImage.tag = 1
                favouriteImage.setImageResource(R.drawable.star)

                myRef.setValue(true)

                favouriteMap[liveFeed[position].match_id] = true
            }
            else {
                favouriteImage.tag = 0
                favouriteImage.setImageResource(R.drawable.star_empty)

                myRef.removeValue()

                favouriteMap[liveFeed[position].match_id] = null
            }

        }


        holder.view.setOnClickListener {
            val manager = (holder.view.context as AppCompatActivity).supportFragmentManager
            val transaction = manager.beginTransaction()
            var fragment: Fragment = MatchDetail(liveFeed[position])
            transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            transaction.replace(R.id.content_main, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }
}