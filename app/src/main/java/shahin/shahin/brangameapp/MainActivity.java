package shahin.shahin.brangameapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
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

import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.ArrayList;
import java.util.Random;

import soup.neumorphism.NeumorphButton;

public class MainActivity extends AppCompatActivity {

    ////====for Ads
    RewardedAd mRewardedAd;
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitialAd;
    String AdmobeAdsShowValue="";

    //+============================================================
    Button button0,button1,button2,button3;
    TextView sumtextView,scertextview,timerTextView,answer;
    int scrore=0;
    int nmberofQustions=0;
    ArrayList<Integer>answers=new ArrayList<Integer>();
    int lacatiocreccetanswers;
    LottieAnimationView animationView,rightanim,waronganim;
    MediaPlayer mediaPlayer,mediaPlayer2;
    EditText inputtime;
    NeumorphButton playWithTime;
    Random random;
    int countTIme;
    String IntentCount;
    String answer1;
    ////


    TextToSpeech textToSpeech;
    ///===============================================================
    // loadFullscreenAd method starts here.....
    public void loadFullscreenAd(){



        //Requesting for a fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
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

    //==============================================
    private void showInterstitial() {
        // Show the ad if it's ready.
        if (mInterstitialAd != null ) {
            mInterstitialAd.show(this);

        } else {
          ///  Toast.makeText(getApplicationContext(), "Ads Not redy", Toast.LENGTH_SHORT).show();
        }
    }
    //============================================
    // Reward Video Ad Loading method starts here.....
    private void loadRewardedAd() {





        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
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
                                loadRewardedAd();
                            }
                        });


                    }

                });


        //===========================================

    }
    ////===========
    private void showRewardAd() {
        // Show the ad if it's ready.
        if (mRewardedAd != null) {

            mRewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                  //  Toast.makeText(getApplicationContext(), "You Watch Videos Ads", Toast.LENGTH_SHORT).show();
                    // Handle the reward.

                    //Reward user and showing a dialog
//                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                    alertDialog.setTitle("Congrats \uD83D\uDE03");
//                    alertDialog.setMessage("Nice Job! You got some reward!");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();
//                    //=========================================

                }
            });
        } else {

            Toast.makeText(getApplicationContext(), "Video Ads not Loads", Toast.LENGTH_SHORT).show();

        }
    }

    DatabaseReference dbref;
    String adsremote;

    /////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ////===============
        answer=findViewById(R.id.answer);
        mediaPlayer2=MediaPlayer.create(MainActivity.this,R.raw.correct);
        mediaPlayer= new MediaPlayer();
        mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.wrong);
        inputtime=findViewById(R.id.timeInput);
        playWithTime=findViewById(R.id.playwithtime);
        animationView=findViewById(R.id.animationView);
        random=new Random();
        timerTextView=findViewById(R.id.timer);
        scertextview=findViewById(R.id.score);
        sumtextView=findViewById(R.id.sumtextview);
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        waronganim=findViewById(R.id.wronganim);
        rightanim=findViewById(R.id.rightanim);
        waronganim.setVisibility(View.INVISIBLE);
        rightanim.setVisibility(View.INVISIBLE);
        newQutions();
        loadFullscreenAd();
        loadRewardedAd();
        dbref= FirebaseDatabase.getInstance().getReference("AdsControlle");
        dbref.child("Admob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    AdmobeAdsShowValue=dataSnapshot.child("AdmobAdPermitted").getValue().toString();
                    adsremote=AdmobeAdsShowValue;
                } }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        playWithTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                StartAppAd.showAd(MainActivity.this);
                //showRewardedVideo();

                if (AdmobeAdsShowValue.equals("yes")){
                    showInterstitial();
                    //  showRewardAd();
                }
                scrore=0;
                nmberofQustions=0;
                String time=inputtime.getText().toString();
                if (time.isEmpty()){
                    inputtime.setError("Enter Time In Minit");
                    inputtime.requestFocus();
                    return;
                }
                closekeybord();
                int intTime=Integer.parseInt(time);
                int max=intTime*60000;
               countTIme=max;
               playWithTime.setVisibility(View.GONE);
               inputtime.setVisibility(View.GONE);
               IntentCount=String.valueOf(max);
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       playWithTime.setVisibility(View.VISIBLE);
                       inputtime.setVisibility(View.VISIBLE);
                       animationView.playAnimation();}
               },max);
                newQutions();
                timeup();
            }
        });

        StartAppSDK.setTestAdsEnabled(true);
        StartAppAd.disableSplash();



    }

    private void handeler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                answer.setTextSize(18);
                answer.setText("Click Me To Help");
            }
        },5000);
    }
    //======
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void pipmode(){
        enterPictureInPictureMode();

    }

    ///Where is ads?
    //less fin it
    //========================================
    public void newQutions(){
        handeler();
        new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 waronganim.setVisibility(View.INVISIBLE);
                 rightanim.setVisibility(View.INVISIBLE); }
         },2200);
        int a=random.nextInt(21);
        int b=random.nextInt(21);
        lacatiocreccetanswers=random.nextInt(4);
        sumtextView.setText(Integer.toString(a)+"+"+Integer.toString(b));
        lacatiocreccetanswers=random.nextInt(4);
        helpanswer=a+b;

        for (int i=0;i<4;i++){
            if (i==lacatiocreccetanswers){
                answers.add(a+b);
            }else {
                int wrongaswer=random.nextInt(41);

                while (wrongaswer==a+b){
                    wrongaswer=random.nextInt(41);
                }
                answers.add(random.nextInt(41)); }
        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }
    ///================================================================================//
    int helpanswer;



///=============================================================================================
    public void choseAnswer(View view){
        if ( Integer.toString(lacatiocreccetanswers).equals(view.getTag().toString())){
            Log.i("Crecce","Got It");

            answer.setText("Correct ");
            answer.setVisibility(View.VISIBLE);
            scrore++;

            mediaPlayer2.start();
            answers.clear();
            rightanim.setVisibility(View.VISIBLE);
            newQutions();



        }
        else {
            answer.setText("Wrong");
            answer.setVisibility(View.VISIBLE);
            Log.i("Faild","Faild");
           mediaPlayer.start();
           waronganim.setVisibility(View.VISIBLE);
        }
        nmberofQustions++;
        totalqustion=nmberofQustions;
        scertextview.setText(Integer.toString(scrore)+"/"+Integer.toString(nmberofQustions));
        answer1=scertextview.getText().toString();
        answers.clear();
        newQutions();
    }
    int totalqustion;
        //======================================

    ////Auto Timer Class

    ////================================================
    public  void timeup(){
        animationView.playAnimation();
        new CountDownTimer(countTIme,1000){
            @Override
            public void onTick(long l) {
                timerTextView.setText(String.valueOf(l/1000)+"s");
            }

            @Override
            public void onFinish() {
                animationView.pauseAnimation();
                int minit=Integer.parseInt(IntentCount);
                float convartminit=Float.parseFloat(String.valueOf(minit));
                int cm= (int) (convartminit/60000);
                String name="আপনি "+cm+" মিনিটের ভিতরে উত্তর দিয়েছেন "+scrore+"টি  ।\n মোট প্রশ্ন ছিলো "+totalqustion+" টি ।";

                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("t",name);
                startActivity(intent);
            }
        }.start();

    }

    ///=================================================================================
    private  void  closekeybord(){
        View view=this.getCurrentFocus();
        if (view!=null){
            InputMethodManager methodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (isInPictureInPictureMode){}
        else {

        }
    }




    public void ads(View view){


        if (answer.getText().toString()=="Click Me To Help"){


            if (!isNetworkAvailable(this)){
                answer.setTextSize(18);
                answer.setText("Please Connect The Network for get Answer");



            }
            else {

                if (AdmobeAdsShowValue.equals("yes")){

                    showRewardAd();

                }
                answer.setText(""+helpanswer);


            }

        } else {
            answer.setText("Try Your Self");
        }

    }

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
                        if (AdmobeAdsShowValue.equals("yes")){
                            mInterstitialAd.show(MainActivity.this);
                        }


                    }
                }).setMessage("Confram Exit").setPositiveButton("Yes Exit!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                finishAndRemoveTask();


            }
        }).show();

        /// super.onBackPressed();
    }


//    public void showRewardedVideo() {
//        final StartAppAd rewardedVideo = new StartAppAd(this);
//
//        rewardedVideo.setVideoListener(new VideoListener() {
//            @Override
//            public void onVideoCompleted() {
//                // Grant the reward to user
//            }
//        });
//
//        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
//            @Override
//            public void onReceiveAd(Ad ad) {
//                rewardedVideo.showAd();
//            }
//
//            @Override
//            public void onFailedToReceiveAd(Ad ad) {
//                // Can't show rewarded video
//            }
//        });
//    }


}