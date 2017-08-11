package accessweb.com.br.radiocontrole.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.ampiri.sdk.Ampiri;
import com.ampiri.sdk.banner.BannerAd;
import com.ampiri.sdk.banner.BannerAdPool;
import com.ampiri.sdk.listeners.BannerAdCallback;
import com.ampiri.sdk.mediation.BannerSize;
import com.ampiri.sdk.mediation.ResponseStatus;
import com.squareup.picasso.Picasso;
import android.provider.ContactsContract.Data;

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

import static accessweb.com.br.radiocontrole.R.id.btnRegulamento;
import static android.R.id.message;
import static com.ampiri.sdk.c.b.a.P;

public class HomeFragment extends Fragment {

    private ImageView logo;
    private FloatingActionButton btnHomePlayPause;
    private TextView txtRedesSociais;
    private TextView txtContato;

    private RelativeLayout relativeLogo;
    private LinearLayout areaAnuncio;
    private FrameLayout ampiri;
    private ImageView imagemAnuncio;

    private GridView gridViewSocial;
    List<Integer> socialIconIds = new ArrayList<Integer>();
    List<String> socialLinks = new ArrayList<String>();

    private GridView gridViewContato;
    List<Integer> contatoIconIds = new ArrayList<Integer>();
    List<String> contatoLinks = new ArrayList<String>();
    List<String> contatoTipo = new ArrayList<String>();

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

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
        relativeLogo = (RelativeLayout) rootView.findViewById(R.id.relativeLogo);
        areaAnuncio = (LinearLayout) rootView.findViewById(R.id.areaAnuncio);
        ampiri = (FrameLayout) rootView.findViewById(R.id.ampiri);
        imagemAnuncio = (ImageView) rootView.findViewById(R.id.imagemAnuncio);

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
                                checkAndRequestPermissions(numeroWhatsapp);


                                /*try {
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
                                }*/

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



        if (cacheData.getString("adsHomeContent").equals("off")){
            areaAnuncio.setVisibility(View.GONE);
            final float scale = getContext().getResources().getDisplayMetrics().density;
            int px = (int) (313 * scale + 0.5f);  // replace 100 with your dimensions
            relativeLogo.getLayoutParams().height = px;
        }else if (cacheData.getString("adsHomeContent").equals("ampiri")) {
            ampiri.setVisibility(View.VISIBLE);
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

            //FrameLayout adView = (FrameLayout) rootView.findViewById(R.id.anuncio);

            //bannerAd = BannerAdPool.load(getActivity(),"8160598b-936c-4886-b3a0-12c75b3a145f",  adView, BannerSize.BANNER_SIZE_320x50);
            bannerAd = BannerAdPool.load(getActivity(),cacheData.getString("adsHomeValue"),  ampiri, BannerSize.BANNER_SIZE_320x50);
        } else if (cacheData.getString("adsHomeContent").equals("image")) {
            ampiri.setVisibility(View.GONE);
            imagemAnuncio.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(cacheData.getString("adsHomeValue")).into(imagemAnuncio);
            imagemAnuncio.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(cacheData.getString("adsHomeLink")));
                        startActivity(intentContato);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

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

    public boolean contactExists(Context context, String number) {
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        }
        finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }

    private void addContact(String number) {
        CacheData cacheData = new CacheData(getContext());
        if (contactExists(getContext(), number)){
            System.out.println("Contato existe!");
            try {
                /*Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra(Intent.EXTRA_TEXT, " ");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/

                Uri mUri = Uri.parse("smsto:" + number);
                Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                mIntent.setPackage("com.whatsapp");
                mIntent.putExtra("chat",true);
                startActivity(Intent.createChooser(mIntent, "Conversar via:"));
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
        }else {
            System.out.println("Contato ainda não existe!");
            ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
            operationList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            // first and last names
            operationList.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, cacheData.getString("nomeRadio"))
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "- Rádio")
                    .build());

            operationList.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());

            try{
                ContentProviderResult[] results = getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationList);
                System.out.println(results.toString());
                try {
                    /*Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, " ");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setPackage("com.whatsapp");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);*/
                    Uri mUri = Uri.parse("smsto:" + number);
                    Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                    mIntent.setPackage("com.whatsapp");
                    mIntent.putExtra("chat",true);
                    startActivity(Intent.createChooser(mIntent, "Share with"));
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
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private  boolean checkAndRequestPermissions(String numero) {
        int lerContato = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
        int escreverContato = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (lerContato != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (escreverContato != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }else {
            addContact(numero);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        System.out.println("AAAAAAAAAA" + requestCode);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(getView(),"Permição Garantida, Clique novamente no ícone do Whatsapp.",Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(getView(),"Permição Negada, Para conversar via Whatsapp precisamos de sua permissão.",Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}
