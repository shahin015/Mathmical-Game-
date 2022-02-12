package shahin.shahin.brangameapp;

import androidx.annotation.NonNull;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.util.Log;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;

import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.OnUserEarnedRewardListener;

import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;


import java.util.ArrayList;
import java.util.Random;
import soup.neumorphism.NeumorphButton;

public class MainActivity extends AppCompatActivity {


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>admob Banner Ads
    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (BANNER_AD_CLICK_COUNT >= 3) {
                    if (mAdView != null) mAdView.setVisibility(View.GONE);
                } else {
                    if (mAdView != null) mAdView.setVisibility(View.VISIBLE);
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
                    if (mAdView != null) mAdView.setVisibility(View.GONE);
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
    ///===============================================================
    // loadFullscreenAd method starts here..... admobe
    public void loadFullscreenAd() {
        //Requesting for a fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                //Fullscreen callback || Requesting again when an ad is shown already
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
    //==============================================admove
    private void showInterstitial() {
        // Show the ad if it's ready.
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            ///  Toast.makeText(getApplicationContext(), "Ads Not redy", Toast.LENGTH_SHORT).show();
        }
    }

    //============================================
    // Reward Video Ad Loading method starts here.....admove
    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.Admovideoreword),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;

                        //Handle callbacks
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                mRewardedAd = null;
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
                                ADMOVEREWORDADCLICKCOUNT++;

                                loadRewardedAd();
                            }
                        });


                    }

                });

    }

    ////===========admove
    private void showRewardAd() {
        if (mRewardedAd != null) {

            mRewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                }
            });
        } else {

            Toast.makeText(getApplicationContext(), "Please Connect The Internet For parfect Working Condition ", Toast.LENGTH_SHORT).show();

        }
    }

    ////====for Admob Ads
    RewardedAd mRewardedAd;
    InterstitialAd mInterstitialAd;
    String AdmobeAdsShowValue = "";
    AdView mAdView;
    int BANNER_AD_CLICK_COUNT = 0;
    ////=========for Facebook Ads
    com.facebook.ads.InterstitialAd fbinterstitialAd;  ////Interstatialad
    private com.facebook.ads.AdView facebookadView; ////banner ads
    private com.facebook.ads.InterstitialAd interstitialAds;
    private InterstitialAd interstitialAd;
    private boolean isLoaded;
    ////=====for Start io ads
    Banner startIo_banner;
    ////=========================================Database
    DatabaseReference dbref;
    String adsremote;


    // Game ============================================================
    Button button0, button1, button2, button3;
    TextView sumtextView, scertextview, timerTextView, answer;
    NeumorphButton playWithTime,seeYourResut;
    ArrayList<Integer> answers = new ArrayList<Integer>();
    LottieAnimationView animationView, rightanim, waronganim;
    MediaPlayer mediaPlayer, mediaPlayer2;
    EditText inputtime;
    LinearLayout fbBannerContinar;
    int scrore = 0;
    int countTIme=1;
    int nmberofQustions = 0;
    int totalqustion;
    int helpanswer;
    int lacatiocreccetanswers;
    Random random;
    String IntentCount;
    String answer1;
    String HElPans;
    String vai = "b";
    String GameMode;
    CountDownTimer countDownTimer;
    FloatingActionButton stopTime;
    RelativeLayout relativeLayout;
    ////=============Gamne end
    com.startapp.sdk.ads.interstitials.InterstitialAd interstitialAdStati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudienceNetworkAds.initialize(this);

        String data = getIntent().getExtras().getString("easy");
        if (data.contains("easy")) {
            GameMode = "easy";
        } else if (data.contains("mediam")) {
            GameMode = "mediam";
        } else {
            if (data.contains("hard")) {
                GameMode = "hard";
            }
        }

        answer = findViewById(R.id.answer);
        relativeLayout=findViewById(R.id.id);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closekeybord();
            }
        });
        seeYourResut=findViewById(R.id.seeSult);
        stopTime=findViewById(R.id.stopeTime);
        mediaPlayer2 = MediaPlayer.create(MainActivity.this, R.raw.correct);
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.wrong);
        inputtime = findViewById(R.id.timeInput);
        playWithTime = findViewById(R.id.playwithtime);
        animationView = findViewById(R.id.animationView);
        startIo_banner = findViewById(R.id.startio_banner);
        random = new Random();
        timerTextView = findViewById(R.id.timer);
        scertextview = findViewById(R.id.score);
        sumtextView = findViewById(R.id.sumtextview);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        waronganim = findViewById(R.id.wronganim);
        rightanim = findViewById(R.id.rightanim);
        waronganim.setVisibility(View.INVISIBLE);
        rightanim.setVisibility(View.INVISIBLE);
        mAdView = findViewById(R.id.madview);
        fbBannerContinar = findViewById(R.id.banner_container);
        /////================
        startIo_banner.setVisibility(View.GONE);
        fbBannerContinar.setVisibility(View.GONE);
        mAdView.setVisibility(View.GONE);

        ///////////=============================================================Cheking Database
        dbref = FirebaseDatabase.getInstance().getReference("AdsControlle");
        dbref.child("Admob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();
                    adsremote = AdmobeAdsShowValue;
                    /////Callind Data Base And Take Data And get class
                    if (AdmobeAdsShowValue.equals("ad")) {
                        closeAds();
                        startIo_banner.setVisibility(View.GONE);
                        fbBannerContinar.setVisibility(View.GONE);
                        mAdView.setVisibility(View.VISIBLE);
                        loadBannerAd();
                        loadRewardedAd();
                        loadFullscreenAd();

                    }
                    if (AdmobeAdsShowValue.equals("io")) {
                        mAdView.setVisibility(View.GONE);
                        fbBannerContinar.setVisibility(View.GONE);
                        startIo_banner.setVisibility(View.VISIBLE);
                    }
                    if (AdmobeAdsShowValue.equals("fb")) {
                        closeAds();
                        startIo_banner.setVisibility(View.GONE);
                        mAdView.setVisibility(View.GONE);
                        fbBannerContinar.setVisibility(View.VISIBLE);
                        loadFacebookbanner();
                       LoadFacebookInterstaal();

                        ///Facebook Banner ad loading

                        ///===============Oncrate End
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        stopTime.setVisibility(View.GONE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                stopTime.setVisibility(View.VISIBLE);
            }
        }, 15000);


        stopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbref.child("Admob").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();
                            adsremote = AdmobeAdsShowValue;
                            /////Callind Data Base And Take Data And get class
                            if (AdmobeAdsShowValue.equals("ad")) {

                                showInterstitial();

                            }
                            if (AdmobeAdsShowValue.equals("io"))
                            {



                            }
                            if (AdmobeAdsShowValue.equals("fb")) {


                             showFacebookAdsindrastial();
                                ///===============Oncrate End
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                countDownTimer.cancel();
                inputtime.setVisibility(View.INVISIBLE);
                playWithTime.setVisibility(View.INVISIBLE);
                stopTime.setVisibility(View.INVISIBLE);
                animationView.cancelAnimation();



            }
        });



        inputtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();

            }
        });


        //==================== Calling classs
        newQutions();
        helperHandrl();
        //===================================
        StartAppAd.disableSplash();


        ///=============Database call

        timeup2();
        animationView.playAnimation();


        /////Onclick Leasener

        playWithTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopTime.setVisibility(View.INVISIBLE);

                closekeybord();
                scrore = 0;
                nmberofQustions = 0;
                String time = inputtime.getText().toString();
                if (time.isEmpty()) {
                    inputtime.setError("Enter Time In Minit");
                    inputtime.requestFocus();
                    return;
                }
                int intTime = Integer.parseInt(time);
                int max = intTime * 60000;
                countTIme = max;
                playWithTime.setVisibility(View.INVISIBLE);
                inputtime.setVisibility(View.INVISIBLE);
                IntentCount = String.valueOf(max);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playWithTime.setVisibility(View.VISIBLE);
                        inputtime.setVisibility(View.VISIBLE);
                        animationView.playAnimation();
                    }
                }, max);
                newQutions();
                timeup();

            }
        });
        seeYourResut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbref.child("Admob").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();
                            adsremote = AdmobeAdsShowValue;
                            /////Callind Data Base And Take Data And get class
                            if (AdmobeAdsShowValue.equals("ad")) {

                                showInterstitial();

                            }
                            if (AdmobeAdsShowValue.equals("io")) {



                            }
                            if (AdmobeAdsShowValue.equals("fb")) {


                                showFacebookAdsindrastial();
                                ///===============Oncrate End
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                String name = "You Answered of Qustion " + scrore + "\n Total Qustion there " + totalqustion + " .";

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("t", name);
                startActivity(intent);

            }
        });


    }
    private void LoadFacebookInterstaal() {




        interstitialAds = new com.facebook.ads.InterstitialAd(this, getString(R.string.fbfullscreenAds));
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
        interstitialAds.loadAd(
                interstitialAds.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }

    int ADMOVEREWORDADCLICKCOUNT =0;
    private void showFacebookAdsindrastial(){
        if (interstitialAds!=null&&interstitialAds.isAdLoaded()&& !interstitialAds.isAdInvalidated()){
            interstitialAds.show();
            Log.d("", "Interstitial ad is loaded and ready to be displayed!");


        }else {

        }

    }
    ///=================================Facebook Class
    private void loadFacebookbanner() {
        facebookadView = new com.facebook.ads.AdView(MainActivity.this, getString(R.string.fbbanenr), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(facebookadView);
        facebookadView.loadAd();


    }



    ////===========================================================game Classs

    private void helperHandrl() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vai = "a";
                helperHandrl();
            }
        }, 5000);
    }

    private void handeler() {


        final Handler handler = new Handler();

        if (vai.equals("a")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    answer.setTextSize(18);
                    answer.setText("Click Me To Help");
                    vai = "b";
                }
            }, 5000);


        }


    }

    public void newQutions() {

        nmberofQustions++;


        lacatiocreccetanswers = random.nextInt(4);
        int a = random.nextInt(61);
        int b = random.nextInt(41);
        int c = random.nextInt(61);
        if (GameMode == "easy") {
            sumtextView.setText(Integer.toString(a) + "+" + Integer.toString(b));
            helpanswer = a + b;
            HElPans = String.valueOf(helpanswer);
        } else if (GameMode == "mediam") {
            sumtextView.setText(Integer.toString(a) + "+" + Integer.toString(b) + "-" + Integer.toString(c));
            helpanswer = a + b - c;
            HElPans = String.valueOf(helpanswer);
        } else if (GameMode == "hard") {
            sumtextView.setText(Integer.toString(a) + "+" + Integer.toString(b) + "+" + Integer.toString(c));
            helpanswer = a + b + c;
            HElPans = String.valueOf(helpanswer);
        }

        for (int i = 0; i < 4; i++) {
            if (i == lacatiocreccetanswers) {
                if (GameMode == "easy") {
                    answers.add(a + b);

                } else if (GameMode == "mediam") {
                    answers.add(a + b - c);

                } else if (GameMode == "hard") {
                    answers.add(a + b + c);
                }

            } else {
                int wrongaswer = random.nextInt(81);
                if (GameMode == "easy") {
                    while (wrongaswer == a + b) {
                        wrongaswer = random.nextInt(81);
                    }
                } else if (GameMode == "mediam") {
                    while (wrongaswer == a + b - c) {
                        wrongaswer = random.nextInt(81);
                    }


                } else if (GameMode == "hard") {

                    while (wrongaswer == a + b + c) {
                        wrongaswer = random.nextInt(81);
                    }

                }


                answers.add(random.nextInt(81));
            }


        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
        handeler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                waronganim.setVisibility(View.INVISIBLE);
                rightanim.setVisibility(View.INVISIBLE);
            }
        }, 2200);
    }


    public void choseAnswer(View view) {
        if (Integer.toString(lacatiocreccetanswers).equals(view.getTag().toString())) {
            Log.i("Crecce", "Got It");

            answer.setText("Correct ");
            answer.setVisibility(View.VISIBLE);
            scrore++;

            mediaPlayer2.start();
            answers.clear();
            rightanim.setVisibility(View.VISIBLE);
            newQutions();


        } else {
            answer.setText("Wrong");
            answer.setVisibility(View.VISIBLE);
            Log.i("Faild", "Faild");
            mediaPlayer.start();
            waronganim.setVisibility(View.VISIBLE);
        }
        totalqustion = nmberofQustions;
        scertextview.setText(Integer.toString(scrore) + "/" + Integer.toString(nmberofQustions));
        answer1 = scertextview.getText().toString();
        answers.clear();
        newQutions();
    }



    public void timeup() {
        if (countDownTimer!=null){
            countDownTimer.cancel();

        }

        animationView.playAnimation();
        countDownTimer=new CountDownTimer(countTIme, 1000) {
            @Override
            public void onTick(long l) {
                timerTextView.setText(String.valueOf(l / 1000) + "s");
            }

            @Override
            public void onFinish() {

                String name = "You Carrect Answerd  " +scrore+ " .Total Qustion " + totalqustion ;
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("t", name);
                startActivity(intent);
            }
        }.start();

    }
    int countTimerValue;

    public void timeup2() {
        animationView.playAnimation();
        if (countDownTimer!=null){
            countDownTimer.cancel();

        }
        int intTime=1;
        int max = intTime * 60000;
      int   countTIme2 = max;
        countDownTimer = new CountDownTimer(countTIme2, 1000) {
            @Override
            public void onTick(long l) {
                timerTextView.setText(String.valueOf(l / 1000) + "s");
                countTimerValue= (int) (l/1000);


            }


            @Override
            public void onFinish() {
                dbref.child("Admob").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AdmobeAdsShowValue = dataSnapshot.child("AdmobAdPermitted").getValue().toString();
                            adsremote = AdmobeAdsShowValue;
                            /////Callind Data Base And Take Data And get class
                            if (AdmobeAdsShowValue.equals("ad")) {
                                showInterstitial();



                            }
                            if (AdmobeAdsShowValue.equals("io")) {


                            }
                            if (AdmobeAdsShowValue.equals("fb")) {
                                showFacebookAdsindrastial();


                                ///===============Oncrate End
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });



                int minit = 1;
                float convartminit = Float.parseFloat(String.valueOf(minit));
                int cm = (int) (convartminit / 60000);
                String name = "You Correct Answerd  " +scrore+ " .Total Qustion " + totalqustion + ". With in 1 minit";

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("t", name);
                startActivity(intent);
                closeAds();

            }
        }.start();

    }

    private void closekeybord() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void ads(View view) {


        if (answer.getText().toString() == "Click Me To Help") {


            if (!isNetworkAvailable(this)) {
                answer.setTextSize(18);
                answer.setText("Please Connect The Network for get Answer");


            } else {
                answer.setText(HElPans);

                if (AdmobeAdsShowValue.equals("ad")) {

                    if (ADMOVEREWORDADCLICKCOUNT >=1){

                    }else{
                        showRewardAd();

                    }


                } else {
                    if (AdmobeAdsShowValue.equals("io")) { }

                }


            }

        } else {
            answer.setText("Try Your Self");
        }

    }

    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this).setTitle("Exit")
                .setIcon(R.drawable.exiticon)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).setMessage("Confram Exit").setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                finishAndRemoveTask();


            }
        }).show();

        /// super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        if (facebookadView != null) {
            facebookadView.destroy();
        }


        if (mAdView!=null){
            mAdView.destroy();
        }

        super.onDestroy();
    }

    public void showRewardedVideo_Startio() {
        final StartAppAd rewardedVideo = new StartAppAd(this);


        rewardedVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {

                countDownTimer.cancel();
                // Grant the reward to user
            }
        });




        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                rewardedVideo.showAd();



            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                // Can't show rewarded video
            }
        });
    }

    private void closeAds() {

        if (facebookadView != null) {
            facebookadView.destroy();
        }

        if (mAdView!=null){
            mAdView.destroy();
        }







    }


}