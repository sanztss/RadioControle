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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.ProgramacaoAdapter;
import accessweb.com.br.radiocontrole.model.Host;
import accessweb.com.br.radiocontrole.model.Program;
import accessweb.com.br.radiocontrole.model.Programa;
import accessweb.com.br.radiocontrole.model.Programs;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;

import static android.R.attr.data;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class ProgramacaoDiaFragment extends Fragment{

    private RecyclerView recyclerView;
    private ProgramacaoAdapter adapter;
    private List<Program> programas = new ArrayList<Program>();
    private List<Host> hosts = new ArrayList<Host>();
    private static String TAG = ProgramacaoDiaFragment.class.getSimpleName();
    private String diaSemana;
    private ArrayList<String> programasNotificar = null;

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
        programas = (List<Program>) args.getSerializable("programas");
        hosts = (List<Host>) args.getSerializable("hosts");

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);

        adapter = new ProgramacaoAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    public List<Programa> getData() {
        final List<Programa> data = new ArrayList<>();

        CacheData cacheData = new CacheData(getContext());

        programasNotificar =  cacheData.getListString("programasNotificar");

        for (Program program : programas){

            Calendar segunda = Calendar.getInstance();
            segunda.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Calendar terca = Calendar.getInstance();
            terca.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            Calendar quarta = Calendar.getInstance();
            quarta.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            Calendar quinta = Calendar.getInstance();
            quinta.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            Calendar sexta = Calendar.getInstance();
            sexta.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            Calendar sabado = Calendar.getInstance();
            sabado.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            Calendar domingo = Calendar.getInstance();
            int weekday = domingo.get(Calendar.DAY_OF_WEEK);
            int days = Calendar.SUNDAY - weekday;
            if (days < 0) {
                days += 7;
            }
            domingo.add(Calendar.DAY_OF_YEAR, days);

            switch (diaSemana){
                case "segunda":
                    if (program.getMon()){
                        Programa programa = new Programa();
                        programa.setHoraInicioPrograma(program.getStartTime());
                        programa.setNomePrograma(program.getName());
                        for (Host host : hosts){
                            if (host.getId().equals(program.getHostId())){
                                programa.setFotoLocutorPrograma("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/" + host.getPicture());
                                programa.setNomeLocutorPrograma(host.getName());
                                break;
                            }
                        }
                        String startTime = program.getStartTime();
                        String[] startTimeSplit = startTime.split(":");
                        int hora = Integer.parseInt(startTimeSplit[0]);
                        int minuto = Integer.parseInt(startTimeSplit[1]);
                        segunda.set(Calendar.HOUR_OF_DAY,hora);
                        segunda.set(Calendar.MINUTE,minuto);
                        segunda.set(Calendar.SECOND, 0);
                        programa.setDataPrograma(segunda);
                        for (String programaNotificar : programasNotificar){
                            String diaPrograma = programaNotificar.replaceAll("[^A-Za-z]+", "");
                            long idPrograma = Long.parseLong(programaNotificar.replaceAll("\\D+",""));
                            if (diaPrograma.equals("segunda") && idPrograma == program.getId().longValue()){
                                programa.setNotificarPrograma(true);
                                break;
                            }else {
                                programa.setNotificarPrograma(false);
                            }
                        }
                        programa.setIdPrograma(program.getId());
                        programa.setDiaPrograma("segunda");
                        data.add(programa);
                    }

                    break;
                case "terca":
                    if (program.getTue()){
                        Programa programa = new Programa();
                        programa.setHoraInicioPrograma(program.getStartTime());
                        programa.setNomePrograma(program.getName());
                        for (Host host : hosts){
                            if (host.getId().equals(program.getHostId())){
                                programa.setFotoLocutorPrograma("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/" + host.getPicture());
                                programa.setNomeLocutorPrograma(host.getName());
                                break;
                            }
                        }
                        String startTime = program.getStartTime();
                        String[] startTimeSplit = startTime.split(":");
                        int hora = Integer.parseInt(startTimeSplit[0]);
                        int minuto = Integer.parseInt(startTimeSplit[1]);
                        terca.set(Calendar.HOUR_OF_DAY,hora);
                        terca.set(Calendar.MINUTE,minuto);
                        terca.set(Calendar.SECOND, 0);
                        programa.setDataPrograma(terca);
                        for (String programaNotificar : programasNotificar){
                            String diaPrograma = programaNotificar.replaceAll("[^A-Za-z]+", "");
                            long idPrograma = Long.parseLong(programaNotificar.replaceAll("\\D+",""));
                            if (diaPrograma.equals("terca") && idPrograma == program.getId().longValue()){
                                programa.setNotificarPrograma(true);
                                break;
                            }else {
                                programa.setNotificarPrograma(false);
                            }
                        }
                        programa.setIdPrograma(program.getId());
                        programa.setDiaPrograma("terca");
                        data.add(programa);
                    }
                    break;
                case "quarta":
                    if (program.getWed()){
                        Programa programa = new Programa();
                        programa.setHoraInicioPrograma(program.getStartTime());
                        programa.setNomePrograma(program.getName());
                        for (Host host : hosts){
                            if (host.getId().equals(program.getHostId())){
                                programa.setFotoLocutorPrograma("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/" + host.getPicture());
                                programa.setNomeLocutorPrograma(host.getName());
                                break;
                            }
                        }
                        String startTime = program.getStartTime();
                        String[] startTimeSplit = startTime.split(":");
                        int hora = Integer.parseInt(startTimeSplit[0]);
                        int minuto = Integer.parseInt(startTimeSplit[1]);
                        quarta.set(Calendar.HOUR_OF_DAY,hora);
                        quarta.set(Calendar.MINUTE,minuto);
                        quarta.set(Calendar.SECOND, 0);
                        programa.setDataPrograma(quarta);
                        for (String programaNotificar : programasNotificar){
                            String diaPrograma = programaNotificar.replaceAll("[^A-Za-z]+", "");
                            long idPrograma = Long.parseLong(programaNotificar.replaceAll("\\D+",""));
                            if (diaPrograma.equals("quarta") && idPrograma == program.getId().longValue()){
                                programa.setNotificarPrograma(true);
                                break;
                            }else {
                                programa.setNotificarPrograma(false);
                            }
                        }
                        programa.setIdPrograma(program.getId());
                        programa.setDiaPrograma("quarta");
                        data.add(programa);
                    }
                    break;
                case "quinta":
                    if (program.getThu()){
                        Programa programa = new Programa();
                        programa.setHoraInicioPrograma(program.getStartTime());
                        programa.setNomePrograma(program.getName());
                        for (Host host : hosts){
                            if (host.getId().equals(program.getHostId())){
                                programa.setFotoLocutorPrograma("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/" + host.getPicture());
                                programa.setNomeLocutorPrograma(host.getName());
                                break;
                            }
                        }
                        String startTime = program.getStartTime();
                        String[] startTimeSplit = startTime.split(":");
                        int hora = Integer.parseInt(startTimeSplit[0]);
                        int minuto = Integer.parseInt(startTimeSplit[1]);
                        quinta.set(Calendar.HOUR_OF_DAY,hora);
                        quinta.set(Calendar.MINUTE,minuto);
                        quinta.set(Calendar.SECOND, 0);
                        programa.setDataPrograma(quinta);
                        for (String programaNotificar : programasNotificar){
                            String diaPrograma = programaNotificar.replaceAll("[^A-Za-z]+", "");
                            long idPrograma = Long.parseLong(programaNotificar.replaceAll("\\D+",""));
                            if (diaPrograma.equals("quinta") && idPrograma == program.getId().longValue()){
                                programa.setNotificarPrograma(true);
                                break;
                            }else {
                                programa.setNotificarPrograma(false);
                            }
                        }
                        programa.setIdPrograma(program.getId());
                        programa.setDiaPrograma("quinta");
                        data.add(programa);
                    }
                    break;
                case "sexta":
                    if (program.getFri()){
                        Programa programa = new Programa();
                        programa.setHoraInicioPrograma(program.getStartTime());
                        programa.setNomePrograma(program.getName());
                        for (Host host : hosts){
                            if (host.getId().equals(program.getHostId())){
                                programa.setFotoLocutorPrograma("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/" + host.getPicture());
                                programa.setNomeLocutorPrograma(host.getName());
                                break;
                            }
                        }
                        String startTime = program.getStartTime();
                        String[] startTimeSplit = startTime.split(":");
                        int hora = Integer.parseInt(startTimeSplit[0]);
                        int minuto = Integer.parseInt(startTimeSplit[1]);
                        sexta.set(Calendar.HOUR_OF_DAY,hora);
                        sexta.set(Calendar.MINUTE,minuto);
                        sexta.set(Calendar.SECOND, 0);
                        programa.setDataPrograma(sexta);
                        for (String programaNotificar : programasNotificar){
                            String diaPrograma = programaNotificar.replaceAll("[^A-Za-z]+", "");
                            long idPrograma = Long.parseLong(programaNotificar.replaceAll("\\D+",""));
                            if (diaPrograma.equals("sexta") && idPrograma == program.getId().longValue()){
                                programa.setNotificarPrograma(true);
                                break;
                            }else {
                                programa.setNotificarPrograma(false);
                            }
                        }
                        programa.setIdPrograma(program.getId());
                        programa.setDiaPrograma("sexta");
                        data.add(programa);
                    }
                    break;
                case "sabado":
                    if (program.getSat()){
                        Programa programa = new Programa();
                        programa.setHoraInicioPrograma(program.getStartTime());
                        programa.setNomePrograma(program.getName());
                        for (Host host : hosts){
                            if (host.getId().equals(program.getHostId())){
                                programa.setFotoLocutorPrograma("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/" + host.getPicture());
                                programa.setNomeLocutorPrograma(host.getName());
                                break;
                            }
                        }
                        String startTime = program.getStartTime();
                        String[] startTimeSplit = startTime.split(":");
                        int hora = Integer.parseInt(startTimeSplit[0]);
                        int minuto = Integer.parseInt(startTimeSplit[1]);
                        sabado.set(Calendar.HOUR_OF_DAY,hora);
                        sabado.set(Calendar.MINUTE,minuto);
                        sabado.set(Calendar.SECOND, 0);
                        programa.setDataPrograma(sabado);
                        for (String programaNotificar : programasNotificar){
                            String diaPrograma = programaNotificar.replaceAll("[^A-Za-z]+", "");
                            long idPrograma = Long.parseLong(programaNotificar.replaceAll("\\D+",""));
                            if (diaPrograma.equals("sabado") && idPrograma == program.getId().longValue()){
                                programa.setNotificarPrograma(true);
                                break;
                            }else {
                                programa.setNotificarPrograma(false);
                            }
                        }
                        programa.setIdPrograma(program.getId());
                        programa.setDiaPrograma("sabado");
                        data.add(programa);
                    }
                    break;
                case "domingo":
                    if (program.getSun()){
                        Programa programa = new Programa();
                        programa.setHoraInicioPrograma(program.getStartTime());
                        programa.setNomePrograma(program.getName());
                        for (Host host : hosts){
                            if (host.getId().equals(program.getHostId())){
                                programa.setFotoLocutorPrograma("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/" + host.getPicture());
                                programa.setNomeLocutorPrograma(host.getName());
                                break;
                            }
                        }
                        String startTime = program.getStartTime();
                        String[] startTimeSplit = startTime.split(":");
                        int hora = Integer.parseInt(startTimeSplit[0]);
                        int minuto = Integer.parseInt(startTimeSplit[1]);
                        domingo.set(Calendar.HOUR_OF_DAY,hora);
                        domingo.set(Calendar.MINUTE,minuto);
                        domingo.set(Calendar.SECOND, 0);
                        programa.setDataPrograma(domingo);
                        for (String programaNotificar : programasNotificar){
                            String diaPrograma = programaNotificar.replaceAll("[^A-Za-z]+", "");
                            long idPrograma = Long.parseLong(programaNotificar.replaceAll("\\D+",""));
                            if (diaPrograma.equals("domingo") && idPrograma == program.getId().longValue()){
                                programa.setNotificarPrograma(true);
                                break;
                            }else {
                                programa.setNotificarPrograma(false);
                            }
                        }
                        programa.setIdPrograma(program.getId());
                        programa.setDiaPrograma("domingo");
                        data.add(programa);
                    }
                    break;
                default:
                    break;
            }
        }
        return data;
    }

}
