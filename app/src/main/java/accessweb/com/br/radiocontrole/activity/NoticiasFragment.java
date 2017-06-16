package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.ResultReceiver;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.NoticiasListAdapter;
import accessweb.com.br.radiocontrole.model.Noticia;
import accessweb.com.br.radiocontrole.util.NoticiaService;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;

import static android.media.CamcorderProfile.get;


public class NoticiasFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoticiasListAdapter adapter;
    private NoticiasFragmentListener drawerListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static ArrayList<String> tempoNoticias = new ArrayList<String>();
    private static ArrayList<String> tituloNoticias = new ArrayList<String>();
    private static ArrayList<String> resumoNoticias = new ArrayList<String>();
    private static ArrayList<String> linkNoticias = new ArrayList<String>();

    List<Noticia> items;

    private static String TAG = ModalListFragment.class.getSimpleName();

    public NoticiasFragment() {
        // Required empty public constructor
    }

    public void setDrawerListener(NoticiasFragmentListener listener) {
        this.drawerListener = listener;
    }

    public static List<Noticia> getData() {
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
    }

    private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            items = (List<Noticia>) resultData.getSerializable(NoticiaService.ITEMS);
            if (items != null) {
                adapter = new NoticiasListAdapter(getActivity(), items);
                //adapter.clearData();
                //adapter.setData(items);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "Error while downloading the rss feed.",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    private void startService() {
        Intent intent = new Intent(getContext(), NoticiaService.class);
        intent.putExtra(NoticiaService.RECEIVER, resultReceiver);
        getContext().startService(intent);
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_noticias, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        /*adapter = new NoticiasListAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);*/

        items =  new ArrayList<Noticia>();
        adapter = new NoticiasListAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
        startService();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*recyclerView.addOnItemTouchListener(new ModalListFragment.RecyclerTouchListener(getActivity(), recyclerView, new ModalListFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v("aaaa","" + view);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        Noticia item = items.get(itemPosition);

                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLinkNoticia()));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            //Toast.makeText(this, "No application can handle this request. Please install a web browser or check your URL.",  Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        // Set a Refresh Listener for the SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the data
                // Calls setRefreshing(false) when it is finish
                updateNoticias();
            }
        });

        /*try {
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        } catch (Exception e) {
            Log.v("aaaa", "" + e);
        }*/
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Notícias");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface NoticiasFragmentListener {
        public void onDrawerItemSelected(View view, int position);
    }

    protected void updateNoticias(){
        startService();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
