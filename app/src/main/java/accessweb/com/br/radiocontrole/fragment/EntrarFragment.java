package accessweb.com.br.radiocontrole.fragment;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.activity.MainActivity;
import accessweb.com.br.radiocontrole.util.ActivityResultBus;
import accessweb.com.br.radiocontrole.util.ActivityResultEvent;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Des. Android on 27/06/2017.
 */

public class EntrarFragment extends Fragment {
    private static String TAG = ProgramacaoDiaFragment.class.getSimpleName();
    private TextView inputEmail;
    private TextView inputSenha;
    private Button btnLogin;
    CallbackManager callbackManager;
    JSONObject resposta, profile_pic_data, profile_pic_url;

    public EntrarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //final CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(), "us-east-1:9434eddb-ce1a-4204-bb3b-4a7f88b97b17", Regions.US_EAST_1);

        callbackManager = CallbackManager.Factory.create();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_entrar, container, false);

        inputEmail = (TextView) rootView.findViewById(R.id.inputEmail);
        inputSenha = (TextView) rootView.findViewById(R.id.inputSenha);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn Entrar");
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("fragment_dialog");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    df.dismiss();
                }
            }
        });

        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.connectWithFbButton);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("Resultado: ", "sucesso!");
                getUserDetails(loginResult);

                CognitoClientManager.addLogins("graph.facebook.com", AccessToken.getCurrentAccessToken().getToken());

                new InsertUserTask().execute();
            }

            @Override
            public void onCancel() {
                Log.v("Resultado: ", "cancelado!");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("Resultado: ", "erro!");
            }
        });


        return rootView;
    }

    /*@Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("fragment result", "aaaa");
    }*/

    public void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json_object, GraphResponse response) {
                        try {
                            resposta = json_object;
                            Log.v("Id facebook: ", resposta.get("id").toString());
                            Log.v("Email facebook: ", resposta.get("email").toString());
                            Log.v("Nome facebook: ", resposta.get("name").toString());
                            profile_pic_data = new JSONObject(resposta.get("picture").toString());
                            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                            Log.v("Url foto facebook: ", profile_pic_url.getString("url"));

                            SharedPreferences sharedPrefs = getActivity().getSharedPreferences("UserData", 0);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString("userId", resposta.get("id").toString());
                            editor.putString("userEmail", resposta.get("email").toString());
                            editor.putString("userNome", resposta.get("name").toString());
                            editor.putString("userUrlFoto", profile_pic_url.getString("url"));
                            editor.commit();
                            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("fragment_dialog");
                            if (prev != null) {
                                DialogFragment df = (DialogFragment) prev;
                                ((MainActivity)getActivity()).abrirPerfil();
                                df.dismiss();
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(300).height(300)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    class InsertUserTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            String userID = null;
            if (CognitoClientManager.isAuthenticated()) {
                userID = CognitoClientManager.getCredentials().getIdentityId();
                Log.i(TAG, "userID = " + userID);

                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("UserData", 0);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("cognitoId", userID);
                editor.commit();

            } else {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (CognitoClientManager.isAuthenticated()) {

            }
        }
    }
}
