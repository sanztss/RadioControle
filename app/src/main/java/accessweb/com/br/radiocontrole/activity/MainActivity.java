package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;

import accessweb.com.br.radiocontrole.R;
import wseemann.media.FFmpegMediaMetadataRetriever;

import static accessweb.com.br.radiocontrole.R.id.playPauseToolbar;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private Toolbar pToolbar;
    private FragmentDrawer drawerFragment;
    private Context mContext;
    private SlidingUpPanelLayout painel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        ///////////////////////
        ///     Toolbar     ///
        ///////////////////////
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Início");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //////////////////////////////
        ///     FragmentDrawer     ///
        //////////////////////////////
        drawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);


        ////////////////////////////////////
        ///     SlidingUpPanelLayout     ///
        ////////////////////////////////////
        painel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        painel.getChildAt(1).setOnClickListener(null);
        painel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
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

                Log.v("back", "" + item.getItemId());
                if(item.getItemId() == R.id.action_canais){
                    final ModalListFragment dialogListCanais = new ModalListFragment("Selecione o canal desejado:", "canais");
                    dialogListCanais.show(getSupportFragmentManager(), "test");

                }else if (item.getItemId() == R.id.action_dormir){
                    final ModalListFragment dialogListDormir = new ModalListFragment("Selecione o tempo para desligar automaticamente:", "dormir");
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

    /////////////////////////////////////
    ///     Métodos Home Fragment     ///
    /////////////////////////////////////
    public void abrirPainel(View view) {
        Log.v("view", "Player toolbar");
        painel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void playPauseStreaming(View view) {
        Log.v("aaa", ""+ view.getId());
        if (!audioTocando) {
            changeIcon("pause");
        } else {
            changeIcon("play");
        }

    }

    private void changeIcon(String acao) {
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("inicio");
        Log.v("aaa", ""+ homeFragment);
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

            //String url = "http://files.freemusicarchive.org/music%2FccCommunity%2FThe_Kyoto_Connection%2FWake_Up%2FThe_Kyoto_Connection_-_09_-_Hachiko_The_Faithtful_Dog.mp3";
            String url = "http://stream.hostpg.com.br:8170";
            FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
            try {
                mmr.setDataSource(url);
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
            }



            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            try {
                mediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();

            imgBtnPlayPause.setImageResource(R.drawable.ic_pause_white);
            playPauseToolbar.setImageResource(R.drawable.ic_pause_gray);
            if (homeFragment != null)
                homeFragment.changeIcon(acao);
        }
    }

    public void abrirUrlIntent(View view) {
        Log.v("intent", "" + view.getTag());
        //view.get
    }


    /////////////////////////////////////////
    ///     Métodos Notícias Fragment     ///
    /////////////////////////////////////////


}