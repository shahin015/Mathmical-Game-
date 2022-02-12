package shahin.shahin.brangameapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;
import com.startapp.sdk.adsbase.model.AdPreferences;


public class MainActivity2 extends AppCompatActivity {
    TextView textView,textView2;
    Button button;
    String dataformintent;
    RewardedAd mRewardedAd2;
    private String AdmobeAdsShowValue = "";
    private com.facebook.ads.AdView facebookadView; ////banner ads
    ////=========================================Database
    private DatabaseReference dbref;
    AdView mAdView;
    // Reward Video Ad Loading method starts here.....
    int BANNER_AD_CLICK_COUNT =0;



    // loadFullscreenAd method starts here..... admobe
    public void loadFullscreenAd() {
        //Requesting for a fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.AdmoveInterstatil), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                googleinterstitialAd = interstitialAd;
                //Fullscreen callback || Requesting again when an ad is shown already
                googleinterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        //User dismissed the previous ad. So we are requesting a new ad here
                        loadFullscreenAd();
                    }
                }); // FullScreen Callback Ends here

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
               googleinterstitialAd = null;
            }
        });

    }

    // loadFullscreenAd method ENDS  here..... >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //==============================================admove
    private void showgoogleInterstitial() {
        // Show the ad if it's ready.
        if (googleinterstitialAd != null) {
           googleinterstitialAd.show(this);
        } else {
            ///  Toast.makeText(getApplicationContext(), "Ads Not redy", Toast.LENGTH_SHORT).show();
        }
    }

    //============================================
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private void loadBannerAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (BANNER_AD_CLICK_COUNT >=3){
                    if(mAdView!=null) mAdView.setVisibility(View.GONE);
                }else{
                    if(mAdView!=null) mAdView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                BANNER_AD_CLICK_COUNT++;

                if (BANNER_AD_CLICK_COUNT >=3){
                    if(mAdView!=null) mAdView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                loadBannerAd();
            }
        });

    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    ///==============================================================================
    private void loadRewardedAd() {





        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, String.valueOf(R.string.Reword_ads),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd2 = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd2 = rewardedAd;

                        //Handle callbacks
                        mRewardedAd2.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                mRewardedAd2 = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Don't forget to set the ad reference to null so you
                                // don't show the ad a second time.textView.setText("Ad was dismissed.");
                                // requesting a new ad here...

                                Toast.makeText(MainActivity2.this, "Now You Can Play agine", Toast.LENGTH_SHORT).show();                         }
                        });


                    }

                });

    }
    ////=============================================================================
    ///===============================================================================
    private void showadmobRewardAd() {
        if (mRewardedAd2 != null) {
            mRewardedAd2.show(MainActivity2.this, new OnUserEarnedRewardListener()
                       {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                }
            });
        } else {

        }

    }

    ////==============================================================================
    InterstitialAd googleinterstitialAd;
    com.startapp.sdk.ads.interstitials.InterstitialAd StartiointerstitialAd;

    CountDownTimer countDownTimer;
    LinearLayout facebookbannerAdcontinar_ac2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, getResources().getString(R.string.startAppid), true);
        setContentView(R.layout.activity_main2);
        textView=findViewById(R.id.textviewsssss);
        textView2=findViewById(R.id.text2);
        dataformintent=getIntent().getStringExtra("t");
        button=findViewById(R.id.agine);


        facebookbannerAdcontinar_ac2=findViewById(R.id.banner_container_maiActitivity2);

        button.setEnabled(false);
        mAdView=findViewById(R.id.madview);
        setCountDownTimer();
        textView.setText(dataformintent);
        String tx=textView.getText().toString();

        dbref = FirebaseDatabase.getInstance().getReference("AdsControlle");
        dbref.child("Admob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();

                    if (AdmobeAdsShowValue.equals("ad"))
                    {


                        closeAds_mainactiti2();
                        facebookbannerAdcontinar_ac2.setVisibility(View.GONE);
                        mAdView.setVisibility(View.VISIBLE);

                        loadBannerAd();
                        loadRewardedAd();
                        loadFullscreenAd();
                    }
                    if (AdmobeAdsShowValue.equals("io")){
                        closeAds_mainactiti2();
                        mAdView.setVisibility(View.GONE);
                        facebookbannerAdcontinar_ac2.setVisibility(View.VISIBLE);

                        loadFacebookbanner();

                    }
                    if (AdmobeAdsShowValue.equals("fb"))
                    {
                        closeAds_mainactiti2();
                        mAdView.setVisibility(View.GONE);
                        facebookbannerAdcontinar_ac2.setVisibility(View.VISIBLE);
                        loadFacebookbanner();
                        LoadFacebookInterstaal();
                                            }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref.child("Admob").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();

                            if (AdmobeAdsShowValue.equals("ad"))
                            {

                                closeAds_mainactiti2();
                               showgoogleInterstitial();

                                //  showRewardAd();


                            }
                            if (AdmobeAdsShowValue.equals("io"))
                            {


                                closeAds_mainactiti2();


                                StartAppAd.showAd(getApplicationContext());
                            }
                            if (AdmobeAdsShowValue.equals("fb"))
                            {



                                 showFacebookfullscreenads();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


               closeAds_mainactiti2();
                Intent intent=new Intent(MainActivity2.this,Opening_Activity.class);
                startActivity(intent);
            }
        });



    }

    private void StartIOads() {
        final StartAppAd videosAdd=new StartAppAd(getApplicationContext());
        videosAdd.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {



            }
        });
        videosAdd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                videosAdd.showAd();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
            }
        });
    }



    private void  setCountDownTimer(){


        countDownTimer=new CountDownTimer(8000,1000) {


            @Override
            public void onTick(long l) {





                button.setText(String.valueOf(l/1000)+"s"+" :Please Wate For next Round");


            }

            @Override
            public void onFinish() {
                dbref.child("Admob").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();

                            if (AdmobeAdsShowValue.equals("ad"))
                            {

                                closeAds_mainactiti2();
                                showadmobRewardAd();


                                //  showRewardAd();


                            }
                            if (AdmobeAdsShowValue.equals("io"))
                            {


                                closeAds_mainactiti2();
                                StartIOads();
                            }
                            if (AdmobeAdsShowValue.equals("fb"))
                            {


                                closeAds_mainactiti2();

                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });




            button.setText("Play Agine");

                button.setEnabled(true);
            }
        }.start();
    }




    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MainActivity2.this,Opening_Activity.class);
        startActivity(intent);

        dbref.child("Admob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();

                    if (AdmobeAdsShowValue.equals("ad"))
                    {

                        closeAds_mainactiti2();

                   //  showRewardAd();


                    }
                    if (AdmobeAdsShowValue.equals("io"))
                    {

                        closeAds_mainactiti2();


                        StartIOads();
                    }
                    if (AdmobeAdsShowValue.equals("fb"))
                    {

                        closeAds_mainactiti2();

                     ////   LoadFacebookInterstaal();





                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //super.onBackPressed();
    }

    com.facebook.ads.InterstitialAd interstitialAds;
    private void LoadFacebookInterstaal() {




        interstitialAds = new com.facebook.ads.InterstitialAd(this, getString(R.string.fbfullscreenAds));
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
                // Interstitial ad displayed callback
                Log.e("TAG", "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                // Interstitial dismissed callback
                Log.e("ta", "Interstitial ad dismissed.");
            }

            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                // Ad error callback
                Log.e("trag", "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("", "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad


            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
                // Ad clicked callback
                Log.d("tag", "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
                // Ad impression logged callback
                Log.d("TAG", "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAds.loadAd(
                interstitialAds.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }
    private void showFacebookfullscreenads(){
        closeAds_mainactiti2();

        if (interstitialAds!=null&&interstitialAds.isAdLoaded()&& !interstitialAds.isAdInvalidated()){
            interstitialAds.show();
            Log.d("", "Interstitial ad is loaded and ready to be displayed!");


        }else {

        }
    }


    private void loadFacebookbanner() {
        facebookadView = new com.facebook.ads.AdView(MainActivity2.this, getString(R.string.fbbanenr), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container_maiActitivity2);
        adContainer.addView(facebookadView);
        facebookadView.loadAd();


    }

    private void closeAds_mainactiti2() {

        if (facebookadView != null) {
            facebookadView.destroy();
        }

        if (mAdView!=null){
            mAdView.destroy();
        }





    }



}