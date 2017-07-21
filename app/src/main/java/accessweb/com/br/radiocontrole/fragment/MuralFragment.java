package accessweb.com.br.radiocontrole.fragment;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.ampiri.sdk.listeners.NativeAdCallback;
import com.ampiri.sdk.listeners.StreamNativeAdCallback;
import com.ampiri.sdk.mediation.ResponseStatus;
import com.ampiri.sdk.nativead.FeedNativeAdView;
import com.ampiri.sdk.nativead.NativeAd;
import com.ampiri.sdk.nativead.recyclerview.StreamAdRecyclerAdapter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.dialog.EscolherDialogFragment;
import accessweb.com.br.radiocontrole.adapter.MuralListAdapter;
import accessweb.com.br.radiocontrole.dialog.SuaContaDialogFragment;
import accessweb.com.br.radiocontrole.model.Mural;
import accessweb.com.br.radiocontrole.model.Post;
import accessweb.com.br.radiocontrole.model.Posts;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.EndlessRecyclerViewScrollListener;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;

import static accessweb.com.br.radiocontrole.R.drawable.ge;


public class MuralFragment extends Fragment {

    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView recyclerView;
    private MuralListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton btnAdicionarPostagem;
    private static ArrayList<String> fotoUsuario = new ArrayList<String>();
    private static ArrayList<String> nomeUsuario = new ArrayList<String>();
    private static ArrayList<String> modeloPublicacao = new ArrayList<String>();
    private static ArrayList<String> tempoPublicacao = new ArrayList<String>();
    private static ArrayList<String> textoPublicacao = new ArrayList<String>();
    List<Mural> items = new ArrayList<>();

    private CacheData cacheData;
    private String lastKey = "";

    public MuralFragment() {
        // Required empty public constructor
    }

    /*public static List<Mural> getData() {
        List<Mural> data = new ArrayList<>();

        for (int i = 0; i < nomeUsuario.size(); i++) {
            Mural mural = new Mural();
            mural.setFotoUsuario(fotoUsuario.get(i));
            mural.setNomeUsuario(nomeUsuario.get(i));
            mural.setTempoPublicacao(tempoPublicacao.get(i));
            mural.setTextoPublicacao(textoPublicacao.get(i));
            mural.setModeloPublicacao(modeloPublicacao.get(i));
            data.add(mural);
        }
        return data;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nomeUsuario.clear();
        modeloPublicacao.clear();
        fotoUsuario.add("http://jc.blogs.com/.a/6a00d83454559b69e2019affd5a0d1970b-800wi");
        fotoUsuario.add("https://s-media-cache-ak0.pinimg.com/originals/84/8c/89/848c89d33eea05fb2da64e1a70d516ea.jpg");
        fotoUsuario.add("https://s-media-cache-ak0.pinimg.com/originals/e3/9b/b0/e39bb045fa7df2c2171d97ec1a3be9dc.jpg");

        nomeUsuario.add("Maria Sato");
        nomeUsuario.add("Guilherme de Santi");
        nomeUsuario.add("Paulo Marques");

        tempoPublicacao.add("há 1 horas");
        tempoPublicacao.add("há 2 horas");
        tempoPublicacao.add("há 3 horas");

        textoPublicacao.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus volutpat justo gravida malesuada. Pellentesque pulvinar, ante placerat commodo mattis, ipsum enim pulvinar orci, non accumsan urna neque vitae tellus.");
        textoPublicacao.add("");
        textoPublicacao.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus volutpat justo gravida malesuada.");

        modeloPublicacao.add("texto");
        modeloPublicacao.add("audio");
        modeloPublicacao.add("imagem");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        cacheData = new CacheData(getContext());

        View rootView = inflater.inflate(R.layout.fragment_mural, container, false);

        btnAdicionarPostagem = (FloatingActionButton) rootView.findViewById(R.id.btnAdicionarPostagem);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                Posts posts = client.radioIdPostsGet(cacheData.getString("idRadio"), lastKey);
                for (Post post : posts.getPosts()){
                    /*Log.e("AAAAAAAAAAA", "getAuthorId:" + post.getAuthorId());
                    Log.e("AAAAAAAAAAA", "getAuthorPicture:" + post.getAuthorPicture());
                    Log.e("AAAAAAAAAAA", "getAuthorName:" + post.getAuthorName());
                    Log.e("AAAAAAAAAAA", "getContent:" + post.getContent());
                    Log.e("AAAAAAAAAAA", "getRadioId:" + post.getRadioId());
                    Log.e("AAAAAAAAAAA", "getTimestamp:" + post.getTimestamp());
                    Log.e("AAAAAAAAAAA", "getType:" + post.getType());
                    Log.e("AAAAAAAAAAA", "getAttachment:" + post.getAttachment());*/
                    Timestamp timestamp = new Timestamp(post.getTimestamp());
                    Date dataPostagem = new Date(timestamp.getTime());

                    lastKey = post.getTimestamp().toString();
                    Mural mural = new Mural();
                    if (post.getType().equals("text")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao(post.getContent());
                        mural.setModeloPublicacao("texto");
                        items.add(mural);
                    } else if (post.getType().equals("image")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao(post.getContent());
                        mural.setImagemPublicacao(post.getAttachment());
                        mural.setModeloPublicacao("imagem");
                        items.add(mural);
                    } else if (post.getType().equals("audio")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao("");
                        mural.setModeloPublicacao("audio");
                        mural.setAudioPublicacao(post.getAttachment());
                        items.add(mural);
                    }


                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                adapter = new MuralListAdapter(getContext(), items);

                StreamNativeAdCallback streamNativeAdListener = new StreamNativeAdCallback() {
                    @Override
                    public void onStreamAdLoadFailed() {

                    }

                    @Override
                    public void onAdLoaded(@NonNull NativeAd nativeAd) {

                    }

                    @Override
                    public void onAdFailed(@NonNull NativeAd nativeAd, @NonNull ResponseStatus responseStatus) {

                    }

                    @Override
                    public void onAdOpened(@NonNull NativeAd nativeAd) {

                    }

                    @Override
                    public void onAdClicked(@NonNull NativeAd nativeAd) {

                    }

                    @Override
                    public void onAdClosed(@NonNull NativeAd nativeAd) {

                    }

                    @Override
                    public void onAdCompleted(@NonNull NativeAd nativeAd) {

                    }
                };
                StreamAdRecyclerAdapter adAdapter = new StreamAdRecyclerAdapter.Builder()
                        .setAdapter(adapter)
                        .setAdUnitId("2f3caac7-7573-4e6b-bf1a-d8b58b06e9d9")
                        .setViewBuilder(FeedNativeAdView.BUILDER)
                        .setEventCallback(streamNativeAdListener)
                        .build(getContext());
                recyclerView.setAdapter(adAdapter);
                adAdapter.loadAd();
                recyclerView.setLayoutManager(linearLayoutManager);

            }
        }.execute();



        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        // Set a Refresh Listener for the SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the data
                // Calls setRefreshing(false) when it is finish
                updateMural();
            }
        });

        /*try {
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        } catch (Exception e) {
            Log.v("aaaa", "" + e);
        }*/
        // Inflate the layout for this fragment

        btnAdicionarPostagem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CacheData cacheData = new CacheData(getContext());
                System.out.println("sashauisha" + cacheData.getString("userId"));
                if (cacheData.getString("userId").equals("")){
                    System.out.println("Usuário deslogado!!!");
                    SuaContaDialogFragment dialog = new SuaContaDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tela", "mural");
                    dialog.setArguments(bundle);

                    dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    //dialog.show(this.getSupportFragmentManager(), "dialog");
                    ft.add(dialog,"fragment_dialog");
                    ft.commit();
                }else {
                    System.out.println("Usuário logado!!!");
                    final EscolherDialogFragment dialogListCanais = new EscolherDialogFragment("Selecione o tipo da postagem:", "mural", null);
                    dialogListCanais.show(getActivity().getSupportFragmentManager(), "dialog");
                }
            }
        });
        btnAdicionarPostagem.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(cacheData.getString("color"))));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Mural");*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void updateMural(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Log.e("VVVVVVVVVVV", lastKey);

                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                Posts posts = client.radioIdPostsGet(cacheData.getString("idRadio"), "");
                items.clear();
                items = new ArrayList<Mural>();
                for (Post post : posts.getPosts()){
                    Timestamp timestamp = new Timestamp(post.getTimestamp());
                    Date dataPostagem = new Date(timestamp.getTime());

                    lastKey = post.getTimestamp().toString();
                    Mural mural = new Mural();
                    if (post.getType().equals("text")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao(post.getContent());
                        mural.setModeloPublicacao("texto");
                        items.add(mural);
                    } else if (post.getType().equals("image")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao(post.getContent());
                        mural.setImagemPublicacao(post.getAttachment());
                        mural.setModeloPublicacao("imagem");
                        items.add(mural);
                    } else if (post.getType().equals("audio")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao("");
                        mural.setAudioPublicacao(post.getAttachment());
                        mural.setModeloPublicacao("audio");
                        items.add(mural);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                adapter.addAllNew(items);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }

    public void loadNextDataFromApi(int offset) {
        final List<Mural> listaAnterires = new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Log.e("VVVVVVVVVVV", lastKey);

                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                Posts posts = client.radioIdPostsGet(cacheData.getString("idRadio"), lastKey);
                for (Post post : posts.getPosts()){
                    Timestamp timestamp = new Timestamp(post.getTimestamp());
                    Date dataPostagem = new Date(timestamp.getTime());

                    lastKey = post.getTimestamp().toString();
                    lastKey = post.getTimestamp().toString();
                    Mural mural = new Mural();
                    if (post.getType().equals("text")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao(post.getContent());
                        mural.setModeloPublicacao("texto");
                        listaAnterires.add(mural);
                    } else if (post.getType().equals("image")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao(post.getContent());
                        mural.setImagemPublicacao(post.getAttachment());
                        mural.setModeloPublicacao("imagem");
                        listaAnterires.add(mural);
                    } else if (post.getType().equals("audio")){
                        mural.setFotoUsuario(post.getAuthorPicture());
                        mural.setNomeUsuario(post.getAuthorName());
                        mural.setTempoPublicacao(dataPostagem);
                        mural.setTextoPublicacao("");
                        mural.setAudioPublicacao(post.getAttachment());
                        mural.setModeloPublicacao("audio");
                        listaAnterires.add(mural);
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                adapter.addAllOld(listaAnterires, adapter.getItemCount());
            }
        }.execute();
    }


}
