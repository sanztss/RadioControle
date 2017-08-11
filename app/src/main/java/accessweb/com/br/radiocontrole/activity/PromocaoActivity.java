package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.PremiosAdapter;
import accessweb.com.br.radiocontrole.dialog.SuaContaDialogFragment;
import accessweb.com.br.radiocontrole.model.Participant;
import accessweb.com.br.radiocontrole.model.Premio;
import accessweb.com.br.radiocontrole.model.Promocao;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.CognitoSyncClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;

import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;

/**
 * Created by Des. Android on 04/08/2017.
 */

public class PromocaoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private Toolbar toolbar;
    private List<Promocao> promocao;
    private List<Premio> dataPremio;
    private int itemPosicao;

    private ImageView imagemPromocao;
    private ImageButton btnCompartilhar;
    private ImageButton btnVideoPromocao;
    private TextView tituloPromocao;
    private TextView dataEncerramentoPromocao;
    private TextView dataSorteioPromocao;
    private TextView participando;
    private Button btnRegulamento;
    private View spacerButton;
    private Button btnParticipar;
    private TextView txtPremios;
    private RecyclerView recyclerView;
    private PremiosAdapter adapter;

    private Context mContext;
    private Activity mActivity;

    private static final int RECOVERY_REQUEST = 1;
    private View youtube_fragment;

    private Boolean participandoCheck = false;
    private Boolean forCheck = false;
    private ArrayList<String> promocoesParticipantes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_promocao);

        mContext = getApplicationContext();
        mActivity = PromocaoActivity.this;

        Bundle args = getIntent().getExtras();
        promocao = (List<Promocao>) args.getSerializable("lista");
        itemPosicao = args.getInt("posicao");
        final CacheData cacheData = new CacheData(mContext);

        if (Build.VERSION.SDK_INT > 23) {
            float[] hsv = new float[3];
            int color = Color.parseColor(cacheData.getString("color"));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(color);
        }

        toolbar = (Toolbar) findViewById(R.id.editarPerfilToolbar);
        toolbar.setTitle("Promoção");
        toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        youtube_fragment = findViewById(R.id.youtube_fragment);
        imagemPromocao = (ImageView) findViewById(R.id.imagemPromocao);
        btnCompartilhar = (ImageButton) findViewById(R.id.btnCompartilhar);
        btnVideoPromocao = (ImageButton) findViewById(R.id.btnVideoPromocao);
        tituloPromocao = (TextView) findViewById(R.id.tituloPromocao);
        dataEncerramentoPromocao = (TextView) findViewById(R.id.dataEncerramentoPromocao);
        dataSorteioPromocao = (TextView) findViewById(R.id.dataSorteioPromocao);
        participando = (TextView) findViewById(R.id.participando);
        btnRegulamento = (Button) findViewById(R.id.btnRegulamento);
        btnParticipar = (Button) findViewById(R.id.btnParticipar);
        spacerButton = findViewById(R.id.spacerButton);
        txtPremios = (TextView) findViewById(R.id.txtPremios);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        promocoesParticipantes = cacheData.getListString("promocoesParticipantes");
        if (!cacheData.getString("userId").equals("")) {
            if (promocoesParticipantes.size()>0){
                for (String promocaoParticipante : promocoesParticipantes){
                    if (promocaoParticipante.equals(promocao.get(itemPosicao).getTimestamp()+"")){
                        forCheck = true;
                        break;
                    }
                }

                if (forCheck){
                    participando.setVisibility(View.VISIBLE);
                    btnParticipar.setVisibility(View.GONE);
                }else {
                    btnParticipar.setVisibility(View.VISIBLE);
                }
            }else {
                btnParticipar.setVisibility(View.VISIBLE);
            }
        }else {
            btnParticipar.setVisibility(View.VISIBLE);
        }

        Picasso.with(mContext)
                .load(promocao.get(itemPosicao).getImagemPromocao())
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(imagemPromocao);

        btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Compartilhar.");
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "No dia " + promocao.get(itemPosicao).getDataEncerramentoPromocao() + " encerrará a promoção " + promocao.get(itemPosicao).getTituloPromocao() + ", venha participar comigo, baixe já o aplicativo Rádio controle:\nVersão Android: https://play.google.com/store/apps/details?id=br.com.devmaker.radiocontroletest\nVersão iOS: https://itunes.apple.com/us/app/access-mobile-rádio-controle/id905070426?mt=8");
                sendIntent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(sendIntent,"Compartilhar no:" ));
            }
        });

        YouTubePlayerSupportFragment frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize("AIzaSyDPfAz7R_J7el29-2_HN6zmLGjAay1chX4", PromocaoActivity.this);
        btnVideoPromocao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão ver vídeo.");
                if (btnVideoPromocao.getTag().equals("imagem")){
                    btnVideoPromocao.setTag("video");
                    btnVideoPromocao.setImageResource(R.drawable.ic_file_image_gray);
                    youtube_fragment.setVisibility(View.VISIBLE);
                    imagemPromocao.setVisibility(View.GONE);
                }else {
                    btnVideoPromocao.setTag("imagem");
                    btnVideoPromocao.setImageResource(R.drawable.ic_video_gray);
                    youtube_fragment.setVisibility(View.GONE);
                    imagemPromocao.setVisibility(View.VISIBLE);
                }

            }
        });
        tituloPromocao.setText(promocao.get(itemPosicao).getTituloPromocao());
        dataEncerramentoPromocao.setText(promocao.get(itemPosicao).getDataEncerramentoPromocao());
        dataSorteioPromocao.setText(promocao.get(itemPosicao).getDataSorteioPromocao());
        /*if (promocao.get(itemPosicao).getParticipando()){
            participando.setVisibility(View.VISIBLE);
            btnParticipar.setVisibility(View.GONE);
        }else {
            participando.setVisibility(View.GONE);
            btnParticipar.setVisibility(View.VISIBLE);
        }*/
        if (!promocao.get(itemPosicao).getVigente()){
            btnParticipar.setVisibility(View.GONE);
            spacerButton.setVisibility(View.GONE);
        }
        Log.e("participando", " " + promocao.get(itemPosicao).getParticipando());
        Log.e("vigente", " " + promocao.get(itemPosicao).getVigente());
        btnRegulamento.setTextColor(Color.parseColor(cacheData.getString("color")));
        btnRegulamento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Regulamento.");
                try {
                    Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(promocao.get(itemPosicao).getLinkRegulamentoPromocao()));
                    startActivity(intentContato);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        btnParticipar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Participar.");

                if (cacheData.getString("userId").equals("")){
                    System.out.println("Usuário deslogado!!!");
                    AlertDialog.Builder customBuilder  = new AlertDialog.Builder(mActivity);
                    customBuilder.setTitle("Rádio Controle");
                    customBuilder.setMessage("Você precisa estar logado para poder participar da promoção.");
                    customBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SuaContaDialogFragment suaContaDialogFragment = new SuaContaDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("tela", "promocao");
                            suaContaDialogFragment.setArguments(bundle);
                            suaContaDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.add(suaContaDialogFragment,"fragment_dialog");
                            ft.commit();
                        }
                    });

                    AlertDialog alertDialog = customBuilder.create();

                    alertDialog.show();
                    Button btnPositiveWhatsapp = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                    btnPositiveWhatsapp.setTextColor(Color.parseColor(cacheData.getString("color")));
                }else {
                    System.out.println("Usuário logado!!!");
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            ApiClientFactory factory = new ApiClientFactory();
                            factory.credentialsProvider(CognitoClientManager.getCredentials());
                            factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                            final RadiocontroleClient client = factory.build(RadiocontroleClient.class);

                            Participant participant = new Participant();
                            participant.setRadioId(cacheData.getString("idRadio"));
                            participant.setPromotionId(promocao.get(itemPosicao).getTimestamp());
                            participant.setEmail(cacheData.getString("userEmail"));
                            Participant participantResult = client.radioIdParticipantsPost(cacheData.getString("idRadio"), participant);
                            System.out.println("AAAAAAAAAAAAAAAAA" + participantResult);
                            participandoCheck = true;
                            promocoesParticipantes.add(promocao.get(itemPosicao).getTimestamp()+"");
                            cacheData.putListString("promocoesParticipantes", promocoesParticipantes);
                            Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");
                            String promocoesDataset;
                            if (cacheData.getString("promocoesDataset").equals("")){
                                promocoesDataset = promocao.get(itemPosicao).getTimestamp() + "";
                            }else {
                                promocoesDataset = cacheData.getString("promocoesDataset") + "-" + promocao.get(itemPosicao).getTimestamp();
                            }

                            profileData.put("promos", promocoesDataset);
                            profileData.synchronize(new SyncCallback() {
                                @Override
                                public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                                    Log.e("SyncCallback", "Success.");
                                }

                                @Override
                                public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                                    return false;
                                }

                                @Override
                                public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                                    return false;
                                }

                                @Override
                                public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                                    return false;
                                }

                                @Override
                                public void onFailure(DataStorageException dse) {

                                }
                            });
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            if (participandoCheck){
                                AlertDialog.Builder customBuilder  = new AlertDialog.Builder(mActivity);
                                customBuilder.setTitle("Rádio Controle");
                                customBuilder.setMessage("Você está participando desta promoção.");
                                customBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = customBuilder.create();

                                alertDialog.show();
                                Button btnPositiveWhatsapp = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                                btnPositiveWhatsapp.setTextColor(Color.parseColor(cacheData.getString("color")));
                                participando.setVisibility(View.VISIBLE);
                                btnParticipar.setVisibility(View.GONE);
                            }else {
                                btnParticipar.setVisibility(View.VISIBLE);
                            }

                        }
                    }.execute();
                }
            }
        });
        txtPremios.setTextColor(Color.parseColor(cacheData.getString("color")));
        adapter = new PremiosAdapter(mContext, promocao.get(itemPosicao).getPremio());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(promocao.get(itemPosicao).getLinkVideoPromocao());

            if(matcher.find()){
                youTubePlayer.cueVideo(matcher.group());
            }

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("Erro ao inicializar Youtube player: ", youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}
