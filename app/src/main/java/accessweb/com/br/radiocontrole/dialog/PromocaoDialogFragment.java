package accessweb.com.br.radiocontrole.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.PremiosAdapter;
import accessweb.com.br.radiocontrole.model.Premio;
import accessweb.com.br.radiocontrole.model.Promocao;
import accessweb.com.br.radiocontrole.util.CacheData;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Des. Android on 30/06/2017.
 */

public class PromocaoDialogFragment extends DialogFragment {

    private Toolbar toolbar;
    private List<Promocao> promocao;
    private List<Premio> dataPremio;
    private int itemPosicao;

    private ImageView imagemPromocao;
    private ImageButton btnCompartilhar;
    private ImageButton btnAudioPromocao;
    private ImageButton btnVideoPromocao;
    private TextView tituloPromocao;
    private TextView dataEncerramentoPromocao;
    private TextView dataSorteioPromocao;
    private TextView participando;
    private Button btnRegulamento;
    private Button btnParticipar;
    private RecyclerView recyclerView;
    private PremiosAdapter adapter;

    public PromocaoDialogFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        promocao = (List<Promocao>) args.getSerializable("lista");
        itemPosicao = args.getInt("posicao");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CacheData cacheData = new CacheData(getContext());

        View rootView = inflater.inflate(R.layout.fragment_promocao, null, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.editarPerfilToolbar);
        toolbar.setTitle("Promoção");
        toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        imagemPromocao = (ImageView) rootView.findViewById(R.id.imagemPromocao);
        btnCompartilhar = (ImageButton) rootView.findViewById(R.id.btnCompartilhar);
        btnAudioPromocao = (ImageButton) rootView.findViewById(R.id.btnAudioPromocao);
        btnVideoPromocao = (ImageButton) rootView.findViewById(R.id.btnVideoPromocao);
        tituloPromocao = (TextView) rootView.findViewById(R.id.tituloPromocao);
        dataEncerramentoPromocao = (TextView) rootView.findViewById(R.id.dataEncerramentoPromocao);
        dataSorteioPromocao = (TextView) rootView.findViewById(R.id.dataSorteioPromocao);
        participando = (TextView) rootView.findViewById(R.id.participando);
        btnRegulamento = (Button) rootView.findViewById(R.id.btnRegulamento);
        btnParticipar = (Button) rootView.findViewById(R.id.btnParticipar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        Picasso.with(getContext())
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
                getContext().startActivity(Intent.createChooser(sendIntent,"Compartilhar no:" ));
            }
        });
        btnAudioPromocao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão ouvir áudio.");
            }
        });
        btnVideoPromocao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão ver vídeo.");
            }
        });
        tituloPromocao.setText(promocao.get(itemPosicao).getTituloPromocao());
        dataEncerramentoPromocao.setText(promocao.get(itemPosicao).getDataEncerramentoPromocao());
        dataSorteioPromocao.setText(promocao.get(itemPosicao).getDataSorteioPromocao());
        if (promocao.get(itemPosicao).getParticipando()){
            participando.setVisibility(View.VISIBLE);
            btnParticipar.setVisibility(View.GONE);
        }else {
            participando.setVisibility(View.GONE);
            btnParticipar.setVisibility(View.VISIBLE);
        }
        if (!promocao.get(itemPosicao).getVigente()){
            btnParticipar.setVisibility(View.GONE);
        }
        Log.e("participando", " " + promocao.get(itemPosicao).getParticipando());
        Log.e("vigente", " " + promocao.get(itemPosicao).getVigente());
        btnRegulamento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Regulamento.");
            }
        });
        btnParticipar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Participar.");
            }
        });
        adapter = new PremiosAdapter(getContext(), promocao.get(itemPosicao).getPremio());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CacheData cacheData = new CacheData(getApplicationContext());
        if (Build.VERSION.SDK_INT > 23) {
            float[] hsv = new float[3];
            int color = Color.parseColor(cacheData.getString("color"));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(color);
        }
        return dialog;
    }

}
