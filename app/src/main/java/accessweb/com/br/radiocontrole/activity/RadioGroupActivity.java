package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

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
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.RadioGroupListAdapter;
import accessweb.com.br.radiocontrole.model.Advert;
import accessweb.com.br.radiocontrole.model.Channel;
import accessweb.com.br.radiocontrole.model.Group;
import accessweb.com.br.radiocontrole.model.Programs;
import accessweb.com.br.radiocontrole.model.Promotions;
import accessweb.com.br.radiocontrole.model.Radio;
import accessweb.com.br.radiocontrole.model.RadioApp;
import accessweb.com.br.radiocontrole.model.Settings;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.CognitoSyncClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;

import static accessweb.com.br.radiocontrole.R.drawable.ca;

/**
 * Created by Des. Android on 19/07/2017.
 */

public class RadioGroupActivity extends Activity {

    private Context mContext;
    private Activity mActivity;
    private RecyclerView recyclerView;
    private RadioGroupListAdapter adapter;
    private List<RadioApp> radioApps = new ArrayList<>();

    private ArrayList<String> modulos = new ArrayList<String>();
    private List<Channel> canais = new ArrayList<Channel>();
    private List<Radio> radios = new ArrayList<Radio>();

    private ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiogroup);
        mContext = getApplicationContext();
        mActivity = RadioGroupActivity.this;
        CognitoClientManager.init(mActivity);
        CognitoSyncClientManager.init(this);
        final CacheData cacheData = new CacheData(mContext);
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
        if (cacheData.getString("idRadio").equals("")){
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Escolha a estação");
            if (!cacheData.getString("color").equals("")){
                toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
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
            toolbar.setVisibility(View.VISIBLE);
            showWaitDialog("Carregando estações...");
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
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    closeWaitDialog();

                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    adapter = new RadioGroupListAdapter(mContext, getData());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

                    // RECUPERANDO DADOS DA RÁDIO ESCOLHIDA
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(mContext, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                                @Override public void onItemClick(View view, int position) {
                                    final int itemPosition = recyclerView.getChildLayoutPosition(view);
                                    cacheData.putString("idRadio", radios.get(itemPosition).getId());
                                    System.out.println(itemPosition);
                                    showWaitDialog("Carregando rádio...");

                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {

                                            ClientConfiguration clientConfiguration = new ClientConfiguration();
                                            CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                                            userPool.getCurrentUser().getSession(authenticationHandler);

                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void result) {
                                            super.onPostExecute(result);

                                        }
                                    }.execute();
                                }

                                @Override public void onLongItemClick(View view, int position) {

                                }
                            })
                    );
                }
            }.execute();
        } else {
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            CognitoUserPool userPool = new CognitoUserPool(mContext, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
            userPool.getCurrentUser().getSession(authenticationHandler);
        }
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
                    Log.i("Radio Controle", "userID = " + userID);

                    Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");
                    profileData.synchronize(new Dataset.SyncCallback() {
                        @Override
                        public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                            Log.d("Radio Controle", "Sync success" + dataset);
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
                            Log.d("Radio Controle", "Conflict");
                            return false;
                        }

                        @Override
                        public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                            Log.d("Radio Controle", "Dataset deleted");
                            return false;
                        }

                        @Override
                        public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                            Log.d("Radio Controle", "Datasets merged");
                            return false;
                        }

                        @Override
                        public void onFailure(DataStorageException dse) {
                            Log.e("Radio Controle", "Sync fails", dse);
                        }
                    });

                    ApiClientFactory factory = new ApiClientFactory();
                    factory.credentialsProvider(CognitoClientManager.getCredentials());
                    factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                    final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                    CacheData cacheData = new CacheData(getApplicationContext());
                    Settings settings = client.radioIdGet(cacheData.getString("idRadio"));

                    // COR PRINCIPAL
                    String [] parts = settings.getColor().toString().split(",");
                    String hex = toHex(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

                    // RECUPERANDO DADOS

                    cacheData.putString("rssLink", settings.getRss().toString());
                    cacheData.putString("color", hex);
                    cacheData.putString("logo", "https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/app/" + settings.getLogo());
                    cacheData.putString("splash", "https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/app/" + settings.getSplash());
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
                    Intent intent = new Intent(RadioGroupActivity.this, SplashActivity.class);
                    intent.putExtra("canais", (Serializable) canais);
                    intent.putExtra("radios", (Serializable) radios);
                    startActivity(intent);
                    finish();
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
                    CacheData cacheData = new CacheData(getApplicationContext());
                    Settings settings = client.radioIdGet(cacheData.getString("idRadio"));

                    // COR PRINCIPAL
                    String [] parts = settings.getColor().toString().split(",");
                    String hex = toHex(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

                    // RECUPERANDO DADOS
                    cacheData.putString("rssLink", settings.getRss().toString());
                    cacheData.putString("color", hex);
                    cacheData.putString("logo", "https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/app/" + settings.getLogo());
                    cacheData.putString("splash", "https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/app/" + settings.getSplash());
                    cacheData.putString("anuncio", settings.getAnnouncement());

                    for (Channel channel: settings.getChannels()){
                        canais.add(channel);
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
                    Intent intent = new Intent(RadioGroupActivity.this, SplashActivity.class);
                    intent.putExtra("canais", (Serializable) canais);
                    intent.putExtra("radios", (Serializable) radios);
                    startActivity(intent);
                    finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeWaitDialog();
    }

    public List<RadioApp> getData() {
        for (Radio radio : radios){
            RadioApp radioApp = new RadioApp();
            radioApp.setIdRadio(radio.getId());
            radioApp.setUrlImagemRadio(radio.getLogo());
            radioApp.setNomeRadio(radio.getName());
            radioApp.setLocalizacaoRadio(radio.getLocation());
            radioApps.add(radioApp);
        }
        return radioApps;
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(mActivity);
        waitDialog.setMessage(message);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }
}


