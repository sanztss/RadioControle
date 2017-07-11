package accessweb.com.br.radiocontrole.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Des. Android on 21/06/2017.
 */

public class GridIconAdapter extends BaseAdapter {
    private Context mContext;
    private List<Integer> drawables = null;

    // Constructor
    public GridIconAdapter(Context context, List<Integer> socialIconIds) {
        this.mContext = context;
        this.drawables = socialIconIds;
    }

    public int getCount() {
        return drawables.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        AppCompatImageView imageView;

        if (convertView == null) {
            imageView = new AppCompatImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (AppCompatImageView) convertView;
        }
        imageView.setImageResource(drawables.get(position));
        return imageView;
    }
}