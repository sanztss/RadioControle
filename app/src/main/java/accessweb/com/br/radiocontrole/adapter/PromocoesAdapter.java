package accessweb.com.br.radiocontrole.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Promocao;

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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Promocao current = data.get(position);

        //holder.horaInicioPromocao.setText(current.getHoraInicioPromocao());
    }

    @Override
    public int getItemCount() {
        return  this.data == null ? 0 : this.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {



        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }

}

