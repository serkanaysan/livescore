package com.example.serkanaysan.livescore

import android.app.DatePickerDialog
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.serkanaysan.livescore.FirebaseService.Config
import com.example.serkanaysan.livescore.Fragment.Game
import com.example.serkanaysan.livescore.Fragment.Intro
import com.example.serkanaysan.livescore.Fragment.Live
import kotlinx.android.synthetic.main.activity_main.*
import com.example.serkanaysan.livescore.Fragment.Main
import com.example.serkanaysan.livescore.Helper.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item ->
        var fragment: Fragment?
        when (item.itemId) {
            R.id.tummaclar -> {
                fragment = Main()
                changeFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.canli -> {
                fragment = Live()
                changeFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.oyunlarim -> {
                fragment = Game()
                changeFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private  fun changeFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        var fragment = fragment

        transaction.replace(R.id.content_main, fragment)
        //transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FacebookSdk()
        AppEventsLogger.activateApp(application)
        //val logger = AppEventsLogger.newLogger(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //navigation.itemIconTintList = null

        textView_calendar.text = strDate.substring(8)
        relativeLayout_calendar.setOnClickListener {
            showDate()
        }

        var tb = this.findViewById<AppBarLayout>(R.id.appBarLayout)
        var navigation = this.findViewById<BottomNavigationView>(R.id.navigation)

        if (tb != null) {
            tb.visibility = View.GONE
            navigation.visibility = View.GONE

            if (Build.VERSION.SDK_INT >= 16) {
                this.window.setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT)
                this.window.decorView.systemUiVisibility = 3328
            }else{
                this.requestWindowFeature(Window.FEATURE_NO_TITLE)
                this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        var fragment:Fragment = Intro()

        transaction.replace(R.id.content_main, fragment)
        transaction.commit()

        setupFirebase()
    }

    private fun setupFirebase() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        mRegistrationBroadcastReceiver = object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent!!.action == Config.STR_PUSH){
                    val message = intent.getStringExtra("message")
                    showNotification("LiveScore", message)
                }
            }
        }

        val android_id = Settings.Secure.getString(contentResolver,
                Settings.Secure.ANDROID_ID)

        val database = FirebaseDatabase.getInstance()
        val favouriteRef = database.reference.child("v1").child("favourite").child(android_id)

        // Read from the database
        favouriteRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.children?.forEach {
                    favouriteMap[it.key] = it.value as Boolean
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                println(error.toException())
            }
        })

        val settingsRef = database.reference.child("v1").child("settings")

        settingsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.children?.forEach {
                    settingsMap[it.key] = it.value.toString()
                }

                setupInterstitial()
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.toException())
            }
        })
    }

    private fun setupInterstitial() {
        if (!settingsMap["INTERSTITIAL_ID"].isNullOrEmpty()) {
            mPublisherInterstitialAd = PublisherInterstitialAd(this)
            mPublisherInterstitialAd.adUnitId = settingsMap["INTERSTITIAL_ID"]
            mPublisherInterstitialAd.loadAd(PublisherAdRequest.Builder().build())

            mPublisherInterstitialAd.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    // Code to be executed when an ad request fails.
                }

                override fun onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                override fun onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                    mPublisherInterstitialAd.loadAd(PublisherAdRequest.Builder().build())
                }
            }
        }
    }

    private fun showNotification(title: String, message: String?){
        val intent = Intent(applicationContext, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(applicationContext)
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.stadium)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)

        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

    private fun showDate(){
        val day = strDate.substring(8).toInt()
        val month = strDate.substring(5, 7).toInt() - 1
        val year = strDate.substring(0, 4).toInt()

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            var selectedDate = year.toString() + "-" + (monthOfYear + 1).toString().padStart(2, '0') + "-" + dayOfMonth.toString().padStart(2, '0')
            if (strDate != selectedDate) {
                strDate = selectedDate
                textView_calendar.text = dayOfMonth.toString()
                changeFragment(Main())
            }

        }, year, month, day)

        dpd.show()
    }
}
