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
import com.google.firebase.database.FirebaseDatabase
import android.view.View
import com.example.serkanaysan.livescore.Helper.countryFlag
import com.example.serkanaysan.livescore.Helper.countryNameENTR
import com.example.serkanaysan.livescore.Helper.favouriteMap
import kotlinx.android.synthetic.main.h2h_row.view.*


class H2HAdapter(val liveFeed: ArrayList<Match>, val teamName: String): RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return liveFeed.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.h2h_row,parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val homeText = holder.view.textView_home
        homeText.text = liveFeed[position].match_hometeam_name

        val awayText = holder.view.textView_away
        awayText.text = liveFeed[position].match_awayteam_name

        /*val timeText = holder.view.textView_date
        when {
            liveFeed[position].match_status == "HT" -> timeText.text = "Devre Arası"
            liveFeed[position].match_status == "FT" -> timeText.text = "Maç Sonu"
            liveFeed[position].match_status == "Canc." -> timeText.text = "İptal"
            liveFeed[position].match_status == "" -> timeText.text = liveFeed[position].match_time
            else -> timeText.text = liveFeed[position].match_status
        }*/

        val scoreText = holder.view.textView_score
        if (liveFeed[position].match_hometeam_score == "?" || liveFeed[position].match_awayteam_score == "?") {
            scoreText.text = ""
        }
        else {
            scoreText.text = liveFeed[position].match_hometeam_score + " - " + liveFeed[position].match_awayteam_score
        }

        val statusText = holder.view.textView_status

        if (liveFeed[position].match_hometeam_score.toIntOrNull() != null && liveFeed[position].match_awayteam_score.toIntOrNull() != null) {
            when {
                liveFeed[position].match_hometeam_score.toInt() > liveFeed[position].match_awayteam_score.toInt() -> {
                    homeText.typeface = Typeface.DEFAULT_BOLD
                    awayText.typeface = Typeface.DEFAULT
                    awayText.text = " - " + awayText.text
                }
                liveFeed[position].match_hometeam_score.toInt() < liveFeed[position].match_awayteam_score.toInt() -> {
                    homeText.typeface = Typeface.DEFAULT
                    awayText.typeface = Typeface.DEFAULT_BOLD
                    homeText.text = "" + homeText.text + " - "
                }
                else -> {
                    homeText.typeface = Typeface.DEFAULT
                    awayText.typeface = Typeface.DEFAULT
                    homeText.text = "" + homeText.text + " - "
                }
            }

            if (teamName == "") {
                statusText.visibility = View.GONE
            }
            else {
                statusText.visibility = View.VISIBLE

                if (liveFeed[position].match_hometeam_name == teamName) {
                    when {
                        liveFeed[position].match_hometeam_score.toInt() > liveFeed[position].match_awayteam_score.toInt() -> {
                            statusText.text = "G"
                            statusText.setBackgroundResource(R.drawable.green_round)

                        }
                        liveFeed[position].match_hometeam_score.toInt() < liveFeed[position].match_awayteam_score.toInt() -> {
                            statusText.text = "M"
                            statusText.setBackgroundResource(R.drawable.red_round)
                        }
                        else -> {
                            statusText.text = "B"
                            statusText.setBackgroundResource(R.drawable.orange_round)
                        }
                    }
                }
                else {
                    when {
                        liveFeed[position].match_hometeam_score.toInt() > liveFeed[position].match_awayteam_score.toInt() -> {
                            statusText.text = "M"
                            statusText.setBackgroundResource(R.drawable.red_round)
                        }
                        liveFeed[position].match_hometeam_score.toInt() < liveFeed[position].match_awayteam_score.toInt() -> {
                            statusText.text = "G"
                            statusText.setBackgroundResource(R.drawable.green_round)
                        }
                        else -> {
                            statusText.text = "B"
                            statusText.setBackgroundResource(R.drawable.orange_round)
                        }
                    }
                }
            }

            val dateText = holder.view.textView_date
            val day = liveFeed[position].match_date.substring(8).padStart(2,'0')
            val month = liveFeed[position].match_date.substring(5, 7).padStart(2,'0')
            dateText.text = "$day.$month"
        }

        /*holder.view.setOnClickListener {
            val manager = (holder.view.context as AppCompatActivity).supportFragmentManager
            val transaction = manager.beginTransaction()
            var fragment: Fragment = MatchDetail(liveFeed[position])
            transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            transaction.replace(R.id.content_main, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }*/

    }
}