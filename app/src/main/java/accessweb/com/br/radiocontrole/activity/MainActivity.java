package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import accessweb.com.br.radiocontrole.util.ActivityResultBus;
import accessweb.com.br.radiocontrole.util.ActivityResultEvent;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.CacheData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private MediaPlayer mediaPlayer;
    private ImageView albumCoverToolbar;
    private ImageView imgCapaAlbum;
    private TextView nomeMusica;
    private TextView txtNomeMusica;
    private TextView nomeArtista;
    private TextView txtNomeCantor;

    private List<Channel> canais = new ArrayList<Channel>();
    private int indexCanal = 0;

    private ConnectivityManager connManager;
    private NetworkInfo mWifi;
    private NetworkInfo mMobile;
    private String urlStreaming =  "";

    MediaPlayer myMediaPlayer = null;


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

        CacheData cacheData = new CacheData(getApplicationContext());

        // RECUPERANDO LISTA DE CANAIS DA SPLASH ACTIVITY
        Bundle extras = getIntent().getExtras();
        canais = (List<Channel>) extras.getSerializable("canais");

        // INICIANDO COGNITO
        initCognito();

        ///////////////////////
        ///     Toolbar     ///
        ///////////////////////
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
        displayView(0);


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

    }

    private void initCognito() {
        CognitoClientManager.init(this);
    }

    @Override
    public void onBackPressed() {
        if(isDrawerOpen()) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
                    final EscolherDialogFragment dialogListCanais = new EscolherDialogFragment("Selecione o canal desejado:", "canais");
                    dialogListCanais.show(getSupportFragmentManager(), "test");

                }else if (item.getItemId() == R.id.action_dormir){
                    final EscolherDialogFragment dialogListDormir = new EscolherDialogFragment("Selecione o tempo para desligar automaticamente:", "dormir");
                    dialogListDormir.show(getSupportFragmentManager(), "test");
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
                    //Log.i("test");
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

        displayView(position);

    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String tag = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = "Início";
                tag = "inicio";
                break;
            case 1:
                fragment = new NoticiasFragment();
                title = "Notícias";
                tag = "noticias";
                break;
            case 2:
                fragment = new MuralFragment();
                title = "Mural";
                tag = "mural";
                break;
            case 3:
                fragment = new ProgramacaoFragment();
                title = "Programação";
                tag = "programacao";
                break;
            case 4:
                SharedPreferences sharedPrefs  = getSharedPreferences("UserData", 0);
                System.out.println("sashauisha" + sharedPrefs.getString("userNome",""));
                if (!sharedPrefs.contains("userId") || sharedPrefs.getString("userId","").equals("")){
                    System.out.println("Usuário deslogado!!!");
                    SuaContaDialogFragment dialog = new SuaContaDialogFragment();
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
            case 5:
                fragment = new PodcastsFragment();
                title = "Podcasts";
                tag = "podcasts";
                break;
            case 6:
                fragment = new PromocoesFragment();
                title = "Promoções";
                tag = "promocoes";
                break;
            case 7:
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
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
            if (position == 0 && audioTocando) {
                changeIcon("pause");
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
            changeIcon("pause");
        } else {
            changeIcon("play");
        }

    }

    // ALTERAR ÍCONE PLAY PAUSE
    private void changeIcon(final String acao) {
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("inicio");
        if (acao.equals("play")){
            audioTocando = false;
            mediaPlayer.stop();
            albumCoverToolbar.setImageResource(R.drawable.album_cover);
            imgCapaAlbum.setImageResource(R.drawable.album_cover);
            nomeMusica.setText("Pausado");
            txtNomeMusica.setText("Pausado");
            nomeArtista.setText("");
            txtNomeCantor.setText("");
            imgBtnPlayPause.setImageResource(R.drawable.ic_play_white);
            playPauseToolbar.setImageResource(R.drawable.ic_play_gray);
            if (homeFragment != null)
                homeFragment.changeIcon(acao);
        }else if (acao.equals("pause")) {
            audioTocando = true;
            if (indexCanal == 0){
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
                    customBuilderWhatstapp .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialogWhatsapp = customBuilderWhatstapp.create();

                    dialogWhatsapp.show();
                    CacheData cacheData = new CacheData(mContext);
                    Button btnPositiveWhatsapp = dialogWhatsapp.getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPositiveWhatsapp.setTextColor(Color.parseColor(cacheData.getString("color")));
                }

            }

            /*FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
            Log.e("urlStreaming", "" + urlStreaming);
            try {
                mmr.setDataSource(urlStreaming);
            }catch (Exception e){

            }
            byte [] artwork = mmr.getEmbeddedPicture();
            String artist = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
            String album = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
            String title = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
            mmr.release();
            if (artwork != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
                albumCoverToolbar.setImageBitmap(bitmap);
                imgCapaAlbum.setImageBitmap(bitmap);
            } else {
                albumCoverToolbar.setImageResource(R.drawable.album_cover);
                imgCapaAlbum.setImageResource(R.drawable.album_cover);
            }

            if (artist != null){
                nomeMusica.setText(title);
                txtNomeMusica.setText(title);
                nomeArtista.setText(artist);
                txtNomeCantor.setText(artist);
            } else {
                nomeMusica.setText("Desconhecido");
                txtNomeMusica.setText("Desconhecido");
                nomeArtista.setText("Desconhecido");
                txtNomeCantor.setText("Desconhecido");
            }*/

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

            /*MediaPlayer myMediaPlayer = new MediaPlayer();
            myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                myMediaPlayer.setDataSource(urlStreaming);
                myMediaPlayer.prepareAsync();

            } catch (IOException e) {
                Toast.makeText(this, "mp3 not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer player) {
                    player.start();
                    imgBtnPlayPause.setImageResource(R.drawable.ic_pause_white);
                    playPauseToolbar.setImageResource(R.drawable.ic_pause_gray);
                    if (homeFragment != null)
                        homeFragment.changeIcon(acao);
                }

            });

            myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer player) {
                    player.release();
                }
            });*/
        }
    }


    @Override
    public void onPrepared(MediaPlayer myMediaPlayer) {
        Log.d(TAG, "Stream is prepared");
        myMediaPlayer.start();
        imgBtnPlayPause.setImageResource(R.drawable.ic_pause_white);
        playPauseToolbar.setImageResource(R.drawable.ic_pause_gray);
        if (homeFragment != null)
            homeFragment.changeIcon("play");
    }

    private void pause() {
        myMediaPlayer.pause();
    }

    private void stop() {
        myMediaPlayer.stop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();

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
    public void fecharPerfil() {
        Fragment fragment = null;
        fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment, "inicio");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        if (audioTocando) {
            changeIcon("pause");
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
        fragmentTransaction.replace(R.id.container_body, fragment, "inicio");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
    }

}