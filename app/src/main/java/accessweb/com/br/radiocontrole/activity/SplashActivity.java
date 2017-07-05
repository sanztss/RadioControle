package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Channel;
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
    private static int SPLASH_TIME_OUT = 3000;

    private ArrayList<String> modulos = new ArrayList<String>();
    private List<Channel> canais = new ArrayList<Channel>();

    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = getApplicationContext();
        mActivity = SplashActivity.this;

        CognitoClientManager.init(this);

        splash = (ImageView) findViewById(R.id.splash);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                Settings settings = client.radioIdGet("tradicaoAM");

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
                cacheData.putListString("modulos", modulos);

                Log.v("aaaaaaaaaaaaaaaaaaaa", "Nome:" + settings.getChannels().get(0).getName() + ", Rds:" + settings.getChannels().get(0).getRds() + ", Principal:" + settings.getChannels().get(0).getMain() + ", LowStreams:" + settings.getChannels().get(0).getLowStreams() + ", HighStreams:" + settings.getChannels().get(0).getHighStreams());
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                CacheData cacheData = new CacheData(mContext);
                Picasso.with(mContext)
                        .load(cacheData.getString("splash"))
                        .into(splash);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("canais", (Serializable) canais);
                        startActivity(intent);
                        finish();
                    }
                }, SPLASH_TIME_OUT);

            }
        }.execute();
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

}