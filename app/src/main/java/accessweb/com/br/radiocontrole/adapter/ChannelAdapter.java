package accessweb.com.br.radiocontrole.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.model.ChannelItem;

public class ChannelAdapter extends ArrayAdapter<ChannelItem> {

    Context context;
    int layoutResourceId;   
    ChannelItem data[] = null;
   
    public ChannelAdapter(Context context, int layoutResourceId, ChannelItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChannelHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.channel_row, parent, false);
           
            holder = new ChannelHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.icon);
            holder.txtTitle = (TextView)row.findViewById(R.id.title);
           
            row.setTag(holder);
        }
        else
        {
            holder = (ChannelHolder)row.getTag();
        }
       
        ChannelItem weather = data[position];
        holder.txtTitle.setText(weather.title);
        holder.imgIcon.setImageResource(weather.icon);
       
        return row;
    }
   
    static class ChannelHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}