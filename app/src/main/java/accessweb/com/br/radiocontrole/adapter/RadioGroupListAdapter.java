package accessweb.com.br.radiocontrole.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.RadioApp;

/**
 * Created by Des. Android on 19/07/2017.
 */

public class RadioGroupListAdapter extends RecyclerView.Adapter<RadioGroupListAdapter.MyViewHolder> {
    List<RadioApp> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;


    public RadioGroupListAdapter(Context context, List<RadioApp> data) {
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
        View view = inflater.inflate(R.layout.radiogroup_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RadioApp current = data.get(position);

        Picasso.with(context)
                .load(current.getUrlImagemRadio())
                .error(R.drawable.radio)
                .into(holder.urlImagemRadio);
        holder.nomeRadio.setText(current.getNomeRadio());
        holder.localizacaoRadio.setText(current.getLocalizacaoRadio());
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

    public void addAll(List<RadioApp> list) {
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView urlImagemRadio;
        TextView nomeRadio;
        TextView localizacaoRadio;

        public MyViewHolder(View itemView) {
            super(itemView);
            urlImagemRadio = (ImageView) itemView.findViewById(R.id.urlImagemRadio);
            nomeRadio = (TextView) itemView.findViewById(R.id.nomeRadio);
            localizacaoRadio = (TextView) itemView.findViewById(R.id.localizacaoRadio);
        }
    }
}

