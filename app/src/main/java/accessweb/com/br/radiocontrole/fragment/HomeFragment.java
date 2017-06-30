package accessweb.com.br.radiocontrole.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.GridIconAdapter;

import static accessweb.com.br.radiocontrole.R.id.gravarAudio;
import static accessweb.com.br.radiocontrole.R.id.tempoAudio;


public class HomeFragment extends Fragment {

    private FloatingActionButton btnHomePlayPause;

    private GridView gridViewSocial;
    List<Integer> socialIconIds = new ArrayList<Integer>();
    List<String> socialLinks = new ArrayList<String>();

    private GridView gridViewContato;
    List<Integer> contatoIconIds = new ArrayList<Integer>();
    List<String> contatoLinks = new ArrayList<String>();
    List<String> contatoTipo = new ArrayList<String>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnHomePlayPause = (FloatingActionButton) rootView.findViewById(R.id.btnHomePlayPause);

        // ALIMENTANDO LISTAS DAS REDES SOCIAIS
        socialIconIds.add(R.drawable.ic_facebook_gray);
        socialIconIds.add(R.drawable.ic_twitter_gray);
        socialIconIds.add(R.drawable.ic_youtube_play_gray);
        socialIconIds.add(R.drawable.ic_instagram_gray);
        socialIconIds.add(R.drawable.ic_google_plus_gray);
        socialLinks.add("https://www.facebook.com/jovempan/");
        socialLinks.add("https://www.youtube.com/user/portaljovempan");
        socialLinks.add("https://www.instagram.com/radiojovempan/?hl=pt-br");
        socialLinks.add("https://plus.google.com/100662291740097839613");

        gridViewSocial = (GridView) rootView.findViewById(R.id.gridViewSocial);
        gridViewSocial.setAdapter(new GridIconAdapter(getContext(), socialIconIds));
        gridViewSocial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                try {
                    Intent intentSocial = new Intent(Intent.ACTION_VIEW, Uri.parse(socialLinks.get(position)));
                    startActivity(intentSocial);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        // ALIMENTANDO LISTA DE CONTATOS
        contatoIconIds.add(R.drawable.ic_map_marker_gray);
        contatoIconIds.add(R.drawable.ic_phone_gray);
        contatoIconIds.add(R.drawable.ic_whatsapp_gray);
        contatoIconIds.add(R.drawable.ic_web_gray);
        contatoIconIds.add(R.drawable.ic_email_gray);
        contatoLinks.add("Av. Rep. Argentina, 2056 - Água Verde, Curitiba - PR");
        contatoLinks.add("(41) 3333-3333");
        contatoLinks.add("(41) 99999-9999");
        contatoLinks.add("http://www.accessweb.com.br");
        contatoLinks.add("suporte@accessweb.com.br");
        contatoTipo.add("endereco");
        contatoTipo.add("telefone");
        contatoTipo.add("whatsapp");
        contatoTipo.add("site");
        contatoTipo.add("email");

        gridViewContato = (GridView) rootView.findViewById(R.id.gridViewContato);
        gridViewContato.setAdapter(new GridIconAdapter(getContext(), contatoIconIds));
        gridViewContato.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Log.v("aaaa", "" + contatoTipo.get(position));
                switch (contatoTipo.get(position)) {
                    case "endereco":
                        /*try {
                            Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.br/maps/place/" + contatoLinks.get(position)));
                            startActivity(intentContato);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }*/
                        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
                        intentMapa.setData(Uri.parse("geo:0,0?q=" + Uri.encode(contatoLinks.get(position))));
                        intentMapa.setPackage("com.google.android.apps.maps");
                        if (intentMapa.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(intentMapa);
                        }
                        break;
                    case "telefone":
                        final String numeroTelefone = contatoLinks.get(position).replaceAll("[^\\d.]", "");
                        Log.v("aaaa", "" + numeroTelefone);
                        AlertDialog.Builder customBuilder  = new AlertDialog.Builder(getActivity());
                        customBuilder .setTitle("Ligar");
                        customBuilder .setMessage("Você deseja ligar para a rádio?");
                        customBuilder .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri call = Uri.parse("tel:" + numeroTelefone);
                                try {
                                    Intent ligar = new Intent(Intent.ACTION_DIAL, call);
                                    startActivity(ligar);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });

                        customBuilder .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = customBuilder.create();

                        dialog.show();
                        Button btnNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        btnNegative.setTextColor(getResources().getColor(R.color.colorPrimary));

                        Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        btnPositive.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case "whatsapp":
                        final String numeroWhatsapp = contatoLinks.get(position).replaceAll("[^\\d.]", "");

                        try {
                            Intent intentWhatsapp = new Intent("android.intent.action.MAIN");
                            //intentWhatsapp.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                            intentWhatsapp.setAction(Intent.ACTION_SEND);
                            intentWhatsapp.setType("text/plain");
                            intentWhatsapp.putExtra("jid", PhoneNumberUtils.stripSeparators(numeroWhatsapp)+"@s.whatsapp.net");
                            intentWhatsapp.setPackage("com.whatsapp");
                            startActivity(intentWhatsapp);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "site":
                        try {
                            Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(contatoLinks.get(position)));
                            startActivity(intentContato);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "email":
                        Log.v("aaaa", "" + contatoLinks.get(position));
                        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                        intentEmail.setData(Uri.parse("mailto:"));
                        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{contatoLinks.get(position)});
                        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Contato realizado através do aplicativo");
                        if (intentEmail.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(intentEmail);
                        }
                        break;
                    default:
                        break;
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void changeIcon(String acao) {
        if (acao.equals("play")){
            btnHomePlayPause.setImageResource(R.drawable.ic_play_white);
        }else if (acao.equals("pause")) {
            btnHomePlayPause.setImageResource(R.drawable.ic_pause_white);
        }

    }
}
