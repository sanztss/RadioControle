package accessweb.com.br.radiocontrole.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.ampiri.sdk.banner.BannerAd;
import com.ampiri.sdk.banner.BannerAdPool;
import com.ampiri.sdk.listeners.BannerAdCallback;
import com.ampiri.sdk.mediation.BannerSize;
import com.ampiri.sdk.mediation.ResponseStatus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.activity.MainActivity;
import accessweb.com.br.radiocontrole.adapter.GridIconAdapter;
import accessweb.com.br.radiocontrole.model.Settings;
import accessweb.com.br.radiocontrole.model.Social;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;
import accessweb.com.br.radiocontrole.util.CacheData;

public class HomeFragment extends Fragment {

    private ImageView logo;
    private FloatingActionButton btnHomePlayPause;
    private TextView txtRedesSociais;
    private TextView txtContato;

    private GridView gridViewSocial;
    List<Integer> socialIconIds = new ArrayList<Integer>();
    List<String> socialLinks = new ArrayList<String>();

    private GridView gridViewContato;
    List<Integer> contatoIconIds = new ArrayList<Integer>();
    List<String> contatoLinks = new ArrayList<String>();
    List<String> contatoTipo = new ArrayList<String>();

    private BannerAd bannerAd;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final CacheData cacheData = new CacheData(getContext());

        // INSTANCIANDO A LOGO INSERINDO NA TELA
        logo = (ImageView) rootView.findViewById(R.id.logo);
        Picasso.with(getContext())
                .load(cacheData.getString("logo"))
                .error(R.drawable.radio_controle)
                .into(logo);

        // INSTANCIANDO BOTÃO E ALTERANDO COR
        btnHomePlayPause = (FloatingActionButton) rootView.findViewById(R.id.btnHomePlayPause);
        btnHomePlayPause.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(cacheData.getString("color"))));

        // INSTANCIANDO TEXTVIEW E ALTERANDO COR
        txtRedesSociais = (TextView) rootView.findViewById(R.id.txtRedesSociais);
        txtRedesSociais.setTextColor(ColorStateList.valueOf(Color.parseColor(cacheData.getString("color"))));
        txtContato = (TextView) rootView.findViewById(R.id.txtContato);
        txtContato.setTextColor(ColorStateList.valueOf(Color.parseColor(cacheData.getString("color"))));

        contatoIconIds.clear();
        contatoLinks.clear();
        contatoTipo.clear();

        socialIconIds.clear();
        socialLinks.clear();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                Settings settings = client.radioIdGet(cacheData.getString("idRadio"));

                // RECUPERANDO AS REDES SOCIAIS
                for (Social social : settings.getSocial()){
                    switch (social.getType().toString()){
                        case "website":
                            contatoIconIds.add(R.drawable.ic_web_gray);
                            contatoLinks.add(social.getValue().toString());
                            contatoTipo.add("site");
                            break;
                        case "email":
                            contatoIconIds.add(R.drawable.ic_email_gray);
                            contatoLinks.add(social.getValue().toString());
                            contatoTipo.add("email");
                            break;
                        case "phone":
                            contatoIconIds.add(R.drawable.ic_phone_gray);
                            contatoLinks.add(social.getValue().toString());
                            contatoTipo.add("telefone");
                            break;
                        case "whatsapp":
                            contatoIconIds.add(R.drawable.ic_whatsapp_gray);
                            contatoLinks.add(social.getValue().toString());
                            contatoTipo.add("whatsapp");
                            break;
                        case "endereco":
                            contatoIconIds.add(R.drawable.ic_map_marker_gray);
                            contatoLinks.add(social.getValue().toString());
                            contatoTipo.add("endereco");
                            break;
                        case "facebook":
                            socialIconIds.add(R.drawable.ic_facebook_gray);
                            socialLinks.add(social.getValue().toString());
                            break;
                        case "twitter":
                            socialIconIds.add(R.drawable.ic_twitter_gray);
                            socialLinks.add(social.getValue().toString());
                            break;
                        case "instagram":
                            socialIconIds.add(R.drawable.ic_instagram_gray);
                            socialLinks.add(social.getValue().toString());
                            break;
                        case "googleplus":
                            socialIconIds.add(R.drawable.ic_google_plus_gray);
                            socialLinks.add(social.getValue().toString());
                            break;
                        case "youtube":
                            socialIconIds.add(R.drawable.ic_youtube_play_gray);
                            socialLinks.add(social.getValue().toString());
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
                // ALIMENTANDO LISTAS DAS REDES SOCIAIS
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
                gridViewContato = (GridView) rootView.findViewById(R.id.gridViewContato);
                gridViewContato.setAdapter(new GridIconAdapter(getContext(), contatoIconIds));
                gridViewContato.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id){

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
                                btnNegative.setTextColor(Color.parseColor(cacheData.getString("color")));

                                Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                btnPositive.setTextColor(Color.parseColor(cacheData.getString("color")));
                                break;
                            case "whatsapp":
                                final String numeroWhatsapp = contatoLinks.get(position).replaceAll("[^\\d.]", "");

                                Log.e("whats", "" + numeroWhatsapp);
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

                                    AlertDialog.Builder customBuilderWhatstapp  = new AlertDialog.Builder(getActivity());
                                    customBuilderWhatstapp .setTitle("Whatsapp");
                                    customBuilderWhatstapp .setMessage("Você não possui o aplicativo instalado em seu dispositivo.");
                                    customBuilderWhatstapp .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog dialogWhatsapp = customBuilderWhatstapp.create();

                                    dialogWhatsapp.show();

                                    Button btnPositiveWhatsapp = dialogWhatsapp.getButton(DialogInterface.BUTTON_NEUTRAL);
                                    btnPositiveWhatsapp.setTextColor(Color.parseColor(cacheData.getString("color")));
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
            }
        }.execute();

        BannerAdCallback bannerAdListener = new BannerAdCallback() {
            @Override
            public void onAdLoaded(@NonNull BannerAd ad) {

            }

            @Override
            public void onAdFailed(@NonNull BannerAd AD, @NonNull ResponseStatus responseStatus) {

            }

            @Override
            public void onAdOpened(@NonNull BannerAd ad) {

            }

            @Override
            public void onAdClicked(@NonNull BannerAd ad) {

            }

            @Override
            public void onAdClosed(@NonNull BannerAd ad) {

            }
        };

        FrameLayout adView = (FrameLayout) rootView.findViewById(R.id.anuncio);
        bannerAd = BannerAdPool.load(getActivity(),"3dfbb889-3bcd-4c34-82ae-8fcb539c3b25",  adView, BannerSize.BANNER_SIZE_320x50);
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).trocarTituloToolbar();
    }

    public void changeIcon(String acao) {
        if (acao.equals("play")){
            btnHomePlayPause.setImageResource(R.drawable.ic_play_white);
        }else if (acao.equals("pause")) {
            btnHomePlayPause.setImageResource(R.drawable.ic_stop_white);
        }

    }
}
