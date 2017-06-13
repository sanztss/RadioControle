package accessweb.com.br.radiocontrole.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.ViewGroup.LayoutParams;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.ChannelAdapter;
import accessweb.com.br.radiocontrole.model.ChannelItem;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private Toolbar pToolbar;
    private FragmentDrawer drawerFragment;
    private Context mContext;
    private SlidingUpPanelLayout painel;
    private TextView txtRotativo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        ///////////////////////
        ///     Toolbar     ///
        ///////////////////////
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    Toast.makeText(getApplicationContext(), "Abrir lista de canais!", Toast.LENGTH_SHORT).show();
                }else if (item.getItemId() == R.id.action_dormir){
                    Toast.makeText(getApplicationContext(), "Abrir lista de tempo!", Toast.LENGTH_SHORT).show();
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
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new FriendsFragment();
                title = getString(R.string.title_noticias);
                break;
            case 2:
                fragment = new MessagesFragment();
                title = getString(R.string.title_mural);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public void abrirPainel(View view) {
        Log.v("view", "Player toolbar");
        painel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void playPauseButton(View view) {
        Log.v("view", "Botão play/pause");
    }
}