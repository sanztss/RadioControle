package accessweb.com.br.radiocontrole.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.NoticiasListAdapter;
import accessweb.com.br.radiocontrole.adapter.NoticiasRetrofitAdapter;
import accessweb.com.br.radiocontrole.dal.DatabaseHandler;
import accessweb.com.br.radiocontrole.model.Noticia;
import accessweb.com.br.radiocontrole.model.NoticiaFeed;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static android.media.CamcorderProfile.get;


public class NoticiasFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoticiasListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /*private static ArrayList<String> tempoNoticias = new ArrayList<String>();
    private static ArrayList<String> tituloNoticias = new ArrayList<String>();
    private static ArrayList<String> resumoNoticias = new ArrayList<String>();
    private static ArrayList<String> linkNoticias = new ArrayList<String>();*/

    private static final String RSS_LINK = "http://g1.globo.com/"; // http://g1.globo.com/ http://feeds.feedburner.com/TechCrunch/social/

    private List<Noticia> cachedList = new ArrayList<Noticia>();

    //List<Noticia> items = null;

    private DatabaseHandler db;

    private static String TAG = NoticiasFragment.class.getSimpleName();

    public NoticiasFragment() {
        // Required empty public constructor
    }

    /*public static List<Noticia> getData() {
        List<Noticia> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < tituloNoticias.size(); i++) {
            Noticia noticia = new Noticia();
            noticia.setTempoNoticia(tempoNoticias.get(i));
            noticia.setTituloNoticia(tituloNoticias.get(i));
            noticia.setResumoNoticia(resumoNoticias.get(i));
            noticia.setLinkNoticia(linkNoticias.get(i));
            data.add(noticia);
        }


        return data;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*tempoNoticias.clear();
        tituloNoticias.clear();
        resumoNoticias.clear();

        tempoNoticias.add("há 43 minutos");
        tempoNoticias.add("há 1 hora");
        tempoNoticias.add("há 1 hora");

        tituloNoticias.add("Título da notícia pode ter até duas linhas de tamanho 1");
        tituloNoticias.add("Título da notícia pode ter até duas linhas de tamanho 2");
        tituloNoticias.add("Título da notícia pode ter até uma linha");

        resumoNoticias.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultrices ultricies nisi, ac lobortis metus semper vitae. Vestibulum condimentum quis purus ut volutpat.");
        resumoNoticias.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultrices ultricies nisi, ac lobortis metus semper vitae. Vestibulum condimentum quis purus ut volutpat.");
        resumoNoticias.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultrices ultricies nisi, ac lobortis metus semper vitae. Vestibulum condimentum quis purus ut volutpat.");

        linkNoticias.add("http://");
        linkNoticias.add("http://");
        linkNoticias.add("http://");*/
    }

    private void requestNoticias(final Boolean desabilitarSwipe){

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
                if (cachedList.size() == 0 && mItems != null) {
                    cachedList = new ArrayList<Noticia>(mItems);
                    for ( int i = mItems.size() -1 ; i >= 0 ; i--) {
                        Noticia item = mItems.get(i);
                        db.addNoticia(item);
                    }
                    Log.d(TAG, "Initialized  cached list");
                    sendCachedList();
                } else if ( mItems != null ){
                    boolean itemsUpdated = false;
                    for ( int j = mItems.size() -1 ; j >= 0 ; j--) {
                        Noticia item = mItems.get(j);
                        boolean itemExists = false;
                        for (Noticia i: cachedList) {
                            if (db.isEqualTo(item)) {
                                itemExists = true;
                                break;
                            }
                        }
                        if (!itemExists) {
                            itemsUpdated = true;
                            Log.d(TAG, "Found a new item " + item.getTituloNoticia());
                            db.addNoticia(item);
                            cachedList.add(0, item);
                        }
                    }
                    if (itemsUpdated) {
                        Log.d(TAG, "Finished updating cached list");
                        //sendCachedList();
                    } else {
                        Log.d(TAG,"No updates to cache no need to send an update");
                        //sendCachedList();
                    }
                    if (desabilitarSwipe){
                        sendCachedList();
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        sendCachedList();
                    }

                }

            }

            @Override
            public void onFailure(Call<NoticiaFeed> call, Throwable t) {
                Log.d(TAG, "OnFailure Error is " + t);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_noticias, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        adapter = new NoticiasListAdapter(getContext(), cachedList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        Noticia item = cachedList.get(itemPosition);
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLinkNoticia()));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );

        /*adapter = new NoticiasListAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);*/

        //items =  new ArrayList<Noticia>();

        db = new DatabaseHandler(getActivity());
        cachedList = db.getAllNoticias();
        requestNoticias(false);

        /*List<Noticia> noticias = db.getAllNoticias();

        for (Noticia n : noticias) {
            String log = "Tempo Notícia: " + n.getTempoNoticia() + " ,Título Notícia: " + n.getTituloNoticia() + " ,Resumo Notícia: " + n.getResumoNoticia() + " ,Link Notícia: " + n.getLinkNoticia();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }*/

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateNoticias();
            }
        });

        return rootView;
    }

    private void sendCachedList() {
        if ( cachedList != null ) {
            adapter.clear();
            adapter.addAll(cachedList);

            Log.d(TAG, "Sending cachedList");
        }
        else {
            Log.d(TAG, "Cached list is empty!");
        }
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface NoticiasFragmentListener {
        public void onDrawerItemSelected(View view, int position);
    }

    protected void updateNoticias(){
        requestNoticias(true);
    }
}
