package accessweb.com.br.radiocontrole.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;

import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.activity.MainActivity;
import accessweb.com.br.radiocontrole.fragment.PerfilFragment;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoSyncClientManager;

import static accessweb.com.br.radiocontrole.R.drawable.et;
import static accessweb.com.br.radiocontrole.R.drawable.ge;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Des. Android on 28/06/2017.
 */

public class EditarPerfilDialogFragment extends DialogFragment {

    private static String TAG = EditarPerfilDialogFragment.class.getSimpleName();
    private Toolbar toolbar;
    private EditText inputNome;
    private Button btnSalvarInformacoes;

    private ProgressDialog waitDialog;

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

        CacheData cacheData = new CacheData(getContext());

        View rootView = inflater.inflate(R.layout.fragment_editar_perfil, null, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.editarPerfilToolbar);
        toolbar.setTitle("Editar Perfil");
        toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
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

        inputNome = (EditText) rootView.findViewById(R.id.inputNome);
        inputNome.setText(cacheData.getString("userNome"));
        inputNome.setSelection(inputNome.getText().length());

        btnSalvarInformacoes = (Button) rootView.findViewById(R.id.btnSalvarInformacoes);
        btnSalvarInformacoes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn btnSalvarInformacoes");

                showWaitDialog("Salvando informações...");
                Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");
                CacheData cacheData = new CacheData(getContext());
                profileData.put("name", inputNome.getText().toString());
                profileData.put("email", cacheData.getString("userEmail"));
                profileData.put("phone", cacheData.getString("userTelefone"));
                profileData.put("picture", cacheData.getString("userUrlFoto"));
                profileData.synchronize(new SyncCallback() {

                    @Override
                    public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                        Log.d(TAG, "Sync success" + dataset);
                        closeWaitDialog();
                        System.out.println(dataset.get("name"));
                        CacheData cacheData = new CacheData(getContext());
                        cacheData.putString("userNome", dataset.get("name"));
                        dismiss();
                        ((MainActivity)getActivity()).abrirPerfil();

                    }

                    @Override
                    public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                        return false;
                    }

                    @Override
                    public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                        return false;
                    }

                    @Override
                    public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                        return false;
                    }

                    @Override
                    public void onFailure(DataStorageException dse) {

                    }
                });
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
        CacheData cacheData = new CacheData(getContext());
        if (Build.VERSION.SDK_INT > 23) {
            float[] hsv = new float[3];
            int color = Color.parseColor(cacheData.getString("color"));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(color);
        }
        return dialog;
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(getContext());
        waitDialog.setMessage(message);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }

}
