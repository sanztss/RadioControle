package accessweb.com.br.radiocontrole.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.PromocoesAdapter;
import accessweb.com.br.radiocontrole.model.Promocao;

import static java.security.AccessController.getContext;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PromocoesStatusFragment extends Fragment{

    private RecyclerView recyclerView;
    private PromocoesAdapter adapter;
    private List<Promocao> promocaos = new ArrayList<Promocao>();
    private static String TAG = PromocoesStatusFragment.class.getSimpleName();
    private String diaSemana;

    public PromocoesStatusFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_promocoes_status, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        adapter = new PromocoesAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    public List<Promocao> getData() {
        List<Promocao> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            Promocao promocao = new Promocao();

            promocao.setDataEncerramentoPromocao("29/06/2016");

            data.add(promocao);
        }
        return data;
    }

}
