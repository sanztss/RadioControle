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
import accessweb.com.br.radiocontrole.model.Premio;

/**
 * Created by Des. Android on 30/06/2017.
 */

public class PremiosAdapter extends RecyclerView.Adapter<PremiosAdapter.MyViewHolder> {

    List<Premio> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public PremiosAdapter(Context context, List<Premio> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.premio_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Premio current = data.get(position);

        Picasso.with(context)
                .load(current.getImagemPremio())
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(holder.imagemPremio);
        holder.tituloPremio.setText(current.getTituloPremio());
    }

    @Override
    public int getItemCount() {
        return  this.data == null ? 0 : this.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemPremio;
        TextView tituloPremio;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagemPremio = (ImageView) itemView.findViewById(R.id.imagemPremio);
            tituloPremio = (TextView) itemView.findViewById(R.id.tituloPremio);
        }
    }

}
