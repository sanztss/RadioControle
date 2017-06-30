package accessweb.com.br.radiocontrole.dialog;

import android.app.Activity;
import android.app.Dialog;
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
 * Created by Des. Android on 28/06/2017.
 */

public class EditarPerfilDialogFragment extends DialogFragment {

    private static String TAG = EscolherDialogFragment.class.getSimpleName();
    private Toolbar toolbar;
    private TextView inputNome;
    private TextView inputEmail;
    private TextView inputSenha;
    private Button btnSalvarInformacoes;
    public EditarPerfilDialogFragment() {

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

        View rootView = inflater.inflate(R.layout.fragment_editar_perfil, null, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.editarPerfilToolbar);
        toolbar.setTitle("Editar Perfil");
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

        inputNome = (TextView) rootView.findViewById(R.id.inputNome);
        inputEmail = (TextView) rootView.findViewById(R.id.inputEmail);
        inputSenha = (TextView) rootView.findViewById(R.id.inputSenha);

        btnSalvarInformacoes = (Button) rootView.findViewById(R.id.btnSalvarInformacoes);
        btnSalvarInformacoes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn btnSalvarInformacoes");
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
