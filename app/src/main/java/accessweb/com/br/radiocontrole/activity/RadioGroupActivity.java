package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.RadioGroupListAdapter;
import accessweb.com.br.radiocontrole.model.Channel;
import accessweb.com.br.radiocontrole.model.Radio;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;

import static android.R.attr.data;

/**
 * Created by Des. Android on 19/07/2017.
 */

public class RadioGroupActivity extends Activity {

    private Context mContext;
    private Activity mActivity;
    private RecyclerView recyclerView;
    private RadioGroupListAdapter adapter;
    private List<Radio> radios = new ArrayList<>();

    private List<Channel> canais = new ArrayList<Channel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiogroup);
        mContext = getApplicationContext();
        mActivity = RadioGroupActivity.this;

        // RECUPERANDO LISTA DE CANAIS DA SPLASH ACTIVITY
        Bundle extras = getIntent().getExtras();
        canais = (List<Channel>) extras.getSerializable("canais");

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
                    int itemPosition = recyclerView.getChildLayoutPosition(view);
                    System.out.println(itemPosition);
                    //CacheData cacheData = new CacheData(getApplicationContext());
                    cacheData.putString("idRadio", radios.get(itemPosition).getIdRadio());
                    Intent intent = new Intent(RadioGroupActivity.this, MainActivity.class);
                    intent.putExtra("canais", (Serializable) canais);
                    startActivity(intent);
                    finish();
                }

                @Override public void onLongItemClick(View view, int position) {

                }
            })
        );
    }

    public List<Radio> getData() {
        CacheData cacheData = new CacheData(mContext);
        for (int i = 0; i < 5; i++) {
            Radio radio = new Radio();
            radio.setIdRadio("tradicaoAM");
            radio.setUrlImagemRadio(cacheData.getString("logo"));
            radio.setNomeRadio("Tradição AM");
            radio.setLocalizacaoRadio(" Rio Branco do Sul - PR");
            radios.add(radio);
        }
        return radios;
    }
}


