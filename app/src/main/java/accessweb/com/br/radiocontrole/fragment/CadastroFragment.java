package accessweb.com.br.radiocontrole.fragment;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.activity.SignUpConfirm;
import accessweb.com.br.radiocontrole.adapter.PaisAdapter;
import accessweb.com.br.radiocontrole.model.Pais;
import accessweb.com.br.radiocontrole.util.AppHelper;
import accessweb.com.br.radiocontrole.util.BrPhoneNumberFormatter;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.CognitoSyncClientManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Des. Android on 27/06/2017.
 */

public class CadastroFragment extends Fragment {
    private static String TAG = ProgramacaoDiaFragment.class.getSimpleName();
    private EditText inputNome;
    private EditText inputEmail;
    private EditText inputTelefone;
    private EditText inputSenha;
    private Button btnCadastrar;
    private ProgressDialog waitDialog;
    private AlertDialog userDialog;
    protected Spinner mSpinner;
    protected PaisAdapter mAdapter;
    private List<Pais> paises = new ArrayList<>();
    private String codigoPais;

    public CadastroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cadastro, container, false);

        mSpinner = (Spinner) rootView.findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mAdapter = new PaisAdapter(getActivity(), getData());
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(27);

        /*CognitoCachingCredentialsProvider provider = new CognitoCachingCredentialsProvider(getContext(), "us-east-1:9434eddb-ce1a-4204-bb3b-4a7f88b97b17", Regions.US_EAST_1);

        System.out.println(provider.getIdentityId());*/

        inputNome = (EditText) rootView.findViewById(R.id.inputNome);
        inputEmail = (EditText) rootView.findViewById(R.id.inputEmail);
        inputTelefone = (EditText) rootView.findViewById(R.id.inputTelefone);
        inputSenha = (EditText) rootView.findViewById(R.id.inputSenha);
        btnCadastrar = (Button) rootView.findViewById(R.id.btnCadastrar);

        BrPhoneNumberFormatter addLineNumberFormatter = new BrPhoneNumberFormatter(new WeakReference<EditText>(inputTelefone));
        inputTelefone.addTextChangedListener(addLineNumberFormatter);

        /*inputSenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (inputSenha.getText().toString().trim().length() < 6) {
                        inputSenha.setError("Sua senha deve conter no mínimo 6 dígitos.");
                    } else {
                        inputSenha.setError(null);
                    }
                } else {
                    if (inputSenha.getText().toString().trim().length() < 6) {
                        inputSenha.setError("Sua senha deve conter no mínimo 6 dígitos.");
                    } else {
                        inputSenha.setError(null);
                    }
                }
            }
        });*/

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn Cadastrar");

                if (inputNome.getText().toString().equals("")) {
                    showDialogMessage("Rádio Controle", "Por favor, informe um nome válido.");
                } else if (inputTelefone.getText().toString().trim().length() < 14) {
                    showDialogMessage("Rádio Controle", "Por favor, informe um telefone válido.");
                } else if (inputSenha.getText().toString().trim().length() < 6) {
                    showDialogMessage("Rádio Controle", "Por favor, informe uma senha válida.");
                } else {
                    ClientConfiguration clientConfiguration = new ClientConfiguration();

                    // Create a CognitoUserPool object to refer to your user pool
                    CognitoUserPool userPool = new CognitoUserPool(getContext(), "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);

                    // Create a CognitoUserAttributes object and add user attributes
                    CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                    Pais pais = (Pais) mSpinner.getSelectedItem();
                    userAttributes.addAttribute("phone_number", pais.getCodigoPais() + String.valueOf(inputTelefone.getText().toString().trim().replaceAll("\\D+","")));
                    //Log.e("codigoPais", "" + pais.getCodigoPais() + String.valueOf(inputTelefone.getText().toString().trim().replaceAll("\\D+","")));
                    userAttributes.addAttribute("email", String.valueOf(inputEmail.getText()));

                    showWaitDialog("Cadastrando...");
                    Log.d(TAG, "S))))))))))))))))))" + CognitoClientManager.getCredentials().getIdentityId());
                    userPool.signUpInBackground( String.valueOf(inputEmail.getText()).toLowerCase(),  String.valueOf(inputSenha.getText()), userAttributes, null, signupCallback);
                }
            }
        });

        return rootView;
    }

    SignUpHandler signupCallback = new SignUpHandler() {

        @Override
        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Sign-up was successful

            CognitoSyncManager client = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1,
                    CognitoClientManager.getCredentials());
            Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");
            profileData.put("name", inputNome.getText().toString());
            profileData.put("email", inputEmail.getText().toString().toLowerCase());
            profileData.put("phone", inputTelefone.getText().toString().toLowerCase());
            profileData.synchronize(new SyncCallback() {

                @Override
                public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                    Log.d(TAG, "Sync success");
                }

                @Override
                public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                    Log.d(TAG, "Conflict");
                    return false;
                }

                @Override
                public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                    Log.d(TAG, "Dataset deleted");
                    return false;
                }

                @Override
                public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                    Log.d(TAG, "Datasets merged");
                    return false;
                }

                @Override
                public void onFailure(DataStorageException dse) {
                    Log.e(TAG, "Sync fails", dse);
                }
            });

            Log.e("onSuccess", "" + cognitoUserCodeDeliveryDetails);
            closeWaitDialog();
            // Check if this user (cognitoUser) needs to be confirmed
            if(!userConfirmed) {
                //showDialogMessage("Rádio Controle", "Cadastro realizado com sucesso, ");
                confirmSignUp(cognitoUserCodeDeliveryDetails);
            }
            else {
                // The user has already been confirmed
            }
        }

        @Override
        public void onFailure(Exception exception) {
            // Sign-up failed, check exception for the cause
            closeWaitDialog();
            if (AppHelper.formatException(exception).equals("User already exists ")){
                showDialogMessage("Rádio Controle", "Erro ao realizar cadastro, usuário já cadastrado.");
            } else {
                showDialogMessage("Rádio Controle", "Erro ao realizar cadastro, verifique os campos e tente novamente.");
            }
        }
    };

    private void clearInputs() {
        inputNome.setText("");
        inputEmail.setText("");
        inputTelefone.setText("");
        inputSenha.setText("");
    }

    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
        Intent intent = new Intent(getContext(), SignUpConfirm.class);
        intent.putExtra("source","signup");
        intent.putExtra("name", inputEmail.getText().toString().toLowerCase());
        intent.putExtra("destination", cognitoUserCodeDeliveryDetails.getDestination());
        intent.putExtra("deliveryMed", cognitoUserCodeDeliveryDetails.getDeliveryMedium());
        intent.putExtra("attribute", cognitoUserCodeDeliveryDetails.getAttributeName());
        startActivityForResult(intent, 10);
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    /*if(exit) {
                        exit(usernameInput);
                    }*/
                } catch (Exception e) {
                    /*if(exit) {
                        exit(usernameInput);
                    }*/
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
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

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("paises.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public List<Pais> getData() {
        List<Pais> data = new ArrayList<>();

        try {
            JSONArray paisesArray = new JSONArray(readJSONFromAsset());
            for (int i = 0; i < paisesArray.length(); i++){
                Pais pais = new Pais();
                JSONObject paisJSON = paisesArray.getJSONObject(i);
                pais.setBandeiraPais(paisJSON.getString("iso").toLowerCase());
                pais.setNomePais(paisJSON.getString("nome"));
                pais.setCodigoPais("+" + paisJSON.getString("fone"));
                if (paisJSON.getString("iso").toLowerCase().equals("br")){
                    pais.setPrincipal(true);
                }else {
                    pais.setPrincipal(false);
                }
                data.add(pais);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (data.size() > 0) {
            Collections.sort(data, new Comparator<Pais>() {
                @Override
                public int compare(final Pais object1, final Pais object2) {
                    return object1.getNomePais().compareTo(object2.getNomePais());
                }
            });
        }

        return data;
    }
    protected AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Pais pais = (Pais) mSpinner.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}