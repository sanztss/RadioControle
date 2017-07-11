package accessweb.com.br.radiocontrole.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.Pais;

/**
 * Created by Des. Android on 11/07/2017.
 */

public class PaisAdapter extends ArrayAdapter<Pais> implements SpinnerAdapter {

    private LayoutInflater mLayoutInflater;
    private static Context context;
    List<Pais> data = Collections.emptyList();

    public PaisAdapter(Context context, List<Pais> data) {
        super(context, 0);
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    public int getCount() {
        return data.size();
    }

    public Pais getItem(int i) {
        return data.get(i);
    }

    public long getItemId(int i) {
        return (long)i;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = mLayoutInflater.inflate(R.layout.pais_row, parent, false);
        holder = new ViewHolder();
        holder.bandeiraPais = (ImageView) convertView.findViewById(R.id.bandeiraPais);
        holder.nomePais = (TextView) convertView.findViewById(R.id.nomePais);
        holder.codigoPais = (TextView) convertView.findViewById(R.id.codigoPais);
        convertView.setTag(holder);
        Pais pais = getItem(position);
        holder.nomePais.setText(pais.getNomePais());
        holder.codigoPais.setText(pais.getCodigoPais());
        int resourceId = context.getResources().getIdentifier(pais.getBandeiraPais(), "drawable", context.getPackageName());
        holder.bandeiraPais.setImageResource(resourceId);
        return convertView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Pais pais = getItem(position);
        convertView = mLayoutInflater.inflate(R.layout.pais_selected, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        int resourceId = context.getResources().getIdentifier(pais.getBandeiraPais(), "drawable", context.getPackageName());
        imageView.setImageResource(resourceId);
        return convertView;
    }

    private static class ViewHolder {
        public ImageView bandeiraPais;
        public TextView nomePais;
        public TextView codigoPais;
    }
}
