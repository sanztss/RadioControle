package accessweb.com.br.radiocontrole.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import accessweb.com.br.radiocontrole.adapter.ProgramacaoAdapter;
import accessweb.com.br.radiocontrole.model.Noticia;
import accessweb.com.br.radiocontrole.model.Programa;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;

import static android.R.string.no;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class SegundaFragment extends Fragment{

    private RecyclerView recyclerView;
    private ProgramacaoAdapter adapter;
    private List<Programa> programas = new ArrayList<Programa>();
    private static String TAG = SegundaFragment.class.getSimpleName();

    public SegundaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dia_semana, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        adapter = new ProgramacaoAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        Programa item = getData().get(itemPosition);
                        Log.v("aaa", "" + itemPosition);
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );*/

        return rootView;
    }

    public static List<Programa> getData() {
        List<Programa> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Programa programa = new Programa();
            programa.setHoraInicioPrograma("10:00");
            programa.setNomePrograma("Nome do programa " + i);
            programa.setFotoLocutorPrograma("https://www.workinentertainment.com/blog/wp-content/uploads/2013/06/career-in-music-radio-host.jpg");
            programa.setNomeLocutorPrograma("Nome do locutor " + i);
            data.add(programa);
        }
        return data;
    }

}
