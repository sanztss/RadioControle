package accessweb.com.br.radiocontrole.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.PromocoesAdapter;
import accessweb.com.br.radiocontrole.dialog.PromocaoDialogFragment;
import accessweb.com.br.radiocontrole.model.Premio;
import accessweb.com.br.radiocontrole.model.Promocao;
import accessweb.com.br.radiocontrole.util.RecyclerItemClickListener;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PromocoesStatusFragment extends Fragment{

    private RecyclerView recyclerView;
    private PromocoesAdapter adapter;
    private List<Promocao> promocoes = new ArrayList<Promocao>();
    private List<Promocao> data;
    private static String TAG = PromocoesStatusFragment.class.getSimpleName();
    private String status;

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

        Bundle args = getArguments();
        status = (String) args.get("status");

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        adapter = new PromocoesAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    public List<Promocao> getData() {
        data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            Promocao promocao = new Promocao();

            switch (status){
                case "vigentes":
                    List<Premio> listaPremioVigentes = new ArrayList<>();
                    promocao.setVigente(true);
                    promocao.setTituloPromocao("Promoção vigente " + i);
                    promocao.setImagemPromocao("http://www.acheipromocao.com.br/wp-content/uploads/2015/10/Promocao-Jovem-Pan-GoPro.jpg");
                    promocao.setDataEncerramentoPromocao("10/07/2017");
                    promocao.setDataSorteioPromocao("15/07/2017");
                    promocao.setLinkAudioPromocao("http://e.mundopodcast.com.br/podprogramar/podprogramar-013-sistemas-controle-versao.mp3");
                    promocao.setLinkRegulamentoPromocao("https://www.promogaroto.com.br/garotodesorte/Regulamento");
                    promocao.setLinkVideoPromocao("http://dl.par30dl.com/Movie/Trailer/01/BAT_vs_SUP_480p_HDTN_%28Par30dl.Com%29.mp4");
                    if (i%2 == 0){
                        promocao.setParticipando(true);
                    }else {
                        promocao.setParticipando(false);
                    }
                    for (int j = 0; j < 5; j++){
                        Premio premioVigentes = new Premio();
                        premioVigentes.setImagemPremio("https://images-na.ssl-images-amazon.com/images/G/01/aplusautomation/vendorimages/6c33899d-6c0c-401c-8ad5-445d923a843c.jpg._CB278032858__SL300__.jpg");
                        premioVigentes.setTituloPremio("Título prêmio " + j);
                        listaPremioVigentes.add(premioVigentes);
                    }
                    promocao.setPremio(listaPremioVigentes);
                    break;
                case "encerradas":
                    List<Premio> listaPremioEncerradas = new ArrayList<>();
                    promocao.setVigente(false);
                    promocao.setTituloPromocao("Promoção encerrada " + i);
                    promocao.setImagemPromocao("http://www.acheipromocao.com.br/wp-content/uploads/2015/10/Promocao-Jovem-Pan-GoPro.jpg");
                    promocao.setDataEncerramentoPromocao("10/07/2017");
                    promocao.setDataSorteioPromocao("15/07/2017");
                    promocao.setLinkAudioPromocao("http://e.mundopodcast.com.br/podprogramar/podprogramar-013-sistemas-controle-versao.mp3");
                    promocao.setLinkRegulamentoPromocao("https://www.promogaroto.com.br/garotodesorte/Regulamento");
                    promocao.setLinkVideoPromocao("http://dl.par30dl.com/Movie/Trailer/01/BAT_vs_SUP_480p_HDTN_%28Par30dl.Com%29.mp4");
                    promocao.setParticipando(false);
                    for (int j = 0; j < 5; j++){
                        Premio premioEncerradas = new Premio();
                        premioEncerradas.setImagemPremio("https://images-na.ssl-images-amazon.com/images/G/01/aplusautomation/vendorimages/6c33899d-6c0c-401c-8ad5-445d923a843c.jpg._CB278032858__SL300__.jpg");
                        premioEncerradas.setTituloPremio("Título prêmio " + j);
                        listaPremioEncerradas.add(premioEncerradas);
                    }
                    promocao.setPremio(listaPremioEncerradas);
                    break;
                default:
                    break;
            }

            data.add(promocao);
        }
        return data;
    }

}
