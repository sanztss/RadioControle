package accessweb.com.br.radiocontrole.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.activity.ForgotPasswordActivity;
import accessweb.com.br.radiocontrole.activity.PromocaoActivity;
import accessweb.com.br.radiocontrole.model.Promocao;

import static java.security.AccessController.getContext;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PromocoesAdapter extends RecyclerView.Adapter<PromocoesAdapter.MyViewHolder> {

    List<Promocao> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public PromocoesAdapter(Context context, List<Promocao> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.promocao_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Promocao current = data.get(position);

        Picasso.with(context)
                .load(current.getImagemPromocao())
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(holder.imagemPromocao);
        holder.tituloPromocao.setText(current.getTituloPromocao());
        String string = "04/08/2017";
        /*DateFormat format = new SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(string);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        holder.dataEncerramentoPromocao.setText(current.getDataEncerramentoPromocao());
        holder.dataSorteioPromocao.setText(current.getDataSorteioPromocao());
        /*if (current.getParticipando()){
            holder.participando.setVisibility(View.VISIBLE);
        }else {
            holder.participando.setVisibility(View.GONE);
        }*/

        holder.btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Compartilhar.");
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "No dia " + current.getDataEncerramentoPromocao() + " encerrará a promoção " + current.getTituloPromocao() + ", venha participar comigo, baixe já o aplicativo Rádio controle:\nVersão Android: https://play.google.com/store/apps/details?id=br.com.devmaker.radiocontroletest\nVersão iOS: https://itunes.apple.com/us/app/access-mobile-rádio-controle/id905070426?mt=8");
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent,"Compartilhar no:" ));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Click Item");
                Intent intent = new Intent(context, PromocaoActivity.class);
                intent.putExtra("posicao", position);
                intent.putExtra("lista", (Serializable) data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  this.data == null ? 0 : this.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemPromocao;
        TextView tituloPromocao;
        TextView dataEncerramentoPromocao;
        TextView dataSorteioPromocao;
        TextView participando;
        ImageButton btnCompartilhar;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagemPromocao = (ImageView) itemView.findViewById(R.id.imagemPromocao);
            tituloPromocao = (TextView) itemView.findViewById(R.id.tituloPromocao);
            dataEncerramentoPromocao = (TextView) itemView.findViewById(R.id.dataEncerramentoPromocao);
            dataSorteioPromocao = (TextView) itemView.findViewById(R.id.dataSorteioPromocao);
            participando = (TextView) itemView.findViewById(R.id.participando);
            btnCompartilhar = (ImageButton) itemView.findViewById(R.id.btnCompartilhar);
        }
    }

}

