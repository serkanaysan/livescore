package com.example.serkanaysan.livescore.FirebaseService

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by serkan on 18.02.2018.
 */

class MyFirebaseIdService: FirebaseInstanceIdService(){
    override fun onTokenRefresh() {
        super.onTokenRefresh()

        val refreshedToken = FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(refreshedToken!!)
    }

    private fun sendRegistrationToServer(refreshedToken: String){
        Log.d("DEBUG", refreshedToken)
    }
}