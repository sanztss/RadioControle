package accessweb.com.br.radiocontrole.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.util.AppHelper;
import accessweb.com.br.radiocontrole.util.CacheData;

import static java.security.AccessController.getContext;

/**
 * Created by Des. Android on 11/07/2017.
 */

public class SignUpConfirm extends AppCompatActivity {
    private EditText userName;
    private EditText confCode;

    private Button confirm;
    private TextView reqCode;
    private String usuarioArgs;
    private AlertDialog userDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CacheData cacheData = new CacheData(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Confirmação");
        toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT > 23) {
            float[] hsv = new float[3];
            int color = Color.parseColor(cacheData.getString("color"));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(color);
        }
        init();
    }

    private void init() {

        userName = (EditText) findViewById(R.id.editTextConfirmUserId);
        confCode = (EditText) findViewById(R.id.editTextConfirmCode);

        Bundle extras = getIntent().getExtras();
        if (extras !=null) {
            if(extras.containsKey("name")) {
                usuarioArgs = extras.getString("name");
                userName.setText(usuarioArgs);

                confCode.requestFocus();

                if(extras.containsKey("destination")) {
                    String dest = extras.getString("destination");
                    String delMed = extras.getString("deliveryMed");

                    TextView screenSubtext = (TextView) findViewById(R.id.textViewConfirmSubtext_1);
                    if(dest != null && delMed != null && dest.length() > 0 && delMed.length() > 0) {
                        screenSubtext.setText("Um código de configmação foi enviado para "+dest+" via "+delMed+".");
                    }
                    else {
                        screenSubtext.setText("Um código de configmação foi enviado.");
                    }
                }
            }
            else {
                TextView screenSubtext = (TextView) findViewById(R.id.textViewConfirmSubtext_1);
                screenSubtext.setText("Confirmar cadastro realizado.");
            }

        }

        confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendConfCode();
            }
        });

        reqCode = (TextView) findViewById(R.id.resend_confirm_req);
        reqCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqConfCode();
            }
        });
    }



    private void sendConfCode() {
        usuarioArgs = userName.getText().toString().toLowerCase();
        String confirmCode = confCode.getText().toString();

        if(usuarioArgs == null || usuarioArgs.length() < 1) {
            showDialogMessage("Rádio Controle","Por favor, informe o nome de usuário.", false);
            return;
        }else if(confirmCode == null || confirmCode.length() < 1) {
            showDialogMessage("Rádio Controle","Por favor, informe o código de confirmação.", false);
            return;
        }else {
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            CognitoUserPool userPool = new CognitoUserPool(this, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
            userPool.getUser(usuarioArgs).confirmSignUpInBackground(confirmCode, true, confHandler);
        }
    }

    private void reqConfCode() {
        usuarioArgs = userName.getText().toString().toLowerCase();
        if(usuarioArgs == null || usuarioArgs.length() < 1) {
            showDialogMessage("Rádio Controle","Por favor, informe o nome de usuário.", false);
            return;
        }else{
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            CognitoUserPool userPool = new CognitoUserPool(this, "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
            userPool.getUser(usuarioArgs).resendConfirmationCodeInBackground(resendConfCodeHandler);
        }
    }

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            showDialogMessage("Rádio Controle", "O usuário " + usuarioArgs +" foi confirmado com sucesso!", true);
        }

        @Override
        public void onFailure(Exception exception) {
            showDialogMessage("Rádio Controle", "Erro ao realizar a confirmação, usuário ou código inválido, verifique os dados e tente novamente.", false);
        }
    };

    VerificationHandler resendConfCodeHandler = new VerificationHandler() {
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            showDialogMessage("Rádio Controle","Código enviado com sucesso para "+cognitoUserCodeDeliveryDetails.getDestination()+" via "+cognitoUserCodeDeliveryDetails.getDeliveryMedium()+".", false);
        }

        @Override
        public void onFailure(Exception exception) {
            showDialogMessage("Rádio Controle","Erro ao reenviar código, tente novamente mais tarde.", false);
        }
    };

    private void showDialogMessage(String title, String body, final boolean exitActivity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if(exitActivity) {
                        exit();
                    }
                } catch (Exception e) {
                    exit();
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit() {
        Intent intent = new Intent();
        if(usuarioArgs == null)
            usuarioArgs = "";
        intent.putExtra("name", usuarioArgs);
        setResult(RESULT_OK, intent);
        finish();
    }

}
