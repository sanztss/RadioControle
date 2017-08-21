package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.ImageView;


import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.ampiri.sdk.Ampiri;
import com.ampiri.sdk.interstitial.InterstitialAd;
import com.ampiri.sdk.interstitial.InterstitialAdPool;
import com.ampiri.sdk.listeners.InterstitialAdCallback;
import com.ampiri.sdk.mediation.ResponseStatus;
import com.ampiri.sdk.mediation.admob.AdMobMediation;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Advert;
import accessweb.com.br.radiocontrole.model.Channel;
import accessweb.com.br.radiocontrole.model.Group;
import accessweb.com.br.radiocontrole.model.Radio;
import accessweb.com.br.radiocontrole.model.Settings;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;

/**
 * Created by Des. Android on 05/07/2017.
 */

public class SplashActivity extends Activity {

    private Context mContext;
    private Activity mActivity;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    private ArrayList<String> modulos = new ArrayList<String>();
    private List<Channel> canais = new ArrayList<Channel>();
    private List<Radio> radios = new ArrayList<Radio>();

    private ImageView splash;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = getApplicationContext();
        mActivity = SplashActivity.this;

        final CacheData cacheData = new CacheData(mContext);

        splash = (ImageView) findViewById(R.id.splash);
        if (!cacheData.getString("splash").equals("")){
            Picasso.with(mContext)
                    .load(cacheData.getString("splash"))
                    .into(splash);
        }

        if (!cacheData.getString("color").equals("")){
            if (Build.VERSION.SDK_INT > 23) {
                float[] hsv = new float[3];
                int color = Color.parseColor(cacheData.getString("color"));
                Color.colorToHSV(color, hsv);
                hsv[2] *= 0.8f;
                color = Color.HSVToColor(hsv);

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(color);
            }
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                Group group = client.radiogroupRadioGroupIdGet("tradicaoAM");
                for (Radio radio: group){
                    radios.add(radio);
                }

                Settings settings = client.radioIdGet(radios.get(0).getId());
                for (Channel channel: settings.getChannels()){
                    canais.add(channel);
                }

                for (Advert advert : settings.getAdverts()){
                    switch (advert.getLocal()){
                        case "player":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsPlayerContent", "off");
                            } else if (advert.getType().equals("ampiri")) {
                                cacheData.putString("adsPlayerContent", "ampiri");
                                cacheData.putString("adsPlayerValue", advert.getValueAndroid());
                            } else if (advert.getType().equals("image")) {
                                cacheData.putString("adsPlayerContent", "image");
                                cacheData.putString("adsPlayerValue", advert.getValueAndroid());
                                cacheData.putString("adsPlayerLink", advert.getLink());
                            }
                            break;
                        case "home":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsHomeContent", "off");
                            } else if (advert.getType().equals("ampiri")) {
                                cacheData.putString("adsHomeContent", "ampiri");
                                cacheData.putString("adsHomeValue", advert.getValueAndroid());
                            } else if (advert.getType().equals("image")) {
                                cacheData.putString("adsHomeContent", "image");
                                cacheData.putString("adsHomeValue", advert.getValueAndroid());
                                cacheData.putString("adsHomeLink", advert.getLink());
                            }
                            break;
                        case "play":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsPlayContent", "off");
                            } else if (advert.getType().equals("audio")) {
                                cacheData.putString("adsPlayContent", "audio");
                                cacheData.putString("adsPlayValue", advert.getValueAndroid());
                            }
                            break;
                        case "wall":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsWallContent", "off");
                            } else if (advert.getType().equals("ampiri")) {
                                cacheData.putString("adsWallContent", "ampiri");
                                cacheData.putString("adsWallValue", advert.getValueAndroid());
                            }
                            break;
                        case "interstitial":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsInterstitialContent", "off");
                            } else if (advert.getType().equals("ampiri")) {
                                cacheData.putString("adsInterstitialContent", "ampiri");
                                cacheData.putString("adsInterstitialValue", advert.getValueAndroid());
                                System.out.println("InterstitialAdCallback: " + cacheData.getString("adsInterstitialContent"));
                            }
                            break;
                        default:
                            break;
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (cacheData.getString("adsInterstitialContent").equals("ampiri")) {

                    InterstitialAdCallback interstitialAdListener = new InterstitialAdCallback() {
                        @Override
                        public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
                            System.out.println("InterstitialAdCallback: " + "onAdLoaded");
                            System.out.println("InterstitialAdCallback: " + "onAdLoaded");
                            if (!cacheData.getString("splash").equals("")){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (interstitialAd.isReady()){
                                            interstitialAd.showAd();
                                        }
                                    }
                                }, SPLASH_TIME_OUT);
                            }else {
                                if (interstitialAd.isReady()){
                                    interstitialAd.showAd();
                                }
                            }
                        }

                        @Override
                        public void onAdFailed(@NonNull InterstitialAd interstitialAd, ResponseStatus responseStatus) {
                            System.out.println("InterstitialAdCallback: " + "onAdFailed");
                            System.out.println("InterstitialAdCallback: " + "onAdFailed");
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("canais", (Serializable) canais);
                            intent.putExtra("radios", (Serializable) radios);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onAdOpened(@NonNull InterstitialAd interstitialAd) {
                            System.out.println("InterstitialAdCallback: " + "onAdOpened");
                            System.out.println("InterstitialAdCallback: " + "onAdOpened");
                        }

                        @Override
                        public void onAdClicked(@NonNull InterstitialAd interstitialAd) {
                            System.out.println("InterstitialAdCallback: " + "onAdClicked");
                            System.out.println("InterstitialAdCallback: " + "onAdClicked");
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("canais", (Serializable) canais);
                            intent.putExtra("radios", (Serializable) radios);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onAdClosed(@NonNull InterstitialAd interstitialAd) {
                            System.out.println("InterstitialAdCallback: " + "onAdClosed");
                            System.out.println("InterstitialAdCallback: " + "onAdClosed");
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("canais", (Serializable) canais);
                            intent.putExtra("radios", (Serializable) radios);
                            startActivity(intent);
                            finish();
                        }
                    };
                    //interstitialAd = InterstitialAdPool.load( SplashActivity.this, "6b34bf0e-04c4-47f9-b4d3-caeddeab0b13", interstitialAdListener);
                    interstitialAd = InterstitialAdPool.load( SplashActivity.this, cacheData.getString("adsInterstitialValue"), interstitialAdListener);

                    Ampiri.addMediationAdapter(new AdMobMediation.Builder()
                            //.addTestDevice("ca-app-pub-4694313893492524~8880658070")
                            .addTestDevice("ca-app-pub-4694313893492524/4709656385")
                            .build());
                }else {
                    if (!cacheData.getString("splash").equals("")){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }
                        }, SPLASH_TIME_OUT);
                    }else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("canais", (Serializable) canais);
                        intent.putExtra("radios", (Serializable) radios);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }.execute();

    }

}