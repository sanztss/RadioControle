package accessweb.com.br.radiocontrole.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import accessweb.com.br.radiocontrole.dialog.EscolherDialogFragment;
import accessweb.com.br.radiocontrole.adapter.MuralListAdapter;
import accessweb.com.br.radiocontrole.model.Mural;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;


public class MuralFragment extends Fragment {

    private RecyclerView recyclerView;
    private MuralListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton btnAdicionarPostagem;
    private static ArrayList<String> fotoUsuario = new ArrayList<String>();
    private static ArrayList<String> nomeUsuario = new ArrayList<String>();
    private static ArrayList<String> modeloPublicacao = new ArrayList<String>();
    private static ArrayList<String> tempoPublicacao = new ArrayList<String>();
    private static ArrayList<String> textoPublicacao = new ArrayList<String>();
    List<Mural> items;

    public MuralFragment() {
        // Required empty public constructor
    }

    public static List<Mural> getData() {
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
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mural, container, false);

        btnAdicionarPostagem = (FloatingActionButton) rootView.findViewById(R.id.btnAdicionarPostagem);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        /*adapter = new NoticiasListAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);*/

        //items =  new ArrayList<Mural>();
        adapter = new MuralListAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        //Mural item = items.get(itemPosition);
                        Log.v("aaa","" + itemPosition);
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
                final EscolherDialogFragment dialogListCanais = new EscolherDialogFragment("Selecione o tipo da postagem:", "mural", null);
                dialogListCanais.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
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
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
