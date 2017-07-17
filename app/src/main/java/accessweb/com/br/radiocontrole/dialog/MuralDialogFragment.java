package accessweb.com.br.radiocontrole.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Post;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;
import accessweb.com.br.radiocontrole.util.S3Util;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MuralDialogFragment extends DialogFragment {

    private static String TAG = MuralDialogFragment.class.getSimpleName();

    private String modalTitle;
    private String modalTipo;
    private Button btnEnviarPostagem;
    private Button btnAdicionarFoto;
    private FloatingActionButton gravarAudio;
    private EditText textoPublicacao;
    private RelativeLayout templateFormImagem;
    private RelativeLayout templateFormAudio;
    private ImageView imgFoto;
    private TextView nomeFoto;
    private String randomNomeFoto;
    private long timeWhenStopped = 0;
    private Chronometer tempoAudio;
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String RandomAudioFileName = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final int RequestPermissionCode = 1;
    private Random random;
    private Bitmap myBitmap;
    private Uri picUri;
    private Uri outputFileUri;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    private TransferUtility transferUtility;
    TransferListener listener = new UploadListener();

    private File imageFile;
    private ProgressDialog waitDialog;

    private Observable<String> myObservable;
    private Observer myObserver;
    private String uploadCheck = "";
    private String s3FileKey;

    public MuralDialogFragment(String title, String modal) {
        modalTitle =  title;
        modalTipo = modal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final CacheData cacheData = new CacheData(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_modal_mural, null, false);
        myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext(uploadCheck);
                        sub.onCompleted();
                    }
                }
        );

        transferUtility = S3Util.getTransferUtility(getActivity());

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.muralToolbar);
        toolbar.setTitle(modalTitle);
        toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_down_white);

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

        // INSTANCIANDO ELEMENTOS DA TELA
        textoPublicacao = (EditText) rootView.findViewById(R.id.textoPublicacao);
        btnEnviarPostagem = (Button) rootView.findViewById(R.id.btnEnviarPostagem);
        btnAdicionarFoto = (Button) rootView.findViewById(R.id.btnAdicionarFoto);
        gravarAudio = (FloatingActionButton) rootView.findViewById(R.id.gravarAudio);
        templateFormImagem = (RelativeLayout) rootView.findViewById(R.id.templateFormImagem);
        templateFormAudio = (RelativeLayout) rootView.findViewById(R.id.templateFormAudio);
        imgFoto = (ImageView) rootView.findViewById(R.id.imgFoto);
        nomeFoto = (TextView) rootView.findViewById(R.id.nomeFoto);
        tempoAudio = (Chronometer) rootView.findViewById(R.id.tempoAudio);

        Drawable wrappedDrawable = DrawableCompat.wrap(textoPublicacao.getBackground());
        DrawableCompat.setTint(wrappedDrawable.mutate(), Color.parseColor(cacheData.getString("color")));
        textoPublicacao.setBackgroundDrawable(wrappedDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textoPublicacao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(cacheData.getString("color"))));
        }

        btnEnviarPostagem.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnEnviarPostagem.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(cacheData.getString("color"))));
        }
        if (modalTipo == "texto") {
            textoPublicacao.setVisibility(View.VISIBLE);
        } else if (modalTipo == "imagem") {
            textoPublicacao.setVisibility(View.VISIBLE);
            templateFormImagem.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT > 23) {
                if (permissionsToRequest.size() > 0)
                    getActivity().requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }else {

                int permission1 = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                int permission2 = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission1 == PermissionChecker.PERMISSION_GRANTED) {
                    Log.w("aaa", "aaaa1");
                } else {
                    Log.w("aaa", "aaaa2");
                }

                if (permission2 == PermissionChecker.PERMISSION_GRANTED) {
                    Log.w("bbb", "bbbb1");
                } else {
                    Log.w("bbb", "bbbb2");
                }

            }

        } else if (modalTipo == "audio") {
            templateFormAudio.setVisibility(View.VISIBLE);
        }

        // ADICIONAR CLIQUE BTN ADICIONAR FOTO
        btnAdicionarFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

        random = new Random();

        //ADICIONAR CLIQUE BTN GRAVAR ÁUDIO
        gravarAudio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (gravarAudio.getTag().equals("record")){
                    if(checkPermission()) {
                        AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.m4a";

                        MediaRecorderReady();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();

                            tempoAudio.setBase(SystemClock.elapsedRealtime());
                            tempoAudio.start();

                            gravarAudio.setTag("stop");
                            gravarAudio.setImageResource(R.drawable.ic_stop_white);
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        requestPermission();
                    }
                } else if (gravarAudio.getTag().equals("stop")){
                    gravarAudio.setTag("play");
                    gravarAudio.setImageResource(R.drawable.ic_play_white);
                    timeWhenStopped = tempoAudio.getBase() - SystemClock.elapsedRealtime();

                    tempoAudio.stop();

                    mediaRecorder.stop();
                } else if (gravarAudio.getTag().equals("play")){
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(AudioSavePathInDevice);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();

                    gravarAudio.setTag("deletar");
                    gravarAudio.setImageResource(R.drawable.ic_delete_white);
                } else if (gravarAudio.getTag().equals("deletar")){
                    AlertDialog.Builder customBuilder  = new AlertDialog.Builder(getActivity());
                    customBuilder .setTitle("Deletar");
                    customBuilder .setMessage("Você tem certeza que deseja deletar esta gravação?");
                    customBuilder .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tempoAudio.setBase(SystemClock.elapsedRealtime());
                            gravarAudio.setTag("record");
                            gravarAudio.setImageResource(R.drawable.ic_microphone_white);
                            dialog.dismiss();
                        }
                    });

                    customBuilder .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = customBuilder.create();

                    dialog.show();
                    Button btnNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    btnNegative.setTextColor(cacheData.getInt("color"));

                    Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPositive.setTextColor(cacheData.getInt("color"));
                }
            }
        });

        // ADICIONAR CLIQUE BTN ENVIAR POSTAGEM
        btnEnviarPostagem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG,"click btn enviar postagem - texto: " + textoPublicacao.getText());


                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);

                final CacheData cacheData = new CacheData(getApplicationContext());
                final Post post = new Post();
                post.setAuthorId(CognitoClientManager.getCredentials().getIdentityId());
                post.setAuthorName(cacheData.getString("userNome"));
                post.setAuthorPicture(cacheData.getString("userUrlFoto"));
                if (modalTipo == "texto") {
                    post.setType("text");
                    post.setContent(textoPublicacao.getText().toString());
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            client.radioIdPostsPost("tradicaoAM", post);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            AlertDialog.Builder customBuilder  = new AlertDialog.Builder(getActivity());
                            customBuilder.setTitle("Rádio Controle");
                            customBuilder.setMessage("Postagem realizada com sucesso, aguarde a aprovação da moderação.");
                            customBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    dismiss();
                                }
                            });

                            AlertDialog dialog = customBuilder.create();

                            dialog.show();
                            Button btnPositive = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                            btnPositive.setTextColor(Color.parseColor(cacheData.getString("color")));

                        }
                    }.execute();
                } else if (modalTipo == "imagem") {
                    post.setType("image");
                    post.setContent(textoPublicacao.getText().toString());
                    if (imageFile != null){
                        try {
                            String path = getPath(Uri.fromFile(imageFile));
                            post.setAttachment(beginUpload(path));
                            System.out.println("////" + post.getAttachment());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        myObserver = new Observer<String>() {

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String text) {
                                Log.e("AAAAAAAAAA", uploadCheck);
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        client.radioIdPostsPost("tradicaoAM", post);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void result) {
                                        super.onPostExecute(result);
                                        AlertDialog.Builder customBuilder  = new AlertDialog.Builder(getActivity());
                                        customBuilder.setTitle("Rádio Controle");
                                        customBuilder.setMessage("Postagem realizada com sucesso, aguarde a aprovação da moderação.");
                                        customBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                dismiss();
                                            }
                                        });

                                        AlertDialog dialog = customBuilder.create();

                                        dialog.show();
                                        Button btnPositive = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                                        btnPositive.setTextColor(Color.parseColor(cacheData.getString("color")));

                                    }
                                }.execute();
                            }
                        };
                    }else {
                        Toast.makeText(getApplicationContext(), "Selecione uma imagem para enviar.", Toast.LENGTH_SHORT) .show();
                    }


                } else if (modalTipo == "audio") {
                    post.setType("audio");
                    post.setAttachment("");
                }

                /*new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        client.radioIdPostsPost("tradicaoAM", post);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        AlertDialog.Builder customBuilder  = new AlertDialog.Builder(getActivity());
                        customBuilder.setTitle("Rádio Controle");
                        customBuilder.setMessage("Postagem realizada com sucesso, aguarde a aprovação da moderação.");
                        customBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dismiss();
                            }
                        });

                        AlertDialog dialog = customBuilder.create();

                        dialog.show();
                        Button btnPositive = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                        btnPositive.setTextColor(Color.parseColor(cacheData.getString("color")));

                    }
                }.execute();*/
            }
        });

        return rootView;
    }

    public Intent getPickImageChooserIntent() {

        randomNomeFoto = CreateRandomAudioFileName(5);
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
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), randomNomeFoto + ".png"));
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
                    imgFoto.setImageBitmap(myBitmap);
                    nomeFoto.setText(randomNomeFoto + ".png");


                    imageFile = new File(getApplicationContext().getCacheDir(), "imagem.jpg");

                    OutputStream os;
                    try {
                        os = new FileOutputStream(imageFile);
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                bitmap = (Bitmap) data.getExtras().get("data");

                myBitmap = bitmap;
                imgFoto.setImageBitmap(myBitmap);
                nomeFoto.setText(randomNomeFoto + ".png");
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
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);
            String check =  "" + newState;
            if (check.equals("COMPLETED")){
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        S3Util.getS3Client(getApplicationContext()).setObjectAcl("radiocontrole/radios/tradicaoAM/wall", s3FileKey , CannedAccessControlList.PublicRead);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);

                        uploadCheck = "uploaded";
                        myObservable.subscribe(myObserver);
                    }
                }.execute();
            }
        }
    }

    private String beginUpload(String filePath) {

        //showWaitDialog("Enviando foto...");

        Long timestampLong = System.currentTimeMillis()/1000;
        String timestamp = timestampLong.toString();


        if (filePath == null) {
            Toast.makeText(getActivity(), "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return null;
        }
        File file = new File(filePath);

        String extension = file.getName().substring(file.getName().lastIndexOf("."));
        Log.e("AAAAAAAA","radiocontrole/users/" + timestamp + extension);
        s3FileKey = timestamp + extension;
        TransferObserver observer = transferUtility.upload("radiocontrole/radios/tradicaoAM/wall", timestamp + extension,
                file);
        observer.setTransferListener(new UploadListener());
        return "http://s3.amazonaws.com/radiocontrole/radios/tradicaoAM/wall/" + timestamp + extension;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        return dialog;
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


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
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
        waitDialog = new ProgressDialog(getApplicationContext());
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