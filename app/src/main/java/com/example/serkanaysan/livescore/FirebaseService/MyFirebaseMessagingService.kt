package com.example.serkanaysan.livescore.FirebaseService

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by serkan on 18.02.2018.
 */

class MyFirebaseMessagingService: FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage != null && remoteMessage.data.containsKey("source")) {
            /** just return or do nothing **/
            return
        }
        handleNotification(remoteMessage!!.notification!!.body)
    }

    private fun handleNotification(body: String?){
        val pushNotification = Intent(Config.STR_PUSH)
        pushNotification.putExtra("message", body)
        LocalBroadcastManager.getInstance(this)

    }
}