package com.example.serkanaysan.livescore.Helper

import android.annotation.SuppressLint
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.RelativeLayout
import com.example.serkanaysan.livescore.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherAdView
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics
import java.text.SimpleDateFormat
import java.util.*

val countryNameENTR = hashMapOf(
        "Champions League" to "Şampiyonlar Ligi",
        "Europa League" to "Avrupa Ligi",
        "Copa America" to "Amerika Kupası",
        "Cup of Nations" to "Uluslar Kupası",
        "International" to "Uluslararası",
        "England" to "İngiltere",
        "Italy" to "İtalya",
        "Spain" to "İspanya",
        "Germany" to "Almanya",
        "France" to "Fransa",
        "Netherlands" to "Hollanda",
        "Belgium" to "Belçika",
        "Portugal" to "Portekiz",
        "Scotland" to "İskoçya",
        "World Cup U-20" to "Dünya Kupası U-20",
        "World Cup U-17" to "Dünya Kupası U-17",
        "Euro U-21" to "Avrupa U-21",
        "Euro U-19" to "Avrupa U-19",
        "Euro U-17" to "Avrupa U-17",
        "World Cup Women" to "Dünya Kupası Kadınlar",
        "Euro Women" to "Avrupa Kadınlar",
        "Austria" to "Avusturya",
        "Cyprus" to "Kıbrıs",
        "Denmark" to "Danimarka",
        "Finland" to "Finlandiya",
        "Greece" to "Yunanistan",
        "Iceland" to "İzlanda",
        "Ireland" to "İrlanda",
        "Luxembourg" to "Lüksemburg",
        "Norway" to "Norveç",
        "Northern Ireland" to "Kuzey İrlanda",
        "Sweden" to "İsveç",
        "Switzerland" to "İsviçre",
        "Turkey" to "Türkiye",
        "Wales" to "Galler",
        "Belarus" to "Beyaz Rusya",
        "Bosnia & Herz." to "Bosna Hersek",
        "Bulgaria" to "Bulgaristan",
        "Croatia" to "Hırvatistan",
        "Czech Republic" to "Çek Cumhuriyeti",
        "Estonia" to "Estonya",
        "Hungary" to "Macaristan",
        "Israel" to "İsrail",
        "Latvia" to "Letonya",
        "Lithuania" to "Litvanya",
        "FYR Macedonia" to "Makedonya",
        "Moldova" to "Moldova",
        "Montenegro" to "Karadağ",
        "Poland" to "Polonya",
        "Romania" to "Romanya",
        "Russia" to "Rusya",
        "Serbia" to "Sırbistan",
        "Slovakia" to "Slovakya",
        "Slovenia" to "Slovenya",
        "Ukraine" to "Ukrayna",
        "South America" to "Güney Amerika",
        "Argentina" to "Arjantin",
        "Bolivia" to "Bolivya",
        "Brazil" to "Brezilya",
        "Chile" to "Şili",
        "Colombia" to "Kolombiya",
        "Ecuador" to "Ekvator",
        "Paraguay" to "Paraguay",
        "Uruguay" to "Uruguay",
        "Venezuela" to "Venezuela",
        "Mexico" to "Meksika",
        "USA" to "USA",
        "Costa Rica" to "Kosta Rika",
        "El Salvador" to "El Salvador",
        "Guatemala" to "Guatemala",
        "Honduras" to "Honduras",
        "China" to "Çin",
        "India" to "Hindistan",
        "Japan" to "Japonya",
        "Republic of Korea" to "Kore Cumhuriyeti",
        "Singapore" to "Singapur",
        "Thailand" to "Tayland",
        "Armenia" to "Ermenistan",
        "Azerbaijan" to "Azerbeycan",
        "Georgia" to "Gürcistan",
        "Kazakhstan" to "Kazakistan",
        "Iran" to "İran",
        "Australia" to "Avusturalya",
        "New Zealand" to "Yeni Zelanda",
        "Algeria" to "Cezayir",
        "Egypt" to "Mısır",
        "Morocco" to "Fas",
        "South Africa" to "Güney Afrika",
        "Tunisia" to "Tunus",
        "Albania" to "Arnavutluk",
        "Faroe Islands" to "Faroe Adaları",
        "Malta" to "Malta",
        "Canada" to "Kanada",
        "Jamaica" to "Jamaika",
        "Hong Kong" to "Hong Kong",
        "Jordan" to "Ürdün",
        "Malaysia" to "Malezya",
        "Qatar" to "Katar",
        "Saudi Arabia" to "Suudi Arabistan",
        "U.A.E" to "U.A.E",
        "Uzbekistan" to "Özbekistan",
        "Uganda" to "Uganda",
        "World Cup" to "Dünya Kupası"
)

val countryFlag = hashMapOf(
        "England" to R.drawable.england,
        "Italy" to R.drawable.italy,
        "Spain" to R.drawable.spain,
        "France" to R.drawable.france,
        "Germany" to R.drawable.germany,
        "Netherlands" to R.drawable.netherland,
        "Portugal" to R.drawable.portugal,
        "Belgium" to R.drawable.belgium,
        "Turkey" to R.drawable.turkey,
        "Brazil" to R.drawable.brazil,
        "Estonia" to R.drawable.estonya,
        "Finland" to R.drawable.finlandiya,
        "Iceland" to R.drawable.izlanda,
        "Lithuania" to R.drawable.litvanya,
        "Malaysia" to R.drawable.malezya,
        "Russia" to R.drawable.rusya,
        "Sweden" to R.drawable.isvec,
        "USA" to R.drawable.usa,
        "Georgia" to R.drawable.gurcistan,
        "Romania" to R.drawable.romanya,
        "Uruguay" to R.drawable.uruguay,
        "Denmark" to R.drawable.danimarka,
        "Bulgaria" to R.drawable.bulgaristan,
        "Guatemala" to R.drawable.guatemala,
        "South America" to R.drawable.world,
        "International" to R.drawable.world,
        "Austria" to R.drawable.avusturya,
        "Czech Republic" to R.drawable.cz,
        "Australia" to R.drawable.au,
        "Ireland" to R.drawable.ie,
        "Hungary" to R.drawable.hu,
        "Paraguay" to R.drawable.py,
        "Uzbekistan" to R.drawable.uz,
        "Luxembourg" to R.drawable.lu,
        "Israel" to R.drawable.ie,
        "Greece" to R.drawable.gr,
        "Uganda" to R.drawable.ug,
        "Armenia" to R.drawable.am,
        "Belarus" to R.drawable.by,
        "Bosnia & Herz." to R.drawable.ba,
        "Champions League" to R.drawable.world,
        "Chile" to R.drawable.cl,
        "China" to R.drawable.cn,
        "Ecuador" to R.drawable.ec,
        "Japan" to R.drawable.jp,
        "Latvia" to R.drawable.lv,
        "Montenegro" to R.drawable.me,
        "Norway" to R.drawable.no,
        "Poland" to R.drawable.pl,
        "Republic of Korea" to R.drawable.kr,
        "Singapore" to R.drawable.sg,
        "Slovakia" to R.drawable.sk,
        "Switzerland" to R.drawable.ch,
        "Thailand" to R.drawable.th,
        "Venezuela" to R.drawable.ve,
        "Argentina" to R.drawable.ar,
        "Canada" to R.drawable.ca,
        "Slovenia" to R.drawable.si,
        "South Africa" to R.drawable.za,
        "Bolivia" to R.drawable.bo,
        "Serbia" to R.drawable.rs,
        "Hong Kong" to R.drawable.hk,
        "Wales" to R.drawable.galler,
        "Scotland" to R.drawable.scotland,
        "Faroe Islands" to R.drawable.fo,
        "Kazakhstan" to R.drawable.kz,
        "Moldova" to R.drawable.md,
        "Albania" to R.drawable.al,
        "Ukraine" to R.drawable.ua,
        "Costa Rica" to R.drawable.cr,
        "Tunisia" to R.drawable.tn,
        "Azerbaijan" to R.drawable.az





)

lateinit var mPublisherInterstitialAd: PublisherInterstitialAd
var mFirebaseAnalytics: FirebaseAnalytics? = null

var favouriteMap = HashMap<String, Boolean?>()
var settingsMap = HashMap<String, String>()

val c = Calendar.getInstance()!!
@SuppressLint("SimpleDateFormat")
val sdf = SimpleDateFormat("yyyy-MM-dd")
var strDate = sdf.format(c.time)!!
var strToday = sdf.format(c.time)!!

fun displayAds(adLayout: RelativeLayout, bannerID: String?, width: Int, height: Int){

    if (bannerID.isNullOrEmpty()) {
        adLayout.visibility = View.GONE
    }
    else {
        val adView = PublisherAdView(adLayout.context)

        adView?.destroy()

        adView.setAdSizes(com.google.android.gms.ads.AdSize(width, height))

        adView.adUnitId = bannerID
        val adRequest = PublisherAdRequest.Builder().build()
        adView.loadAd(adRequest)
        adLayout.removeAllViews()
        adLayout.addView(adView)

        adView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                adLayout.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                // Code to be executed when an ad request fails.
                adLayout.visibility = View.GONE
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }
}