package accessweb.com.br.radiocontrole.fragment;

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
import accessweb.com.br.radiocontrole.adapter.ProgramacaoAdapter;
import accessweb.com.br.radiocontrole.model.Programa;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class ProgramacaoDiaFragment extends Fragment{

    private RecyclerView recyclerView;
    private ProgramacaoAdapter adapter;
    private List<Programa> programas = new ArrayList<Programa>();
    private static String TAG = ProgramacaoDiaFragment.class.getSimpleName();
    private String diaSemana;

    public ProgramacaoDiaFragment() {
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

        Bundle args = getArguments();
        diaSemana = (String) args.get("dia");

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        adapter = new ProgramacaoAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    public List<Programa> getData() {
        List<Programa> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            Programa programa = new Programa();

            switch (diaSemana){
                case "segunda":
                    programa.setHoraInicioPrograma("10:00");
                    programa.setNomePrograma("Nome do programa de segunda " + i);
                    programa.setFotoLocutorPrograma("https://www.workinentertainment.com/blog/wp-content/uploads/2013/06/career-in-music-radio-host.jpg");
                    programa.setNomeLocutorPrograma("Nome do locutor " + i);
                    break;
                case "terca":
                    programa.setHoraInicioPrograma("10:00");
                    programa.setNomePrograma("Nome do programa de terça " + i);
                    programa.setFotoLocutorPrograma("https://www.workinentertainment.com/blog/wp-content/uploads/2013/06/career-in-music-radio-host.jpg");
                    programa.setNomeLocutorPrograma("Nome do locutor " + i);
                    break;
                case "quarta":
                    programa.setHoraInicioPrograma("10:00");
                    programa.setNomePrograma("Nome do programa de quarta " + i);
                    programa.setFotoLocutorPrograma("https://www.workinentertainment.com/blog/wp-content/uploads/2013/06/career-in-music-radio-host.jpg");
                    programa.setNomeLocutorPrograma("Nome do locutor " + i);
                    break;
                case "quinta":
                    programa.setHoraInicioPrograma("10:00");
                    programa.setNomePrograma("Nome do programa de quinta " + i);
                    programa.setFotoLocutorPrograma("https://www.workinentertainment.com/blog/wp-content/uploads/2013/06/career-in-music-radio-host.jpg");
                    programa.setNomeLocutorPrograma("Nome do locutor " + i);
                    break;
                case "sexta":
                    programa.setHoraInicioPrograma("10:00");
                    programa.setNomePrograma("Nome do programa de sexta " + i);
                    programa.setFotoLocutorPrograma("https://www.workinentertainment.com/blog/wp-content/uploads/2013/06/career-in-music-radio-host.jpg");
                    programa.setNomeLocutorPrograma("Nome do locutor " + i);
                    data.add(programa);
                    break;
                case "sabado":
                    programa.setHoraInicioPrograma("10:00");
                    programa.setNomePrograma("Nome do programa de sábado " + i);
                    programa.setFotoLocutorPrograma("https://www.workinentertainment.com/blog/wp-content/uploads/2013/06/career-in-music-radio-host.jpg");
                    programa.setNomeLocutorPrograma("Nome do locutor " + i);
                    break;
                case "domingo":
                    programa.setHoraInicioPrograma("10:00");
                    programa.setNomePrograma("Nome do programa de domingo " + i);
                    programa.setFotoLocutorPrograma("https://www.workinentertainment.com/blog/wp-content/uploads/2013/06/career-in-music-radio-host.jpg");
                    programa.setNomeLocutorPrograma("Nome do locutor " + i);
                    data.add(programa);
                    break;
                default:
                    break;
            }

            data.add(programa);
        }
        return data;
    }

}
