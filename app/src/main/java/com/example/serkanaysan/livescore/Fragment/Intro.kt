package com.example.serkanaysan.livescore.Fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.example.serkanaysan.livescore.R

class Intro: Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        var background = object : Thread(){
            override fun run() {
                try {
                    Thread.sleep(3000)

                    val manager = (context as AppCompatActivity).supportFragmentManager
                    val transaction = manager.beginTransaction()
                    var fragment:Fragment = Main()

                    transaction.replace(R.id.content_main, fragment)
                    transaction.commit()

                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        background.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater!!.inflate(R.layout.fragment_intro, container, false)
    }



}