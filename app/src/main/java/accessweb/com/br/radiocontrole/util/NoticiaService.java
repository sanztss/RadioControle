package accessweb.com.br.radiocontrole.util;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import accessweb.com.br.radiocontrole.adapter.NoticiasRetrofitAdapter;
import accessweb.com.br.radiocontrole.model.Noticia;
import accessweb.com.br.radiocontrole.model.NoticiaFeed;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Des. Android on 16/06/2017.
 */

public class NoticiaService extends Service {

    private static final String RSS_LINK = "http://g1.globo.com/"; // http://g1.globo.com/ http://feeds.feedburner.com/TechCrunch/social/
    public static final String ITEMS = "items";
    public static final String RECEIVER = "receiver";
    private final static String TAG = "NoticiaService";
    private final static int UPDATE_INTERVAL_MIN = 60;
    List<Noticia> cachedList = null;
    Intent mIntent;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.d(TAG, "Service started");
        mIntent = intent;
        sendCachedList();
        updateRssItems();
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60 * UPDATE_INTERVAL_MIN),
                PendingIntent.getService(getApplicationContext(), 0, new Intent(this, NoticiaService.class), 0)
        );
        return Service.START_STICKY;
    }

    void updateRssItems( ) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

       OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        SimpleXmlConverterFactory conv = SimpleXmlConverterFactory.createNonStrict();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RSS_LINK)
                .client(client)
                .addConverterFactory(conv)
                .build();


        NoticiasRetrofitAdapter retrofitService = retrofit.create(NoticiasRetrofitAdapter.class);
        Call<NoticiaFeed> call = retrofitService.getItems();
        call.enqueue(new Callback<NoticiaFeed>() {
            @Override
            public void onResponse(Call<NoticiaFeed> call, Response<NoticiaFeed> response) {
                NoticiaFeed feed = response.body();
                List<Noticia> mItems = feed.getChannel().getItemList();
                // This is first time activity is connecting to service
                // so initialized cachedList
                if ( cachedList == null && mItems != null) {
                    cachedList = new ArrayList<Noticia>(mItems);
                    Log.d(TAG, "Initialized  cached list");
                    sendCachedList();
                } else if ( mItems != null ) {
                    // Prepend mItems to cachedList if they don't exists
                    // I know this n^2 complexity there has to be better
                    // way to merge these two list for now this will suffice.
                    boolean itemsUpdated = false;
                    for ( int k = mItems.size() -1 ; k >= 0 ; k--) {
                        Noticia item = mItems.get(k);
                        boolean itemExists = false;
                        for (Noticia i: cachedList) {
                            /*if (i.isEqualTo(item)) {
                                itemExists = true;
                                break;
                            }*/
                        }
                        if (!itemExists) {
                            itemsUpdated = true;
                            Log.d(TAG, "Found a new item " + item.getTituloNoticia());
                            cachedList.add(0, item);
                        }
                    }
                    if (itemsUpdated) {
                        Log.d(TAG, "Finished updating cached list");
                        sendCachedList();
                    } else {
                        Log.d(TAG,"No updates to cache no need to send an update");
                    }
                }
            }

            @Override
            public void onFailure(Call<NoticiaFeed> call, Throwable t) {
                Log.d(TAG, "OnFailure Error is " + t);
            }
        });
    }

    void sendCachedList( ) {
        if ( cachedList != null ) {
            Log.d(TAG, "Sending cachedList");
            Bundle bundle = new Bundle();
            bundle.putSerializable(ITEMS, (Serializable) cachedList);
            ResultReceiver receiver = mIntent.getParcelableExtra(RECEIVER);
            if (receiver != null)
                receiver.send(0, bundle);
        }
        else {
            Log.d(TAG, "Cached list is empty!");
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        mIntent = intent;
        Log.d(TAG, "Bound to service");

        return null;
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "Service is destroyed");
    }
}
