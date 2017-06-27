package accessweb.com.br.radiocontrole.activity;

import accessweb.com.br.radiocontrole.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Des. Android on 27/06/2017.
 */

public class CadastroFragment extends Fragment {
    private static String TAG = ProgramacaoDiaFragment.class.getSimpleName();
    private TextView inputNome;
    private TextView inputEmail;
    private TextView inputSenha;
    private Button btnCadastrar;

    public CadastroFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_cadastro, container, false);

        inputNome = (TextView) rootView.findViewById(R.id.inputNome);
        inputEmail = (TextView) rootView.findViewById(R.id.inputEmail);
        inputSenha = (TextView) rootView.findViewById(R.id.inputSenha);
        btnCadastrar = (Button) rootView.findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn Cadastrar");
            }
        });

        return rootView;
    }

}