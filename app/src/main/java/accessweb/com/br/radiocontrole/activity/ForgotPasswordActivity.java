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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.util.CacheData;

import static accessweb.com.br.radiocontrole.R.id.inputSenha;

/**
 * Created by Des. Android on 12/07/2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText passwordInput;
    private EditText codeInput;
    private Button setPassword;
    private AlertDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CacheData cacheData = new CacheData(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Resetar Senha");
        toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit(null, null);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("destination")) {
                String dest = extras.getString("destination");
                String delMed = extras.getString("deliveryMed");
                TextView message = (TextView) findViewById(R.id.textViewForgotPasswordMessage);
                String textToDisplay = "Um código de verificação foi enviado para " + dest + " via " + delMed + ".";
                message.setText(textToDisplay);
            }
        }

        passwordInput = (EditText) findViewById(R.id.editTextForgotPasswordPass);
        passwordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (passwordInput.getText().toString().trim().length() < 6) {
                        passwordInput.setError("A senha deve conter no mínimo 6 dígitos.");
                    } else {
                        passwordInput.setError(null);
                    }
                } else {
                    if (passwordInput.getText().toString().trim().length() < 6) {
                        passwordInput.setError("A senha deve conter no mínimo 6 dígitos.");
                    } else {
                        passwordInput.setError(null);
                    }
                }
            }
        });
        codeInput = (EditText) findViewById(R.id.editTextForgotPasswordCode);

        setPassword = (Button) findViewById(R.id.forgotPassword_button);
        setPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getCode();
            }
        });
    }

    private void getCode() {
        String newPassword = passwordInput.getText().toString();

        if (newPassword == null || newPassword.length() < 1) {
            passwordInput.setError("A senha deve conter no mínimo 6 dígitos.");
            return;
        }

        String verCode = codeInput.getText().toString();

        if (verCode == null || verCode.length() < 6) {
            showDialogMessage("Rádio Controle", "Por favor, informe o código de verificação.");
            return;
        }
        exit(newPassword, verCode);
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    //
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit(String newPass, String code) {
        Intent intent = new Intent();
        if(newPass == null || code == null) {
            newPass = "";
            code = "";
        }
        intent.putExtra("newPass", newPass);
        intent.putExtra("code", code);
        setResult(RESULT_OK, intent);
        finish();
    }
}
