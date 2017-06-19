package accessweb.com.br.radiocontrole.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Mural;

import static accessweb.com.br.radiocontrole.R.id.templateAudio;

public class MuralListAdapter extends RecyclerView.Adapter<MuralListAdapter.MyViewHolder> {
    List<Mural> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private String timeAgo;


    public MuralListAdapter(Context context, List<Mural> data) {
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
        View view = inflater.inflate(R.layout.mural_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Mural current = data.get(position);


        if (current.getModeloPublicacao() == "texto"){
            holder.templateTexto.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(current.getFotoUsuario())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.fotoUsuario1);
            holder.nomeUsuario1.setText(current.getNomeUsuario());
            holder.tempoPublicacao1.setText(current.getTempoPublicacao());
            holder.textoPublicacao1.setText(current.getTextoPublicacao());
        } else if (current.getModeloPublicacao() == "imagem") {
            holder.templateImagem.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(current.getFotoUsuario())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.fotoUsuario2);
            holder.nomeUsuario2.setText(current.getNomeUsuario());
            holder.tempoPublicacao2.setText(current.getTempoPublicacao());
            holder.textoPublicacao2.setText(current.getTextoPublicacao());
            Picasso.with(context)
                    .load(current.getImagemPublicacao())
                    .placeholder(R.drawable.picture)
                    .error(R.drawable.picture)
                    .into(holder.imagemPublicacao);
        } else if (current.getModeloPublicacao() == "audio") {
            holder.templateAudio.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(current.getFotoUsuario())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.fotoUsuario3);
            holder.nomeUsuario3.setText(current.getNomeUsuario());
            holder.tempoPublicacao3.setText(current.getTempoPublicacao());

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearData() {
        int size = this.data.size();
        this.data.clear();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout templateTexto;
        RelativeLayout templateImagem;
        RelativeLayout templateAudio;

        ImageView fotoUsuario1;
        ImageView fotoUsuario2;
        ImageView fotoUsuario3;

        TextView nomeUsuario1;
        TextView nomeUsuario2;
        TextView nomeUsuario3;

        TextView tempoPublicacao1;
        TextView tempoPublicacao2;
        TextView tempoPublicacao3;

        TextView textoPublicacao1;
        TextView textoPublicacao2;

        ImageView imagemPublicacao;

        TextView audioPublicacao;

        public MyViewHolder(View itemView) {
            super(itemView);

            templateTexto = (RelativeLayout) itemView.findViewById(R.id.templateTexto);
            templateImagem = (RelativeLayout) itemView.findViewById(R.id.templateImagem);
            templateAudio = (RelativeLayout) itemView.findViewById(R.id.templateAudio);

            fotoUsuario1 = (ImageView) itemView.findViewById(R.id.fotoUsuario1);
            fotoUsuario2 = (ImageView) itemView.findViewById(R.id.fotoUsuario2);
            fotoUsuario3 = (ImageView) itemView.findViewById(R.id.fotoUsuario3);

            nomeUsuario1 = (TextView) itemView.findViewById(R.id.nomeUsuario1);
            nomeUsuario2 = (TextView) itemView.findViewById(R.id.nomeUsuario2);
            nomeUsuario3 = (TextView) itemView.findViewById(R.id.nomeUsuario3);

            tempoPublicacao1 = (TextView) itemView.findViewById(R.id.tempoPublicacao1);
            tempoPublicacao2 = (TextView) itemView.findViewById(R.id.tempoPublicacao2);
            tempoPublicacao3 = (TextView) itemView.findViewById(R.id.tempoPublicacao3);

            textoPublicacao1 = (TextView) itemView.findViewById(R.id.textoPublicacao1);
            textoPublicacao2 = (TextView) itemView.findViewById(R.id.textoPublicacao2);

            imagemPublicacao = (ImageView) itemView.findViewById(R.id.imagemPublicacao);

            audioPublicacao = (TextView) itemView.findViewById(R.id.audioPublicacao);
        }
    }
}
