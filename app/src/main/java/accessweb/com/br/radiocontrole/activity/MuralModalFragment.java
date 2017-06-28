package accessweb.com.br.radiocontrole.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import accessweb.com.br.radiocontrole.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.R.attr.permission;

public class MuralModalFragment extends DialogFragment {

    private static String TAG = ModalListFragment.class.getSimpleName();

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

    public MuralModalFragment(String title, String modal) {
        modalTitle =  title;
        modalTipo = modal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_modal_mural, null, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.muralToolbar);
        toolbar.setTitle(modalTitle);
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

        if (modalTipo == "texto") {
            textoPublicacao.setVisibility(View.VISIBLE);
        } else if (modalTipo == "imagem") {
            textoPublicacao.setVisibility(View.VISIBLE);
            templateFormImagem.setVisibility(View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT > 23) {
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
                    btnNegative.setTextColor(getResources().getColor(R.color.colorPrimary));

                    Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPositive.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        // ADICIONAR CLIQUE BTN ENVIAR POSTAGEM
        btnEnviarPostagem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG,"click btn enviar postagem - texto: " + textoPublicacao.getText());
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
}