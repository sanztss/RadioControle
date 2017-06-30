package accessweb.com.br.radiocontrole.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import accessweb.com.br.radiocontrole.R;

/**
 * Created by Des. Android on 30/06/2017.
 */

public class SobreDialogFragment extends DialogFragment {

    private static String TAG = SobreDialogFragment.class.getSimpleName();
    private Toolbar toolbar;
    private TextView facebook;
    private TextView twitter;
    private TextView email;
    private TextView telefone;

    public SobreDialogFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sobre, null, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.editarPerfilToolbar);
        toolbar.setTitle("Sobre o aplicativo");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        facebook = (TextView) rootView.findViewById(R.id.facebook);
        twitter = (TextView) rootView.findViewById(R.id.twitter);
        email = (TextView) rootView.findViewById(R.id.email);
        telefone = (TextView) rootView.findViewById(R.id.telefone);

        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Facebook");
                try {
                    Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/accessweb"));
                    startActivity(intentContato);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Twitter");
                try {
                    Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/AccessInf"));
                    startActivity(intentContato);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Email");
                Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                intentEmail.setData(Uri.parse("mailto:"));
                intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"marketing@accessweb.com.br"});
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Contato realizado através do aplicativo Rádio Controle");
                if (intentEmail.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intentEmail);
                }
            }
        });
        telefone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Telefone");
                AlertDialog.Builder customBuilder  = new AlertDialog.Builder(getActivity());
                customBuilder .setTitle("Ligar");
                customBuilder .setMessage("Você deseja efetuar a ligação\npara (41) 3329-7494?");
                customBuilder .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri call = Uri.parse("tel:04133297494");
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

                AlertDialog dialogLigar = customBuilder.create();

                dialogLigar.show();
                Button btnNegative = dialogLigar.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnNegative.setTextColor(getResources().getColor(R.color.colorPrimary));

                Button btnPositive = dialogLigar.getButton(DialogInterface.BUTTON_POSITIVE);
                btnPositive.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return dialog;
    }

}
