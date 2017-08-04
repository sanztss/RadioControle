package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.RadioGroupListAdapter;
import accessweb.com.br.radiocontrole.model.Channel;
import accessweb.com.br.radiocontrole.model.Programs;
import accessweb.com.br.radiocontrole.model.Promotions;
import accessweb.com.br.radiocontrole.model.Radio;
import accessweb.com.br.radiocontrole.model.RadioApp;
import accessweb.com.br.radiocontrole.model.Settings;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;

import static accessweb.com.br.radiocontrole.activity.SplashActivity.toHex;

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

        // RECUPERANDO LISTA DE CANAIS DA SPLASH ACTIVITY
        Bundle extras = getIntent().getExtras();
        canais = (List<Channel>) extras.getSerializable("canais");
        radios = (List<Radio>) extras.getSerializable("radios");

        final CacheData cacheData = new CacheData(mContext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Escolha a estação");
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new RadioGroupListAdapter(mContext, getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(mContext, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    final int itemPosition = recyclerView.getChildLayoutPosition(view);
                    System.out.println(itemPosition);
                    showWaitDialog("Carregando rádio...");
                    //CacheData cacheData = new CacheData(getApplicationContext());

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            ApiClientFactory factory = new ApiClientFactory();
                            factory.credentialsProvider(CognitoClientManager.getCredentials());
                            factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                            final RadiocontroleClient client = factory.build(RadiocontroleClient.class);

                            Settings settings = client.radioIdGet(radios.get(itemPosition).getId());

                            Promotions promotions = client.radioIdPromotionsGet(radios.get(itemPosition).getId());

                            Log.e("AAAAASSSSSS", "" + promotions.size());
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
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);

                            cacheData.putString("idRadio", radioApps.get(itemPosition).getIdRadio());
                            cacheData.putString("nomeRadio", radioApps.get(itemPosition).getNomeRadio());
                            Intent intent = new Intent(RadioGroupActivity.this, MainActivity.class);
                            intent.putExtra("canais", (Serializable) canais);
                            intent.putExtra("radios", (Serializable) radios);
                            startActivity(intent);
                            finish();
                        }
                    }.execute();
                }

                @Override public void onLongItemClick(View view, int position) {

                }
            })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeWaitDialog();
    }

    public List<RadioApp> getData() {
        CacheData cacheData = new CacheData(mContext);
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


