package accessweb.com.br.radiocontrole.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
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
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.dialog.EditarPerfilDialogFragment;
import accessweb.com.br.radiocontrole.activity.MainActivity;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoSyncClientManager;
import accessweb.com.br.radiocontrole.util.S3Util;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;
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

    private TransferUtility transferUtility;

    private ProgressDialog waitDialog;
    private String s3FileKey;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        transferUtility = S3Util.getTransferUtility(getActivity());

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
        System.out.println("AAAAAAAAAAAAAAAAA" + cacheData.getString("userUrlFoto"));
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
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;

        if (resultCode == Activity.RESULT_OK) {
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                /*try {
                    String path = getPath(picUri);
                    beginUpload(path);
                } catch (URISyntaxException e) {
                    Toast.makeText(getActivity(),
                            "Unable to get the file from the given URI.  See error log for details",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Unable to upload file from the given uri", e);
                }*/

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);

                    fotoUsuario.setImageBitmap(myBitmap);

                    File imageFile = new File(getContext().getCacheDir(), "imagem.jpg");

                    OutputStream os;
                    try {
                        os = new FileOutputStream(imageFile);
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.flush();
                        os.close();
                        try {
                            String path = getPath(Uri.fromFile(imageFile));
                            beginUpload(path);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                    }
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

    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getApplicationContext().getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*
     * A TransferListener class that can listen to a upload task and be notified
     * when the status changes.
     */
    private class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "Error during upload: " + id, e);
            closeWaitDialog();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
            //updateList();
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);
            String check =  "" + newState;
            if (check.equals("COMPLETED")){
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        S3Util.getS3Client(getContext()).setObjectAcl("radiocontrole/users", s3FileKey , CannedAccessControlList.PublicRead);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);

                        Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");
                        CacheData cacheData = new CacheData(getContext());
                        profileData.put("name", cacheData.getString("userNome"));
                        profileData.put("email", cacheData.getString("userEmail"));
                        profileData.put("phone", cacheData.getString("userTelefone"));
                        profileData.put("picture", "http://s3.amazonaws.com/radiocontrole/users/" + s3FileKey);
                        profileData.synchronize(new SyncCallback() {

                            @Override
                            public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                                Log.d(TAG, "Sync success" + dataset);
                                System.out.println(dataset.get("picture"));
                                CacheData cacheData = new CacheData(getContext());
                                cacheData.putString("userUrlFoto", dataset.get("picture"));
                                closeWaitDialog();
                            }

                            @Override
                            public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                                //Log.d(TAG, "Conflict");
                                return false;
                            }

                            @Override
                            public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                                //Log.d(TAG, "Dataset deleted");
                                return false;
                            }

                            @Override
                            public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                                //Log.d(TAG, "Datasets merged");
                                return false;
                            }

                            @Override
                            public void onFailure(DataStorageException dse) {
                                //Log.e(TAG, "Sync fails", dse);
                            }
                        });
                    }
                }.execute();
            }
        }
    }

    private void beginUpload(String filePath) {
        showWaitDialog("Enviando foto...");
        Long timestampLong = System.currentTimeMillis()/1000;
        String timestamp = timestampLong.toString();

        if (filePath == null) {
            Toast.makeText(getActivity(), "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);

        String extension = file.getName().substring(file.getName().lastIndexOf("."));
        Log.e("AAAAAAAA","radiocontrole/users/" + timestamp + extension);
        s3FileKey = timestamp + extension;
        TransferObserver observer = transferUtility.upload("radiocontrole/users", timestamp + extension,
                file);

        observer.setTransferListener(new UploadListener());
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

