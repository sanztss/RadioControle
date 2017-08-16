package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;
import com.ampiri.sdk.Ampiri;
import com.ampiri.sdk.interstitial.InterstitialAd;
import com.ampiri.sdk.interstitial.InterstitialAdPool;
import com.ampiri.sdk.listeners.InterstitialAdCallback;
import com.ampiri.sdk.mediation.ResponseStatus;
import com.ampiri.sdk.mediation.admob.AdMobMediation;
import com.avocarrot.sdk.Avocarrot;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Advert;
import accessweb.com.br.radiocontrole.model.Channel;
import accessweb.com.br.radiocontrole.model.Empty;
import accessweb.com.br.radiocontrole.model.Group;
import accessweb.com.br.radiocontrole.model.Radio;
import accessweb.com.br.radiocontrole.model.Settings;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.CognitoSyncClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;

import static accessweb.com.br.radiocontrole.R.drawable.ca;
import static accessweb.com.br.radiocontrole.R.id.imagemAnuncio;
import static android.R.id.empty;
import static com.ampiri.sdk.device.i.c;
import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by Des. Android on 05/07/2017.
 */

public class SplashActivity extends Activity {

    private Context mContext;
    private Activity mActivity;

    private InterstitialAd interstitialAd;
    private Boolean ampireSuccess = false;
    private Boolean bannerImage = false;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    private ArrayList<String> modulos = new ArrayList<String>();
    private List<Channel> canais = new ArrayList<Channel>();
    private List<Radio> radios = new ArrayList<Radio>();

    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = getApplicationContext();
        mActivity = SplashActivity.this;

        final CacheData cacheData = new CacheData(mContext);

        CognitoClientManager.init(mActivity);
        CognitoSyncClientManager.init(this);

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
                            Log.e("AAAAAAAAAAAsasas", "ASASASASAS");
                            Log.e("AAAAAAAAAAAsasas", "ASASASASAS");
                            Log.e("AAAAAAAAAAAsasas", "ASASASASAS");
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsInterstitialContent", "off");
                            } else if (advert.getType().equals("ampiri")) {
                                cacheData.putString("adsInterstitialContent", "ampiri");
                                cacheData.putString("adsInterstitialValue", advert.getValueAndroid());
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
                Log.e("AAAAAAAAAAAsasas", cacheData.getString("adsInterstitialContent"));
                if (cacheData.getString("adsInterstitialContent").equals("off")){
                    ampireSuccess = false;
                    ClientConfiguration clientConfiguration = new ClientConfiguration();
                    CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                    userPool.getCurrentUser().getSession(authenticationHandler);
                }else if (cacheData.getString("adsInterstitialContent").equals("ampiri")) {
                    InterstitialAdCallback interstitialAdListener = new InterstitialAdCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            System.out.println("onAdLoaded");
                            ampireSuccess = true;
                            ClientConfiguration clientConfiguration = new ClientConfiguration();
                            CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                            userPool.getCurrentUser().getSession(authenticationHandler);
                        }

                        @Override
                        public void onAdFailed(@NonNull InterstitialAd interstitialAd, ResponseStatus responseStatus) {
                            System.out.println("onAdFailed: " + responseStatus.name());
                            ampireSuccess = false;
                            ClientConfiguration clientConfiguration = new ClientConfiguration();
                            CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                            userPool.getCurrentUser().getSession(authenticationHandler);
                        }

                        @Override
                        public void onAdOpened(@NonNull InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onAdClicked(@NonNull InterstitialAd interstitialAd) {
                            if (cacheData.getString("idRadio").equals("")){
                                Intent intent = new Intent(SplashActivity.this, RadioGroupActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onAdClosed(@NonNull InterstitialAd interstitialAd) {
                            if (cacheData.getString("idRadio").equals("")){
                                Intent intent = new Intent(SplashActivity.this, RadioGroupActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }
                        }
                    };
                    //interstitialAd = InterstitialAdPool.load( SplashActivity.this, "6b34bf0e-04c4-47f9-b4d3-caeddeab0b13", interstitialAdListener);
                    interstitialAd = InterstitialAdPool.load( SplashActivity.this, cacheData.getString("adsInterstitialValue"), interstitialAdListener);

                    Ampiri.addMediationAdapter(new AdMobMediation.Builder()
                            //.addTestDevice("ca-app-pub-4694313893492524~8880658070")
                            .addTestDevice("ca-app-pub-4694313893492524/4709656385")
                            .build());
                } else if (cacheData.getString("adsInterstitialContent").equals("image")) {
                    ampireSuccess = false;
                    bannerImage = true;
                    ClientConfiguration clientConfiguration = new ClientConfiguration();
                    CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                    userPool.getCurrentUser().getSession(authenticationHandler);
                }

            }
        }.execute();

        /*InterstitialAdCallback interstitialAdListener = new InterstitialAdCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                System.out.println("onAdLoaded");
                ampireSuccess =  true;
                ClientConfiguration clientConfiguration = new ClientConfiguration();
                CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                userPool.getCurrentUser().getSession(authenticationHandler);
            }

            @Override
            public void onAdFailed(@NonNull InterstitialAd interstitialAd, ResponseStatus responseStatus) {
                System.out.println("onAdFailed: " + responseStatus.name());
                ampireSuccess = false;
                ClientConfiguration clientConfiguration = new ClientConfiguration();
                CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                userPool.getCurrentUser().getSession(authenticationHandler);
            }

            @Override
            public void onAdOpened(@NonNull InterstitialAd interstitialAd) {

            }

            @Override
            public void onAdClicked(@NonNull InterstitialAd interstitialAd) {
                if (cacheData.getString("idRadio").equals("")){
                    Intent intent = new Intent(SplashActivity.this, RadioGroupActivity.class);
                    intent.putExtra("canais", (Serializable) canais);
                    intent.putExtra("radios", (Serializable) radios);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("canais", (Serializable) canais);
                    intent.putExtra("radios", (Serializable) radios);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAdClosed(@NonNull InterstitialAd interstitialAd) {
                if (cacheData.getString("idRadio").equals("")){
                    Intent intent = new Intent(SplashActivity.this, RadioGroupActivity.class);
                    intent.putExtra("canais", (Serializable) canais);
                    intent.putExtra("radios", (Serializable) radios);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("canais", (Serializable) canais);
                    intent.putExtra("radios", (Serializable) radios);
                    startActivity(intent);
                    finish();
                }
            }
        };
        //interstitialAd = InterstitialAdPool.load( SplashActivity.this, "b43b17fe-2e32-45a5-93fe-8d1086a5611b", interstitialAdListener);
        interstitialAd = InterstitialAdPool.load( SplashActivity.this, "6b34bf0e-04c4-47f9-b4d3-caeddeab0b13", interstitialAdListener);

        Ampiri.addMediationAdapter(new AdMobMediation.Builder()
                //.addTestDevice("ca-app-pub-4694313893492524~8880658070")
                .addTestDevice("ca-app-pub-4694313893492524/4709656385")
                .build());*/

        /*ClientConfiguration clientConfiguration = new ClientConfiguration();
        CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
        userPool.getCurrentUser().getSession(authenticationHandler);*/

        splash = (ImageView) findViewById(R.id.splash);


    }

    // CONVERSOR RGB PARA HEXADECIMAL
    public static String toHex(int r, int g, int b) {
        return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
    }
    private static String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {

        // SUCESSO USUÁRIO COM ID LOGADA
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {

            CognitoClientManager.addLogins("cognito-idp.us-east-1.amazonaws.com/us-east-1_uEcyGgDBj", cognitoUserSession.getIdToken().getJWTToken());

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    String userID = CognitoClientManager.getCredentials().getIdentityId();
                    Log.i(TAG, "userID = " + userID);

                    Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");
                    profileData.synchronize(new SyncCallback() {
                        @Override
                        public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                            Log.d(TAG, "Sync success" + dataset);
                            System.out.println(dataset.get("name"));
                            System.out.println(dataset.get("email"));
                            System.out.println(dataset.get("phone"));
                            CacheData cacheData = new CacheData(mContext);
                            cacheData.putString("userId", dataset.get("email"));
                            cacheData.putString("userEmail", dataset.get("email"));
                            cacheData.putString("userNome", dataset.get("name"));
                            cacheData.putString("userTelefone", dataset.get("phone"));
                            cacheData.putString("userUrlFoto", dataset.get("picture"));

                        }

                        @Override
                        public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                            Log.d(TAG, "Conflict");
                            return false;
                        }

                        @Override
                        public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                            Log.d(TAG, "Dataset deleted");
                            return false;
                        }

                        @Override
                        public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                            Log.d(TAG, "Datasets merged");
                            return false;
                        }

                        @Override
                        public void onFailure(DataStorageException dse) {
                            Log.e(TAG, "Sync fails", dse);
                        }
                    });

                    ApiClientFactory factory = new ApiClientFactory();
                    factory.credentialsProvider(CognitoClientManager.getCredentials());
                    factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                    final RadiocontroleClient client = factory.build(RadiocontroleClient.class);

                    Group group = client.radiogroupRadioGroupIdGet("tradicaoAM");
                    //Log.e("AAAAAAAAAAAAAAAAAAAAA", "" + group.get(0).getName());
                    for (Radio radio: group){
                        radios.add(radio);
                    }

                    Settings settings = client.radioIdGet(radios.get(0).getId());

                    Log.e("AAAAAAAAAAAAAAAAAAAAA", "" + settings.getAdverts().size());

                    // COR PRINCIPAL
                    String [] parts = settings.getColor().toString().split(",");
                    String hex = toHex(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

                    // RECUPERANDO DADOS
                    CacheData cacheData = new CacheData(getApplicationContext());

                    cacheData.putString("rssLink", settings.getRss().toString());
                    cacheData.putString("color", hex);
                    cacheData.putString("logo", "https://s3.amazonaws.com/radiocontrole/radios/tradicaoAM/app/" + settings.getLogo());
                    cacheData.putString("splash", "https://s3.amazonaws.com/radiocontrole/radios/tradicaoAM/app/" + settings.getSplash());
                    cacheData.putString("anuncio", settings.getAnnouncement());

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
                                } else if (advert.getType().equals("image")) {
                                    cacheData.putString("adsInterstitialContent", "image");
                                    cacheData.putString("adsInterstitialValue", advert.getValueAndroid());
                                    cacheData.putString("adsInterstitialLink", advert.getLink());
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    modulos.add("início");
                    for (String modulo:settings.getModules()){
                        switch (modulo){
                            case "about":
                                modulos.add("sobre o aplicativo");
                                break;
                            case "alarms":
                                modulos.add("alarms");
                                break;
                            case "news":
                                modulos.add("notícias");
                                break;
                            case "podcasts":
                                modulos.add("podcasts");
                                break;
                            case "profile":
                                modulos.add("perfil");
                                break;
                            case "programs":
                                modulos.add("programas");
                                break;
                            case "promotions":
                                modulos.add("promoções");
                                break;
                            case "wall":
                                modulos.add("mural");
                                break;
                            default:
                                break;
                        }
                    }
                    modulos.add("estações");
                    cacheData.putListString("modulos", modulos);

                    Log.v("aaaaaaaaaaaaaaaaaaaa", "Nome:" + settings.getChannels().get(0).getName() + ", Rds:" + settings.getChannels().get(0).getRds() + ", Principal:" + settings.getChannels().get(0).getMain() + ", LowStreams:" + settings.getChannels().get(0).getLowStreams() + ", HighStreams:" + settings.getChannels().get(0).getHighStreams());
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    final CacheData cacheData = new CacheData(mContext);
                    if (!cacheData.getString("splash").equals("")){
                        Picasso.with(mContext)
                                .load(cacheData.getString("splash"))
                                .into(splash);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (ampireSuccess){
                                    interstitialAd.showAd();
                                }else {
                                    if (cacheData.getString("idRadio").equals("")){
                                        Intent intent = new Intent(SplashActivity.this, RadioGroupActivity.class);
                                        intent.putExtra("canais", (Serializable) canais);
                                        intent.putExtra("radios", (Serializable) radios);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        intent.putExtra("canais", (Serializable) canais);
                                        intent.putExtra("radios", (Serializable) radios);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            }
                        }, SPLASH_TIME_OUT);
                    }else {
                        if (ampireSuccess){
                            interstitialAd.showAd();
                        }else {
                            if (cacheData.getString("idRadio").equals("")){
                                Intent intent = new Intent(SplashActivity.this, RadioGroupActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                }
            }.execute();
        }

        // FALHA USUÁRIO COM ID DESLOGADA
        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    //CognitoClientManager.getCredentials().getIdentityId();
                    ApiClientFactory factory = new ApiClientFactory();
                    factory.credentialsProvider(CognitoClientManager.getCredentials());
                    factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                    final RadiocontroleClient client = factory.build(RadiocontroleClient.class);

                    Group group = client.radiogroupRadioGroupIdGet("tradicaoAM");
                    for (Radio radio: group){
                        radios.add(radio);
                    }
                    Settings settings = client.radioIdGet(radios.get(0).getId());

                    // COR PRINCIPAL
                    String [] parts = settings.getColor().toString().split(",");
                    String hex = toHex(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

                    // RECUPERANDO DADOS
                    CacheData cacheData = new CacheData(getApplicationContext());

                    cacheData.putString("rssLink", settings.getRss().toString());
                    cacheData.putString("color", hex);
                    cacheData.putString("logo", "https://s3.amazonaws.com/radiocontrole/radios/tradicaoAM/app/" + settings.getLogo());
                    cacheData.putString("splash", "https://s3.amazonaws.com/radiocontrole/radios/tradicaoAM/app/" + settings.getSplash());
                    cacheData.putString("anuncio", settings.getAnnouncement());

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
                            case "intersticial":
                                if (advert.getType().equals("off")){
                                    cacheData.putString("adsIntersticialContent", "off");
                                } else if (advert.getType().equals("ampiri")) {
                                    cacheData.putString("adsIntersticialContent", "ampiri");
                                    cacheData.putString("adsIntersticialValue", advert.getValueAndroid());
                                } else if (advert.getType().equals("image")) {
                                    cacheData.putString("adsIntersticialContent", "image");
                                    cacheData.putString("adsIntersticialValue", advert.getValueAndroid());
                                    cacheData.putString("adsIntersticialLink", advert.getLink());
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    modulos.add("início");
                    for (String modulo:settings.getModules()){
                        switch (modulo){
                            case "about":
                                modulos.add("sobre o aplicativo");
                                break;
                            case "alarms":
                                modulos.add("alarms");
                                break;
                            case "news":
                                modulos.add("notícias");
                                break;
                            case "podcasts":
                                modulos.add("podcasts");
                                break;
                            case "profile":
                                modulos.add("perfil");
                                break;
                            case "programs":
                                modulos.add("programas");
                                break;
                            case "promotions":
                                modulos.add("promoções");
                                break;
                            case "wall":
                                modulos.add("mural");
                                break;
                            default:
                                break;
                        }
                    }
                    modulos.add("estações");
                    cacheData.putListString("modulos", modulos);

                    Log.v("aaaaaaaaaaaaaaaaaaaa", "Nome:" + settings.getChannels().get(0).getName() + ", Rds:" + settings.getChannels().get(0).getRds() + ", Principal:" + settings.getChannels().get(0).getMain() + ", LowStreams:" + settings.getChannels().get(0).getLowStreams() + ", HighStreams:" + settings.getChannels().get(0).getHighStreams());
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);

                    final CacheData cacheData = new CacheData(mContext);
                    if (!cacheData.getString("splash").equals("")){
                        Picasso.with(mContext)
                                .load(cacheData.getString("splash"))
                                .into(splash);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (ampireSuccess){
                                    interstitialAd.showAd();
                                }else {
                                    if (cacheData.getString("idRadio").equals("")){
                                        Intent intent = new Intent(SplashActivity.this, RadioGroupActivity.class);
                                        intent.putExtra("canais", (Serializable) canais);
                                        intent.putExtra("radios", (Serializable) radios);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        intent.putExtra("canais", (Serializable) canais);
                                        intent.putExtra("radios", (Serializable) radios);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            }
                        }, SPLASH_TIME_OUT);
                    }else {
                        if (ampireSuccess){
                            interstitialAd.showAd();
                        }else {
                            if (cacheData.getString("idRadio").equals("")){
                                Intent intent = new Intent(SplashActivity.this, RadioGroupActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                }
            }.execute();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }

        @Override
        public void onFailure(Exception e) {

        }
    };

}