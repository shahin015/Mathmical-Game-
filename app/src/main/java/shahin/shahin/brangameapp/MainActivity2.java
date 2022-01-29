package shahin.shahin.brangameapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class MainActivity2 extends AppCompatActivity {
    TextView textView,textView2;
    Button button;
    String dataformintent;
    TextToSpeech textToSpeech;

    InterstitialAd mInterstitialAd;
    RewardedAd mRewardedAd;
    // Reward Video Ad Loading method starts here.....
    private void loadRewardedAd(){

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback(){
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

            mRewardedAd.show((Activity) getApplicationContext(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                    Toast.makeText(getApplicationContext(), "You Watch Videos Ads", Toast.LENGTH_SHORT).show();
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
            /// textView.setText("The rewarded ad wasn't ready yet.");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView=findViewById(R.id.textviewsssss);
        textView2=findViewById(R.id.text2);
        dataformintent=getIntent().getStringExtra("t");
        button=findViewById(R.id.agine);
        textView.setText(dataformintent);
        String tx=textView.getText().toString();
       // showRewardAd();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRewardAd();
                Intent intent=new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
            }
        });

      //  textToSpeech.speak(""+tx,TextToSpeech.QUEUE_ADD,null);
        ///uf ads ar distrabing
        ///no more ads
    }



}