package pl.op.danex11.hulajwro;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static java.lang.Boolean.TRUE;

//import pl.op.danex11.hulajwro.SpielFahre;

public class AndroidLauncher extends AndroidApplication {


    //private AdView adView;

                                           

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        //initialize(new SpielFahre(), config);
        //View gameView=initializeForView(new SpielFahre(), config);
        //AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //Layout params
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);

        //set general view to @layout
        setContentView(layout);
        Log.d("tagA", "Layout Loaded once0");



        ////View gameView=initializeForView(new BuckyRun(), config);
        //initialize gameview
        View gameView=initializeForView(new SpielFahre(), config);
        //parameters of gameview
        RelativeLayout.LayoutParams gameViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gameViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        gameViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        gameView.setLayoutParams(gameViewParams);
        //add gameview to @layout
        layout.addView(gameView);
        //ad1: add adview
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        TextView textview = new TextView(this);
        textview.setText("this textview stays behind an Ad");
        textview.setTextSize(10);
        //ca-app-pub-3940256099942544/6300978111 == test Id for loading testing ads
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        //add adview to @layout
        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        topParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        topParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layout.addView(textview,topParams);
        //adView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        //adView.setBackgroundColor(Color.RED);
        layout.addView(adView, topParams);



        //ad2: initialize and parametrize adview
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //request and load ad
        //AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        //adView.loadAd(adRequestBuilder.build());
        //ad3 : request and load
        //Create request.
        AdRequest adRequest = new AdRequest.Builder().
                build();
        //adRequest.setTesting(true);
        //load an ad <<< Loads only while connection to Internet is avaliable
        adView.loadAd(adRequest);


        //set general view to @layout
        setContentView(layout);

    }

    //hide software home, back, recent buttons
    @TargetApi(19)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

}
