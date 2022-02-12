package shahin.shahin.brangameapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.ads.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.AutoInterstitialPreferences;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class Opening_Activity extends AppCompatActivity {
   private Button Easy,mediam,hard;
    private AdView fb_Banner;
    private RewardedVideoAd rewardedVideoAds;
    private InterstitialAd interstitialAd;
   private String AdmobeAdsShowValue = "";
   private Banner startIo_banner_Opning;
   private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
   private com.google.android.gms.ads.AdView admobBannerAds;
    private com.facebook.ads.AdView facebookadView; ////banner ads
    ////=========================================Database
   private DatabaseReference dbref;
   private String adsremote;
   LinearLayout facenookLayout;


   private com.startapp.sdk.ads.interstitials.InterstitialAd StartIoIndrail;

   private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        AudienceNetworkAds.initialize(this);
        StartAppSDK.init(this, getString(R.string.startAppid), true);
        StartAppAd.disableSplash();
        Easy=findViewById(R.id.button);
        hard=findViewById(R.id.button3);
        admobBannerAds=findViewById(R.id.admovemadview);
        facenookLayout=findViewById(R.id.fbbanner_container_Opening);
        startIo_banner_Opning= findViewById(R.id.startio_banner_main);





        dbref = FirebaseDatabase.getInstance().getReference("AdsControlle");
        dbref.child("Admob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();

                    /////Callind Data Base And Take Data And get class
                    if (AdmobeAdsShowValue.equals("ad"))
                    {
                        adsCloseClass();
                        startIo_banner_Opning.setVisibility(View.GONE);
                        admobBannerAds.setVisibility(View.VISIBLE);
                        facenookLayout.setVisibility(View.GONE);
                        loadBannerAd();
                        loadFullscreenAd();

                    }
                    if (AdmobeAdsShowValue.equals("io"))

                    {

                        adsCloseClass();
                        admobBannerAds.setVisibility(View.GONE);
                        facenookLayout.setVisibility(View.GONE);

                        startIo_banner_Opning.setVisibility(View.VISIBLE);


                    }
                    if (AdmobeAdsShowValue.equals("fb"))
                    {
                        adsCloseClass();
                        startIo_banner_Opning.setVisibility(View.GONE);
                        admobBannerAds.setVisibility(View.GONE);
                        facenookLayout.setVisibility(View.VISIBLE);
                       loadFacebookbanner();
                       LoadFacebookInterstaal();

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        mediam=findViewById(R.id.button2);
        Easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbref.child("Admob").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();
                            adsremote = AdmobeAdsShowValue.toString();


                            /////Callind Data Base And Take Data And get class
                            if (AdmobeAdsShowValue.equals("ad"))
                            {

                                adsCloseClass();

                                showInterstitial();



                            }
                            if (AdmobeAdsShowValue.equals("io"))
                            {

                              ////No Ads Here



                            }
                            if (AdmobeAdsShowValue.equals("fb"))
                            {

                                adsCloseClass();
                               showFacebookfullscreenads();

                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                Intent intent=new Intent(Opening_Activity.this,MainActivity.class);
                intent.putExtra("easy","easy");
                startActivity(intent);



            }
        });

        mediam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbref.child("Admob").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();
                            adsremote = AdmobeAdsShowValue.toString();


                            /////Callind Data Base And Take Data And get class
                            if (AdmobeAdsShowValue.equals("ad"))
                            {


                                adsCloseClass();
                                showInterstitial();


                            }
                            if (AdmobeAdsShowValue.equals("io"))
                            {




                                adsCloseClass();


                            }
                            if (AdmobeAdsShowValue.equals("fb"))
                            {
                                adsCloseClass();
                                showFacebookfullscreenads();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                Intent intent=new Intent(Opening_Activity.this,MainActivity.class);
                intent.putExtra("easy","mediam");
                startActivity(intent);
                finish();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               dbref.child("Admob").addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                           AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();
                           adsremote = AdmobeAdsShowValue.toString();


                           /////Callind Data Base And Take Data And get class
                           if (AdmobeAdsShowValue.equals("ad"))
                           {


                               adsCloseClass();
                               showInterstitial();


                           }
                           if (AdmobeAdsShowValue.equals("io"))
                           {






                           }
                           if (AdmobeAdsShowValue.equals("fb"))
                           {
                               adsCloseClass();
                               showFacebookfullscreenads();
                           }


                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                   }
               });

               Intent intent=new Intent(Opening_Activity.this,MainActivity.class);
               intent.putExtra("easy","hard");
               startActivity(intent);
           }
       });
    }
    private void showInterstitial() {
        // Show the ad if it's ready.


        adsCloseClass();
        if (mInterstitialAd != null ) {
            mInterstitialAd.show(this);

        } else {

        }
    }


   ///  loadFullscreenAd method starts here.....
    private void loadFullscreenAd(){

        //Requesting for a fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(this, String.valueOf(R.string.AdmoveInterstatil), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;


                //Fullscreen callback || Requesting again when an ad is shown already
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
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
                mInterstitialAd = null;
            }

        });

    }
    // loadFullscreenAd method ENDS  here..... >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    
    
    
    
    
    
    
    ///=============================

    private void LoadFacebookInterstaal() {


        interstitialAd = new InterstitialAd(this,getString(R.string.fbfullscreenAds));
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e("TAG", "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e("ta", "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e("trag", "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {


            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("tag", "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("TAG", "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    private void showFacebookfullscreenads(){

        if (interstitialAd!=null&&interstitialAd.isAdLoaded()&& !interstitialAd.isAdInvalidated()){
            interstitialAd.show();
            Log.d("", "Interstitial ad is loaded and ready to be displayed!");


        }else {

        }
    }
    



  ////////============================================================== Banner Ads Class

    private  int BANNER_AD_CLICK_COUNT=0;
    ///admove
    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        admobBannerAds.loadAd(adRequest);
        admobBannerAds.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (BANNER_AD_CLICK_COUNT >= 3) {
                    if (admobBannerAds != null) admobBannerAds.setVisibility(View.GONE);
                } else {
                    if (admobBannerAds != null) admobBannerAds.setVisibility(View.VISIBLE);
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

                if (BANNER_AD_CLICK_COUNT >= 3) {
                    if (admobBannerAds != null) admobBannerAds.setVisibility(View.GONE);
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
    ////facebook
    private void loadFacebookbanner() {
        facebookadView = new com.facebook.ads.AdView(Opening_Activity.this, getString(R.string.fbbanenr), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.fbbanner_container_Opening);
        adContainer.addView(facebookadView);
        facebookadView.loadAd();


    }
    ///=======================================================================banner Ads Classs end


    private  void adsCloseClass(){

        if (admobBannerAds!=null){
            admobBannerAds.destroy();
        }
        if (fb_Banner!=null){
            fb_Banner.destroy();
        }

    }
}