package com.naxtre.anand.interstitialadsandroid;

import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params=new Bundle();
        params.putString("name","Main Activity");
        params.putString("full_text","Main Activity Opened");
        mFirebaseAnalytics.logEvent("Activity Main Screen",params);
        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        mInterstitialAd=new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

            }
        });
        requestNewInterstitial();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        },3000);

        try{
        raiseException();}
        catch (StringIndexOutOfBoundsException s){
            FirebaseCrash.report(s);
        }

    }
    private void requestNewInterstitial() {
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("Device ID",": "+android_id);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(android_id)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    void raiseException() throws StringIndexOutOfBoundsException{
        throw new StringIndexOutOfBoundsException();
    }
}
