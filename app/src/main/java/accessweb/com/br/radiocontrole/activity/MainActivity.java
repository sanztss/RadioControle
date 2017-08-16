package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;
import com.amazonaws.regions.Regions;
import com.ampiri.sdk.Ampiri;
import com.ampiri.sdk.banner.BannerAd;
import com.ampiri.sdk.banner.BannerAdPool;
import com.ampiri.sdk.listeners.BannerAdCallback;
import com.ampiri.sdk.mediation.BannerSize;
import com.ampiri.sdk.mediation.ResponseStatus;
import com.facebook.FacebookSdk;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.dialog.EscolherDialogFragment;
import accessweb.com.br.radiocontrole.dialog.SobreDialogFragment;
import accessweb.com.br.radiocontrole.dialog.SuaContaDialogFragment;
import accessweb.com.br.radiocontrole.fragment.FragmentDrawer;
import accessweb.com.br.radiocontrole.fragment.HomeFragment;
import accessweb.com.br.radiocontrole.fragment.MuralFragment;
import accessweb.com.br.radiocontrole.fragment.NoticiasFragment;
import accessweb.com.br.radiocontrole.fragment.PerfilFragment;
import accessweb.com.br.radiocontrole.fragment.PodcastsFragment;
import accessweb.com.br.radiocontrole.fragment.ProgramacaoFragment;
import accessweb.com.br.radiocontrole.fragment.PromocoesFragment;
import accessweb.com.br.radiocontrole.model.Channel;
import accessweb.com.br.radiocontrole.model.Radio;
import accessweb.com.br.radiocontrole.util.ActivityResultBus;
import accessweb.com.br.radiocontrole.util.ActivityResultEvent;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.CacheData;

import static accessweb.com.br.radiocontrole.R.id.areaAnuncio;
import static accessweb.com.br.radiocontrole.R.id.imagemAnuncio;
import static accessweb.com.br.radiocontrole.R.id.relativeLogo;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, OnBufferingUpdateListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private Toolbar pToolbar;
    private FragmentDrawer drawerFragment;
    private DrawerLayout drawerLayout;
    private Context mContext;
    private SlidingUpPanelLayout painel;
    private RelativeLayout dragView;
    private TextView txtRotativo;
    private Activity mActivity;
    private ImageButton imgBtnPlayPause;
    private ImageButton playPauseToolbar;
    private HomeFragment homeFragment;
    private Boolean audioTocando = false;
    private ImageView albumCoverToolbar;
    private ImageView imgCapaAlbum;
    private TextView nomeMusica;
    private TextView txtNomeMusica;
    private TextView nomeArtista;
    private TextView txtNomeCantor;
    private BannerAd bannerAd;
    private LinearLayout areaAnuncio;
    private ImageView imagemAnuncio;
    private List<Channel> canais = new ArrayList<Channel>();
    private int indexCanal = 0;

    private List<Radio> radios = new ArrayList<Radio>();

    private ConnectivityManager connManager;
    private NetworkInfo mWifi;
    private NetworkInfo mMobile;
    private String urlStreaming =  "";

    MediaPlayer myMediaPlayer = null;

    private Boolean firstTime = true;

    ProgressDialog progressDialog;

    private static ArrayList<String> modulos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        final CacheData cacheData = new CacheData(getApplicationContext());

        // RECUPERANDO LISTA DE CANAIS DA SPLASH ACTIVITY
        Bundle extras = getIntent().getExtras();
        canais = (List<Channel>) extras.getSerializable("canais");
        radios = (List<Radio>) extras.getSerializable("radios");

        // INICIANDO COGNITO
        initCognito();

        ///////////////////////
        ///     Toolbar     ///
        ///////////////////////

        if (Build.VERSION.SDK_INT > 23) {
            float[] hsv = new float[3];
            int color = Color.parseColor(cacheData.getString("color"));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);

            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Início");

        mToolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //////////////////////////////
        ///     FragmentDrawer     ///
        //////////////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView("inicio");


        ////////////////////////////////////
        ///     SlidingUpPanelLayout     ///
        ////////////////////////////////////

        dragView = (RelativeLayout) findViewById(R.id.dragView);
        dragView.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        painel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        painel.getChildAt(1).setOnClickListener(null);
        painel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        painel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            painel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        //////////////////////////////
        ///     Texto Rotativo     ///
        //////////////////////////////
        txtRotativo = (TextView) this.findViewById(R.id.txtRotativo);
        txtRotativo.setText(cacheData.getString("anuncio"));
        txtRotativo.setSelected(true);

        //////////////////////
        ///     Player     ///
        //////////////////////

        imgBtnPlayPause = (ImageButton) findViewById(R.id.imgBtnPlayPause);
        playPauseToolbar =(ImageButton) findViewById(R.id.playPauseToolbar);
        albumCoverToolbar = (ImageView) findViewById(R.id.albumCoverToolbar);
        imgCapaAlbum = (ImageView) findViewById(R.id.albumCoverToolbar);
        nomeMusica = (TextView) findViewById(R.id.nomeMusica);
        txtNomeMusica = (TextView) findViewById(R.id.txtNomeMusica);
        nomeArtista = (TextView) findViewById(R.id.nomeArtista);
        txtNomeCantor = (TextView) findViewById(R.id.txtNomeCantor);

        painel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        Ampiri.setDebugMode(true);
        Ampiri.setTestMode(true);

        areaAnuncio = (LinearLayout) findViewById(R.id.areaAnuncio);
        imagemAnuncio = (ImageView) findViewById(R.id.imagemAnuncio);
        if (cacheData.getString("adsPlayerContent").equals("off")){
            areaAnuncio.setVisibility(View.GONE);
        }else if (cacheData.getString("adsPlayerContent").equals("ampiri")) {
            BannerAdCallback bannerAdListener = new BannerAdCallback() {
                @Override
                public void onAdLoaded(@NonNull BannerAd ad) {

                }

                @Override
                public void onAdFailed(@NonNull BannerAd AD, @NonNull ResponseStatus responseStatus) {

                }

                @Override
                public void onAdOpened(@NonNull BannerAd ad) {

                }

                @Override
                public void onAdClicked(@NonNull BannerAd ad) {

                }

                @Override
                public void onAdClosed(@NonNull BannerAd ad) {

                }
            };
            FrameLayout adView = (FrameLayout) findViewById(R.id.ad_view);
            adView.setVisibility(View.VISIBLE);
            //bannerAd = BannerAdPool.load(this, "34e3be38-adb1-493b-8eec-5c20a60d0f56", adView, BannerSize.BANNER_SIZE_320x50, bannerAdListener);
            bannerAd = BannerAdPool.load(this, cacheData.getString("adsPlayerValue"), adView, BannerSize.BANNER_SIZE_320x50, bannerAdListener);
        } else if (cacheData.getString("adsPlayerContent").equals("image")) {
            imagemAnuncio.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(cacheData.getString("adsPlayerValue")).into(imagemAnuncio);
            imagemAnuncio.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(cacheData.getString("adsHomeLink")));
                        startActivity(intentContato);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private void initCognito() {
        CognitoClientManager.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bannerAd.onActivityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bannerAd.onActivityPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerAd.onActivityDestroyed();
    }

    @Override
    public void onBackPressed() {
        if(isDrawerOpen()) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(painel.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
            painel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            // Get the fragment fragment manager - and pop the backstack
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++){
                System.out.println("AAAAAAAAAAAAAA" +getSupportFragmentManager().getBackStackEntryCount());
                getSupportFragmentManager().popBackStack();
            }


        } else {
            super.onBackPressed();
        }

    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(
                new ActivityResultEvent(requestCode, resultCode, data));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        System.out.println("BBBBBBB" + requestCode);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    ////////////////////////////
    ///     Menu Toolbar     ///
    ////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        pToolbar = (Toolbar) findViewById(R.id.playerTopToolbar);
        pToolbar.setTitle("Player");
        pToolbar.inflateMenu(R.menu.player_top_menu);
        pToolbar.setNavigationIcon(R.drawable.ic_arrow_down_white);
        CacheData cacheData = new CacheData(getApplicationContext());
        pToolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        setSupportActionBar(pToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                painel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });

        pToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId() == R.id.action_canais){
                    final EscolherDialogFragment dialogListCanais = new EscolherDialogFragment("Selecione o canal desejado:", "canais", canais);
                    dialogListCanais.show(getSupportFragmentManager(), "abrirTelaDesejada");

                }else if (item.getItemId() == R.id.action_dormir){
                    final EscolherDialogFragment dialogListDormir = new EscolherDialogFragment("Selecione o tempo para desligar automaticamente:", "dormir", null);
                    dialogListDormir.show(getSupportFragmentManager(), "abrirTelaDesejada");
                }

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if(id == R.id.action_canal){
            //Toast.makeText(getApplicationContext(), "Abrir lista de canais!", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            String titleText = "Selecione o canal desejado:";
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.0f);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(titleText);
            spannableStringBuilder.setSpan(
                    relativeSizeSpan,
                    0,
                    1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT
            );
            LinearLayout LLayout = new LinearLayout(mContext);
            LLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(15,15,15,15);
            LLayout.setLayoutParams(params);
            LLayout.setPadding(15,15,15,15);
            TextView tv_title = new TextView(mContext);
            tv_title.setLayoutParams(params);
            tv_title.setTextColor(Color.parseColor("#0074c8"));
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            tv_title.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_title.setText(spannableStringBuilder);
            LLayout.addView(tv_title);

            alertBuilder.setCustomTitle(LLayout);

            ChannelItem weather_data[] = new ChannelItem[] {
                new ChannelItem("Sertanejo", R.drawable.ic_library_music_gray),
                new ChannelItem("MPB", R.drawable.ic_library_music_gray),
            };

            View view = getLayoutInflater().inflate(R.layout.dialog_list, null);

            ListView lv = (ListView) view.findViewById(R.id.custom_list);

            final ChannelAdapter arrayAdapter = new ChannelAdapter(this, R.layout.channel_row ,weather_data);

            alertBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //final TextView txtNome = (TextView) view.findViewById(R.id.txtNome);
                    Toast.makeText(getApplicationContext(), "On click selected ", Toast.LENGTH_SHORT) .show();
                    dialog.dismiss();
                }
            });


            final AlertDialog alertDialog = alertBuilder.create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    ListView listView = alertDialog.getListView();
                    //Log.i("abrirTelaDesejada");
                    *//*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(
                                AdapterView<?> parent, View view,
                                int position, long id) {
                            // TODO Auto-generated method stub
                            //String strOS = arrayAdapter.getItem(position);
                            *//**//*Toast.makeText(getApplicationContext(),
                                    "Long Press - Deleted Entry " + strOS,
                                    Toast.LENGTH_SHORT).show();*//**//*
                            alertDialog.dismiss();
                            return true;
                        }
                    });*//*
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View view, int pos, long id) {
                            Toast.makeText(getApplicationContext(),
                                    "Long Press - Deleted Entry ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            alertDialog.show();


            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));


            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////
    ///     Menu Principal Click     ///
    ////////////////////////////////////
    @Override
    public void onDrawerItemSelected(View view, int position) {
        System.out.println(view.getTag().toString());
        displayView(view.getTag().toString());

    }

    private void displayView(String tela) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String tag = null;
        final CacheData cacheData = new CacheData(mContext);
        switch (tela) {
            case "inicio":
                fragment = new HomeFragment();
                title = "Início";
                tag = "inicio";
                break;
            case "noticias":
                fragment = new NoticiasFragment();
                title = "Notícias";
                tag = "noticias";
                break;
            case "mural":
                fragment = new MuralFragment();
                title = "Mural";
                tag = "mural";
                break;
            case "programas":
                fragment = new ProgramacaoFragment();
                title = "Programação";
                tag = "programacao";
                break;
            case "perfil":
                System.out.println("sashauisha" + cacheData.getString("userId"));
                if (cacheData.getString("userId").equals("")){
                    System.out.println("Usuário deslogado!!!");
                    SuaContaDialogFragment dialog = new SuaContaDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tela", "perfil");
                    dialog.setArguments(bundle);
                    dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    //dialog.show(this.getSupportFragmentManager(), "dialog");
                    ft.add(dialog,"fragment_dialog");
                    ft.commit();
                }else {
                    System.out.println("Usuário logado!!!");
                    fragment = new PerfilFragment();
                    title = "Perfil";
                    tag = "perfil";
                }
                break;
            case "podcasts":
                fragment = new PodcastsFragment();
                title = "Podcasts";
                tag = "podcasts";
                break;
            case "promocoes":
                fragment = new PromocoesFragment();
                title = "Promoções";
                tag = "promocoes";
                break;
            case "estacoes":
                Intent intent = new Intent(MainActivity.this, RadioGroupActivity.class);
                intent.putExtra("canais", (Serializable) canais);
                intent.putExtra("radios", (Serializable) radios);
                cacheData.putString("idRadio", "");
                startActivity(intent);
                finish();
                break;
            case "sobre o aplicativo":
                SobreDialogFragment dialog = new SobreDialogFragment();
                dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(dialog,"fragment_dialog");
                ft.commit();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, tag);

            if (!tela.equals("inicio")){
                fragmentTransaction.addToBackStack(tag);
            }
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
            if (tela.equals("inicio") && audioTocando) {
                changeIcon("pause", indexCanal);
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(title);
        }

    }

    ///////////////////////
    ///     Métodos     ///
    ///////////////////////

    // ABRIR SLIDE UP
    public void abrirPainel(View view) {
        //Log.v("view", "Player toolbar");
        painel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    // PLAY PAUSE STREAMING
    public void playPauseStreaming(View view) {
        //Log.v("aaa", ""+ view.getId());
        if (!audioTocando) {
            changeIcon("play", indexCanal);
        } else {
            changeIcon("pause", indexCanal);
        }
    }

    // ALTERAR ÍCONE PLAY PAUSE
    public void changeIcon(final String acao, int index) {
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("inicio");
        if (acao.equals("pause")){
            audioTocando = false;
            stop();
            albumCoverToolbar.setImageResource(R.drawable.album_cover);
            imgCapaAlbum.setImageResource(R.drawable.album_cover);
            nomeMusica.setText("Parado");
            txtNomeMusica.setText("Parado");
            nomeArtista.setText("");
            txtNomeCantor.setText("");
            imgBtnPlayPause.setImageResource(R.drawable.ic_play_white);
            playPauseToolbar.setImageResource(R.drawable.ic_play_gray);
            if (homeFragment != null)
                homeFragment.changeIcon("play");
        }else if (acao.equals("play")) {

            if (index == 0){
                if (mWifi.isAvailable() == true) {
                    if (canais.get(0).getHighStreams().size() > 0){
                        urlStreaming = canais.get(0).getHighStreams().get(0);
                    }else {
                        urlStreaming = canais.get(0).getLowStreams().get(0);
                    }
                } else if (mMobile.isAvailable() == true) {
                    if (canais.get(0).getHighStreams().size() > 0){
                        urlStreaming = canais.get(0).getLowStreams().get(0);
                    }else {
                        urlStreaming = canais.get(0).getHighStreams().get(0);
                    }
                } else {
                    AlertDialog.Builder customBuilderWhatstapp  = new AlertDialog.Builder(mActivity);
                    customBuilderWhatstapp .setTitle("Rádio Controle");
                    customBuilderWhatstapp .setMessage("Você precisa estar conectado a internet para utilizar o aplicativo.");
                    customBuilderWhatstapp .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialogWhatsapp = customBuilderWhatstapp.create();

                    dialogWhatsapp.show();
                    CacheData cacheData = new CacheData(mContext);
                    Button btnPositiveWhatsapp = dialogWhatsapp.getButton(DialogInterface.BUTTON_NEUTRAL);
                    btnPositiveWhatsapp.setTextColor(Color.parseColor(cacheData.getString("color")));
                }

            } else {
                indexCanal = index;
                if (mWifi.isAvailable() == true) {
                    if (canais.get(index).getHighStreams().size() > 0){
                        urlStreaming = canais.get(index).getHighStreams().get(0);
                    }else {
                        urlStreaming = canais.get(index).getLowStreams().get(0);
                    }
                } else if (mMobile.isAvailable() == true) {
                    if (canais.get(0).getHighStreams().size() > 0){
                        urlStreaming = canais.get(index).getLowStreams().get(0);
                    }else {
                        urlStreaming = canais.get(index).getHighStreams().get(0);
                    }
                } else {
                    AlertDialog.Builder customBuilderWhatstapp  = new AlertDialog.Builder(mActivity);
                    customBuilderWhatstapp .setTitle("Rádio Controle");
                    customBuilderWhatstapp .setMessage("Você precisa estar conectado a internet para utilizar o aplicativo.");
                    customBuilderWhatstapp .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialogWhatsapp = customBuilderWhatstapp.create();

                    dialogWhatsapp.show();
                    CacheData cacheData = new CacheData(mContext);
                    Button btnPositiveWhatsapp = dialogWhatsapp.getButton(DialogInterface.BUTTON_NEUTRAL);
                    btnPositiveWhatsapp.setTextColor(Color.parseColor(cacheData.getString("color")));
                }
            }

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Carregando Áudio");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            Uri myUri = Uri.parse(urlStreaming);
            try {
                if (myMediaPlayer == null) {
                    this.myMediaPlayer = new MediaPlayer();
                } else {
                    myMediaPlayer.stop();
                    myMediaPlayer.reset();
                }
                myMediaPlayer.setDataSource(this, myUri); // Go to Initialized state
                myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                myMediaPlayer.setOnPreparedListener(this);
                myMediaPlayer.setOnBufferingUpdateListener(this);

                myMediaPlayer.setOnErrorListener(this);
                myMediaPlayer.prepareAsync();

                Log.d(TAG, "LoadClip Done");
            } catch (Throwable t) {
                Log.d(TAG, t.toString());
            }
        }
    }


    @Override
    public void onPrepared(MediaPlayer myMediaPlayer) {
        Log.d(TAG, "Stream is prepared");
        myMediaPlayer.start();
        progressDialog.dismiss();
        firstTime = false;
        audioTocando = true;
        albumCoverToolbar.setImageResource(R.drawable.album_cover);
        imgCapaAlbum.setImageResource(R.drawable.album_cover);
        nomeMusica.setText("Desconhecido");
        txtNomeMusica.setText("Desconhecido");
        nomeArtista.setText("Desconhecido");
        txtNomeCantor.setText("Desconhecido");
        imgBtnPlayPause.setImageResource(R.drawable.ic_stop_white);
        playPauseToolbar.setImageResource(R.drawable.ic_stop_gray);
        if (homeFragment != null)
            homeFragment.changeIcon("pause");
    }

    private void stop() {
        if (myMediaPlayer != null)
            myMediaPlayer.stop();
    }

    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        StringBuilder sb = new StringBuilder();
        sb.append("Media Player Error: ");
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                sb.append("Not Valid for Progressive Playback");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                sb.append("Server Died");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                sb.append("Unknown");
                break;
            default:
                sb.append(" Non standard (");
                sb.append(what);
                sb.append(")");
        }
        sb.append(" (" + what + ") ");
        sb.append(extra);
        Log.e(TAG, sb.toString());
        return true;
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d(TAG, "PlayerService onBufferingUpdate : " + percent + "%");
    }

    // FECHAR FRAGMENTO PERFIL
    public void voltarTelaInicial() {
        Fragment fragment = null;
        fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment, "inicio");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        if (audioTocando) {
            homeFragment.changeIcon("pause");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Início");
    }

    // ABRIR FRAGMENTO PERFIL
    public void abrirPerfil() {
        Fragment fragment = null;
        fragment = new PerfilFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment, "perfil");
        fragmentTransaction.addToBackStack("perfil");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        mToolbar.setTitle("Perfil");
    }

    public void trocarTituloToolbar(){
        mToolbar.setTitle("Início");
    }
}