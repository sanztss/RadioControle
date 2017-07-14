package accessweb.com.br.radiocontrole.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.dialog.EditarPerfilDialogFragment;
import accessweb.com.br.radiocontrole.activity.MainActivity;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.CognitoSyncClientManager;
import de.hdodenhof.circleimageview.CircleImageView;

import static accessweb.com.br.radiocontrole.R.id.inputEmail;
import static accessweb.com.br.radiocontrole.R.id.inputNome;
import static accessweb.com.br.radiocontrole.R.id.inputTelefone;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;
import static android.R.attr.name;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Des. Android on 27/06/2017.
 */

public class PerfilFragment extends Fragment {

    private static String TAG = PerfilFragment.class.getSimpleName();
    private Button btnEditarPerfil;
    private Button btnAlterarFoto;
    private Button btnSair;
    private CircleImageView fotoUsuario;
    private TextView nomeUsuario;
    private TextView emailUsuario;
    private View bgFotoUsuario;
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    private Bitmap myBitmap;
    private Uri picUri;
    private Uri outputFileUri;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        if (android.os.Build.VERSION.SDK_INT > 23) {
            if (permissionsToRequest.size() > 0)
                getActivity().requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }else {

            int permission1 = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int permission2 = PermissionChecker.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);

            if (permission1 == PermissionChecker.PERMISSION_GRANTED) {

            } else {

            }

            if (permission2 == PermissionChecker.PERMISSION_GRANTED) {

            } else {

            }

        }


        fotoUsuario = (CircleImageView) rootView.findViewById(R.id.fotoUsuario);
        nomeUsuario = (TextView) rootView.findViewById(R.id.nomeUsuario);
        emailUsuario = (TextView) rootView.findViewById(R.id.emailUsuario);
        bgFotoUsuario = (View) rootView.findViewById(R.id.bgFotoUsuario);

        CacheData cacheData = new CacheData(getContext());
        bgFotoUsuario.setBackgroundColor(Color.parseColor(cacheData.getString("color")));

        if (!cacheData.getString("userUrlFoto").equals("")){
            Picasso.with(getContext())
                    .load(cacheData.getString("userUrlFoto"))
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(fotoUsuario);
        }

        nomeUsuario.setText(cacheData.getString("userNome"));
        emailUsuario.setText(cacheData.getString("userEmail"));

        btnEditarPerfil = (Button) rootView.findViewById(R.id.btnEditarPerfil);
        btnAlterarFoto = (Button) rootView.findViewById(R.id.btnAlterarFoto);
        btnSair = (Button) rootView.findViewById(R.id.btnSair);

        btnEditarPerfil.setTextColor(Color.parseColor(cacheData.getString("color")));
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn btnEditarPerfil");
                EditarPerfilDialogFragment dialog = new EditarPerfilDialogFragment();
                dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        btnAlterarFoto.setTextColor(Color.parseColor(cacheData.getString("color")));
        btnAlterarFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn btnAlterarFoto");
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn btnSair");

                LoginManager.getInstance().logOut();

                CacheData cacheData = new CacheData(getContext());
                cacheData.putString("userId", "");
                cacheData.putString("userEmail", "");
                cacheData.putString("userNome", "");
                cacheData.putString("userTelefone", "");
                cacheData.putString("userUrlFoto", "");

                ClientConfiguration clientConfiguration = new ClientConfiguration();
                CognitoUserPool userPool = new CognitoUserPool(getContext(), "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                userPool.getCurrentUser().signOut();
                ((MainActivity)getActivity()).fecharPerfil();
            }
        });
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

    public Intent getPickImageChooserIntent() {

        outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();

        // CAMERA INTENTS
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // GALERIA INTENTS
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // INTENT PRINCIPAL
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // CRIAR INTENT CHOOSER
        Intent chooserIntent = Intent.createChooser(mainIntent, "Selecione a origem");

        // ADCIONAR TODAS AS INTENTS
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {

        Uri outputFileUri = null;
        File getImage = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(!getImage.exists())
            getImage.mkdirs();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profilepic.png"));
        }
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;

        if (resultCode == Activity.RESULT_OK) {
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                Log.i("aaa", "" + picUri);
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);
                    //imgFoto.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");

                myBitmap = bitmap;
                //imgFoto.setImageBitmap(myBitmap);
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

}

