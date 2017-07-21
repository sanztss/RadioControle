package accessweb.com.br.radiocontrole.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Programa;
import accessweb.com.br.radiocontrole.util.AlarmReceiver;
import accessweb.com.br.radiocontrole.util.CacheData;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class ProgramacaoAdapter extends RecyclerView.Adapter<ProgramacaoAdapter.MyViewHolder> {

    List<Programa> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> programasNotificar = null;

    public ProgramacaoAdapter(Context context, List<Programa> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dia_semana_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Programa current = data.get(position);
        final CacheData cacheData = new CacheData(context);
        final Calendar now = Calendar.getInstance();
        holder.horaInicioPrograma.setText(current.getHoraInicioPrograma());
        holder.nomePrograma.setText(current.getNomePrograma());
        if (current.getNomeLocutorPrograma() != null){
            Picasso.with(context)
                    .load(current.getFotoLocutorPrograma())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.fotoLocutorPrograma);
            holder.nomeLocutorPrograma.setText(current.getNomeLocutorPrograma());
        }else {
            holder.fotoLocutorPrograma.getLayoutParams().height = 0;
            holder.fotoLocutorPrograma.getLayoutParams().width = 0;
            holder.fotoLocutorPrograma.requestLayout();
            holder.nomeLocutorPrograma.getLayoutParams().height = 0;
            holder.nomeLocutorPrograma.getLayoutParams().width = 0;
            holder.nomeLocutorPrograma.requestLayout();
        }

        if (current.getNotificarPrograma()){
            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
            holder.btnNotificar.setTag("naoNotificar");
        }

        holder.btnNotificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if (holder.btnNotificar.getTag().equals("notificar")){
                    final String[] selectedItem = {null};

                    RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.0f);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Com quantos minutos antes quer ser notificado?");
                    spannableStringBuilder.setSpan(
                            relativeSizeSpan,
                            0,
                            1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.WRAP_CONTENT
                    );
                    LinearLayout LLayout = new LinearLayout(context);
                    LLayout.setOrientation(LinearLayout.VERTICAL);
                    params.setMargins(15,15,15,15);
                    LLayout.setLayoutParams(params);
                    LLayout.setPadding(15,15,15,15);
                    TextView tv_title = new TextView(context);
                    tv_title.setLayoutParams(params);
                    tv_title.setTextColor(Color.parseColor("#0074c8"));
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                    tv_title.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv_title.setText(spannableStringBuilder);
                    LLayout.addView(tv_title);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setCustomTitle(LLayout);

                    final String[] minutos = new String[]{
                            "5 Minutos",
                            "15 Minutos",
                            "30 Minutos",
                            "60 Minutos",
                    };

                    builder.setSingleChoiceItems(
                            minutos,
                            -1,
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    selectedItem[0] = Arrays.asList(minutos).get(i);
                                }
                            });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Date rightNow = Calendar.getInstance().getTime();
                            Log.v("aaaa", "" + rightNow);

                            if (selectedItem[0] != null){
                                switch (selectedItem[0]){
                                    case "5 Minutos":
                                        if (now.getTime().before(current.getDataPrograma().getTime())){
                                            long min5 = (current.getDataPrograma().getTimeInMillis() - now.getTimeInMillis()) - 5 * 60000;
                                            setUpAlarm(min5, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
                                            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
                                            holder.btnNotificar.setTag("naoNotificar");
                                            programasNotificar.add(current.getIdPrograma() + current.getDiaPrograma());
                                            cacheData.putListString("programasNotificar", programasNotificar);
                                        }else {
                                            current.getDataPrograma().add(Calendar.DATE, 7);
                                            long min5 = (current.getDataPrograma().getTimeInMillis() - now.getTimeInMillis()) - 5 * 60000;
                                            setUpAlarm(min5, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
                                            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
                                            holder.btnNotificar.setTag("naoNotificar");
                                            programasNotificar.add(current.getIdPrograma() + current.getDiaPrograma());
                                            cacheData.putListString("programasNotificar", programasNotificar);
                                        }
                                        break;
                                    case "15 Minutos":
                                        if (now.getTime().before(current.getDataPrograma().getTime())){
                                            long min15 = (current.getDataPrograma().getTimeInMillis() - now.getTimeInMillis()) - 15 * 60000;
                                            setUpAlarm(min15, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
                                            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
                                            holder.btnNotificar.setTag("naoNotificar");
                                            programasNotificar.add(current.getIdPrograma() + current.getDiaPrograma());
                                            cacheData.putListString("programasNotificar", programasNotificar);
                                        }else {
                                            current.getDataPrograma().add(Calendar.DATE, 7);
                                            long min15 = (current.getDataPrograma().getTimeInMillis() - now.getTimeInMillis()) - 15 * 60000;
                                            setUpAlarm(min15, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
                                            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
                                            holder.btnNotificar.setTag("naoNotificar");
                                            programasNotificar.add(current.getIdPrograma() + current.getDiaPrograma());
                                            cacheData.putListString("programasNotificar", programasNotificar);
                                        }
                                        break;
                                    case "30 Minutos":
                                        if (now.getTime().before(current.getDataPrograma().getTime())){
                                            long min30 = (current.getDataPrograma().getTimeInMillis() - now.getTimeInMillis()) - 30 * 60000;
                                            setUpAlarm(min30, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
                                            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
                                            holder.btnNotificar.setTag("naoNotificar");
                                            programasNotificar.add(current.getIdPrograma() + current.getDiaPrograma());
                                            cacheData.putListString("programasNotificar", programasNotificar);
                                        }else {
                                            current.getDataPrograma().add(Calendar.DATE, 7);
                                            long min30 = (current.getDataPrograma().getTimeInMillis() - now.getTimeInMillis()) - 30 * 60000;
                                            setUpAlarm(min30, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
                                            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
                                            holder.btnNotificar.setTag("naoNotificar");
                                            programasNotificar.add(current.getIdPrograma() + current.getDiaPrograma());
                                            cacheData.putListString("programasNotificar", programasNotificar);
                                        }
                                        break;
                                    case "60 Minutos":
                                        if (now.getTime().before(current.getDataPrograma().getTime())){
                                            long min60 = (current.getDataPrograma().getTimeInMillis() - now.getTimeInMillis()) - 60 * 60000;
                                            setUpAlarm(min60, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
                                            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
                                            holder.btnNotificar.setTag("naoNotificar");
                                            programasNotificar.add(current.getIdPrograma() + current.getDiaPrograma());
                                            cacheData.putListString("programasNotificar", programasNotificar);
                                        }else {
                                            current.getDataPrograma().add(Calendar.DATE, 7);
                                            long min60 = (current.getDataPrograma().getTimeInMillis() - now.getTimeInMillis()) - 60 * 60000;
                                            setUpAlarm(min60, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
                                            holder.btnNotificar.setImageResource(R.drawable.ic_bell_green);
                                            holder.btnNotificar.setTag("naoNotificar");
                                            programasNotificar.add(current.getIdPrograma() + current.getDiaPrograma());
                                            cacheData.putListString("programasNotificar", programasNotificar);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });

                    builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog dialog = builder.create();

                    dialog.show();
                    CacheData cacheData = new CacheData(context);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(cacheData.getString("color")));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor(cacheData.getString("color")));
                }else {
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,  intent, 0);
                    AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                    holder.btnNotificar.setImageResource(R.drawable.ic_bell_gray);
                    holder.btnNotificar.setTag("notificar");

                    Log.e("programasNotificar", "" + programasNotificar.size());
                    for (String programaNotificar : programasNotificar){
                        if (programaNotificar.equals(current.getIdPrograma() + current.getDiaPrograma())){
                            programasNotificar.remove(programaNotificar);
                            break;
                        }
                    }
                    cacheData.putListString("programasNotificar", programasNotificar);
                }

            }
        });

        holder.btnFavoritar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão favoritar.");
            }
        });

        holder.btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Às " + current.getHoraInicioPrograma() + " começará o programa " + current.getNomePrograma() + ", venha ouvir comigo, baixe já o aplicativo Rádio controle:\nVersão Android: https://play.google.com/store/apps/details?id=br.com.devmaker.radiocontroletest\nVersão iOS: https://itunes.apple.com/us/app/access-mobile-rádio-controle/id905070426?mt=8");
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent,"Compartilhar no:" ));
            }
        });
    }

    @Override
    public int getItemCount() {
        return  this.data == null ? 0 : this.data.size();
    }

    public void clear() {
        if (this.data != null){
            this.data.clear();
            notifyDataSetChanged();
        }
    }

    public void addAll(List<Programa> list) {
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView horaInicioPrograma;
        TextView nomePrograma;
        CircleImageView fotoLocutorPrograma;
        TextView nomeLocutorPrograma;
        ImageButton btnNotificar;
        ImageButton btnFavoritar;
        ImageButton btnCompartilhar;



        public MyViewHolder(View itemView) {
            super(itemView);

            final CacheData cacheData = new CacheData(context);
            programasNotificar = cacheData.getListString("programasNotificar");
            horaInicioPrograma = (TextView) itemView.findViewById(R.id.horaInicioPrograma);
            nomePrograma = (TextView) itemView.findViewById(R.id.nomePrograma);
            fotoLocutorPrograma = (CircleImageView) itemView.findViewById(R.id.fotoLocutorPrograma);
            nomeLocutorPrograma = (TextView) itemView.findViewById(R.id.nomeLocutorPrograma);
            btnNotificar = (ImageButton) itemView.findViewById(R.id.btnNotificar);
            btnFavoritar = (ImageButton) itemView.findViewById(R.id.btnFavoritar);
            btnCompartilhar = (ImageButton) itemView.findViewById(R.id.btnCompartilhar);
        }
    }

    public void setUpAlarm(long triggerTimeInMS, String message) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,  intent, 0);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeInMS , pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000 , pendingIntent);
        Toast toast = Toast.makeText(context, "Alarm configurado e ativado", Toast.LENGTH_SHORT);
        toast.show();
    }
}
