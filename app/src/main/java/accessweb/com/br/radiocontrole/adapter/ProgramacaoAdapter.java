package accessweb.com.br.radiocontrole.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Programa;
import accessweb.com.br.radiocontrole.util.AlarmReceiver;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class ProgramacaoAdapter extends RecyclerView.Adapter<ProgramacaoAdapter.MyViewHolder> {

    List<Programa> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Programa current = data.get(position);

        holder.horaInicioPrograma.setText(current.getHoraInicioPrograma());
        holder.nomePrograma.setText(current.getNomePrograma());
        Picasso.with(context)
                .load(current.getFotoLocutorPrograma())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(holder.fotoLocutorPrograma);
        holder.nomeLocutorPrograma.setText(current.getNomeLocutorPrograma());

        holder.btnNotificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão notificar.");
                setUpAlarm(10 * 1000, "O programa " + current.getNomePrograma() + " que você marcou um lembrete começa às " + current.getHoraInicioPrograma() + ".");
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
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,  intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+triggerTimeInMS , pendingIntent);
        Toast toast = Toast.makeText(context, "Alarm configurado e ativado", Toast.LENGTH_SHORT);
        toast.show();
    }
}
