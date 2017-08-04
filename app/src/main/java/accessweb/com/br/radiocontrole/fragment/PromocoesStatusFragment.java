package accessweb.com.br.radiocontrole.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.PromocoesAdapter;
import accessweb.com.br.radiocontrole.model.Premio;
import accessweb.com.br.radiocontrole.model.Promocao;
import accessweb.com.br.radiocontrole.model.Promotion;
import accessweb.com.br.radiocontrole.model.PromotionPrizesItem;
import accessweb.com.br.radiocontrole.model.Promotions;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;

import static android.R.attr.format;
import static com.ampiri.sdk.c.b.a.D;

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

        final CacheData cacheData = new CacheData(getContext());


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_promocoes_status, container, false);

        Bundle args = getArguments();
        status = (String) args.get("status");

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        data = new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                Promotions promotions = client.radioIdPromotionsGet(cacheData.getString("idRadio"));

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                String stringToday = dateFormat.format(new Date());

                Date dateToday = null;

                for (Promotion promotion : promotions){

                    switch (status){
                        case "vigentes":
                            Date dateVigente = null;
                            try {
                                dateVigente = dateFormat.parse(promotion.getEndingDate());
                                dateToday = dateFormat.parse(stringToday);

                                if (dateToday.before(dateVigente) || dateToday.equals(dateVigente)){
                                    Promocao promocao = new Promocao();
                                    List<Premio> listaPremioVigentes = new ArrayList<>();
                                    promocao.setVigente(true);
                                    promocao.setTituloPromocao(promotion.getName());
                                    promocao.setImagemPromocao(promotion.getImage());
                                    promocao.setDataEncerramentoPromocao(promotion.getEndingDate());
                                    promocao.setDataSorteioPromocao(promotion.getDrawDate());
                                    promocao.setLinkRegulamentoPromocao(promotion.getLinkRegulation());
                                    promocao.setLinkVideoPromocao(promotion.getLinkYoutube());
                                   /* if (i%2 == 0){
                                        promocao.setParticipando(true);
                                    }else {
                                        promocao.setParticipando(false);
                                    }*/
                                    for (PromotionPrizesItem prize : promotion.getPrizes()){
                                        Premio premioVigentes = new Premio();
                                        premioVigentes.setImagemPremio(prize.getPrizeImage());
                                        premioVigentes.setTituloPremio(prize.getPrizeName());
                                        listaPremioVigentes.add(premioVigentes);
                                    }

                                    promocao.setPremio(listaPremioVigentes);
                                    data.add(promocao);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            break;
                        case "encerradas":
                            Date dateEncerrada = null;
                            try {
                                dateEncerrada = dateFormat.parse(promotion.getEndingDate());
                                dateToday = dateFormat.parse(stringToday);

                                if (dateToday.after(dateEncerrada)){
                                    Promocao promocao = new Promocao();
                                    List<Premio> listaPremioEncerradas = new ArrayList<>();
                                    promocao.setVigente(false);
                                    promocao.setTituloPromocao(promotion.getName());
                                    promocao.setImagemPromocao(promotion.getImage());
                                    promocao.setDataEncerramentoPromocao(promotion.getEndingDate());
                                    promocao.setDataSorteioPromocao(promotion.getDrawDate());
                                    promocao.setLinkRegulamentoPromocao(promotion.getLinkRegulation());
                                    promocao.setLinkVideoPromocao(promotion.getLinkYoutube());
                                    promocao.setParticipando(false);
                                    for (PromotionPrizesItem prize : promotion.getPrizes()){
                                        Premio premioEncerradas = new Premio();
                                        premioEncerradas.setImagemPremio(prize.getPrizeImage());
                                        premioEncerradas.setTituloPremio(prize.getPrizeName());
                                        listaPremioEncerradas.add(premioEncerradas);
                                    }
                                    promocao.setPremio(listaPremioEncerradas);
                                    data.add(promocao);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            break;
                        default:
                            break;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                adapter = new PromocoesAdapter(getContext(), data);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }.execute();


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
