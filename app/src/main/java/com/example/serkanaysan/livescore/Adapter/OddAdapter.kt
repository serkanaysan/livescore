package com.example.serkanaysan.livescore.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.serkanaysan.livescore.Odd
import com.example.serkanaysan.livescore.R
import kotlinx.android.synthetic.main.odd_row.view.*


class OddAdapter(val oddFeed: ArrayList<Odd>): RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return oddFeed.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.odd_row,parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {




        val oddbookmakersText = holder.view.textView_oddbookmakers
        oddbookmakersText.text = oddFeed[position].odd_bookmakers

        val odd1Text = holder.view.textView_odd1
        odd1Text.text = oddFeed[position].odd_1

        val oddxText = holder.view.textView_oddx
        oddxText.text = oddFeed[position].odd_x

        val odd2Text = holder.view.textView_odd2
        odd2Text.text = oddFeed[position].odd_2







    }
}